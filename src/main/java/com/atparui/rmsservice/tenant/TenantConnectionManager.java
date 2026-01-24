package com.atparui.rmsservice.tenant;

import com.atparui.rmsservice.tenant.domain.TenantDatabaseConfig;
import com.atparui.rmsservice.config.R2dbcProxyFactory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Manages tenant-specific database connection factories.
 * Creates and caches connection pools per tenant.
 */
@Component
public class TenantConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(TenantConnectionManager.class);

    private final ConcurrentMap<String, ConnectionFactory> connectionFactoryCache = new ConcurrentHashMap<>();
    private final GatewayTenantService gatewayTenantService;
    private final MultiTenantProperties properties;
    private final R2dbcProxyFactory r2dbcProxyFactory;

    public TenantConnectionManager(GatewayTenantService gatewayTenantService, MultiTenantProperties properties, R2dbcProxyFactory r2dbcProxyFactory) {
        this.gatewayTenantService = gatewayTenantService;
        this.properties = properties;
        this.r2dbcProxyFactory = r2dbcProxyFactory;
    }

    /**
     * Get or create a ConnectionFactory for the given tenant.
     *
     * @param tenantId the tenant ID
     * @return Mono containing the ConnectionFactory
     */
    public Mono<ConnectionFactory> getConnectionFactory(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return Mono.error(new IllegalArgumentException("Tenant ID cannot be null or blank"));
        }

        // Check cache first
        ConnectionFactory cached = connectionFactoryCache.get(tenantId);
        if (cached != null) {
            LOG.debug("Retrieved connection factory from cache for tenant: {}", tenantId);
            return Mono.just(cached);
        }

        // Fetch tenant config and create connection factory
        return gatewayTenantService
            .getTenantDatabaseConfig(tenantId)
            .flatMap(config -> {
                try {
                    ConnectionFactory factory = createConnectionFactory(config);
                    connectionFactoryCache.put(tenantId, factory);
                    LOG.info("Created and cached connection factory for tenant: {}", tenantId);
                    return Mono.just(factory);
                } catch (Exception e) {
                    LOG.error("Failed to create connection factory for tenant {}: {}", tenantId, e.getMessage(), e);
                    return Mono.error(new RuntimeException("Failed to create connection factory for tenant: " + tenantId, e));
                }
            });
    }

    private ConnectionFactory createConnectionFactory(TenantDatabaseConfig config) {
        // Parse database URL
        URI dbUri = URI.create(config.getDatabaseUrl().replace("r2dbc:", ""));

        String host = dbUri.getHost();
        int port = dbUri.getPort() > 0 ? dbUri.getPort() : 5432;
        String database = dbUri.getPath().replaceFirst("/", "");

        // Build PostgreSQL connection configuration
        PostgresqlConnectionConfiguration.Builder configBuilder = PostgresqlConnectionConfiguration.builder()
            .host(host)
            .port(port)
            .database(database)
            .username(config.getUsername())
            .password(config.getPassword());

        PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(configBuilder.build());

        // Wrap to log queries (bindings + timings) before pooling
        ConnectionFactory proxiedFactory = r2dbcProxyFactory.wrap(connectionFactory);

        // Wrap with connection pool
        ConnectionPoolConfiguration poolConfig = ConnectionPoolConfiguration.builder(proxiedFactory)
            .maxIdleTime(Duration.ofSeconds(30))
            .initialSize(properties.getConnection().getMinPoolSize())
            .maxSize(properties.getConnection().getMaxPoolSize())
            .maxCreateConnectionTime(Duration.ofMillis(config.getConnectionTimeout()))
            .validationQuery(config.getValidationQuery())
            .build();

        return new ConnectionPool(poolConfig);
    }

    /**
     * Remove connection factory from cache and close it.
     *
     * @param tenantId the tenant ID
     */
    public Mono<Void> removeConnectionFactory(String tenantId) {
        ConnectionFactory factory = connectionFactoryCache.remove(tenantId);
        if (factory != null) {
            LOG.info("Removed connection factory for tenant: {}", tenantId);
            if (factory instanceof ConnectionPool) {
                return ((ConnectionPool) factory).disposeLater();
            }
        }
        return Mono.empty();
    }

    /**
     * Get all cached tenant IDs.
     *
     * @return set of tenant IDs
     */
    public java.util.Set<String> getCachedTenantIds() {
        return connectionFactoryCache.keySet();
    }

    /**
     * Clear all connection factories.
     */
    public Mono<Void> clearAll() {
        LOG.info("Clearing all connection factories");
        connectionFactoryCache.clear();
        return Mono.empty();
    }
}
