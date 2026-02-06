package com.atparui.rmsservice.tenant;

import com.atparui.rmsservice.tenant.domain.TenantDatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Manages tenant-specific JDBC DataSource connections.
 * Creates and caches DataSource pools per tenant.
 */
@Component
public class TenantJdbcConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(TenantJdbcConnectionManager.class);

    private final ConcurrentMap<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();
    private final GatewayTenantService gatewayTenantService;
    private final MultiTenantProperties properties;

    public TenantJdbcConnectionManager(GatewayTenantService gatewayTenantService, MultiTenantProperties properties) {
        this.gatewayTenantService = gatewayTenantService;
        this.properties = properties;
    }

    /**
     * Get or create a DataSource for the given tenant.
     *
     * @param tenantId the tenant ID
     * @return DataSource for the tenant
     */
    public DataSource getDataSource(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or blank");
        }

        // Check cache first
        DataSource cached = dataSourceCache.get(tenantId);
        if (cached != null) {
            LOG.debug("Retrieved JDBC DataSource from cache for tenant: {}", tenantId);
            return cached;
        }

        // Fetch tenant config and create DataSource
        TenantDatabaseConfig config = gatewayTenantService.getTenantDatabaseConfig(tenantId).block();
        if (config == null) {
            throw new RuntimeException("Tenant database configuration not found for tenant: " + tenantId);
        }

        try {
            DataSource dataSource = createDataSource(config);
            dataSourceCache.put(tenantId, dataSource);
            LOG.info("Created and cached JDBC DataSource for tenant: {}", tenantId);
            return dataSource;
        } catch (Exception e) {
            LOG.error("Failed to create JDBC DataSource for tenant {}: {}", tenantId, e.getMessage(), e);
            throw new RuntimeException("Failed to create JDBC DataSource for tenant: " + tenantId, e);
        }
    }

    private DataSource createDataSource(TenantDatabaseConfig config) {
        // Parse database URL - support both jdbc: and r2dbc: URLs
        String dbUrl = config.getDatabaseUrl();
        if (dbUrl.startsWith("r2dbc:")) {
            // Convert r2dbc URL to JDBC URL
            dbUrl = dbUrl.replace("r2dbc:", "jdbc:");
        } else if (!dbUrl.startsWith("jdbc:")) {
            // If no prefix, assume it's a PostgreSQL connection string and add jdbc: prefix
            dbUrl = "jdbc:" + dbUrl;
        }

        // Strip jdbc: and p6spy: prefixes to get the actual database URL for parsing
        String parseableUrl = dbUrl.replace("jdbc:", "").replace("p6spy:", "");

        URI dbUri = URI.create(parseableUrl);

        String host = dbUri.getHost();
        int port = dbUri.getPort() > 0 ? dbUri.getPort() : 5432;
        String database = dbUri.getPath() != null ? dbUri.getPath().replaceFirst("/", "") : "";
        String query = dbUri.getQuery();

        // Build JDBC URL - use original dbUrl if it has p6spy prefix, otherwise use postgresql directly
        StringBuilder jdbcUrl = new StringBuilder();
        if (config.getDatabaseUrl().contains("p6spy:")) {
            jdbcUrl.append("jdbc:p6spy:postgresql://");
        } else {
            jdbcUrl.append("jdbc:postgresql://");
        }
        jdbcUrl.append(host).append(":").append(port).append("/").append(database);
        if (query != null && !query.isEmpty()) {
            jdbcUrl.append("?").append(query);
        }

        // Configure HikariCP connection pool
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl.toString());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setMaximumPoolSize(config.getMaxPoolSize() != null ? config.getMaxPoolSize() : 20);
        hikariConfig.setMinimumIdle(properties.getConnection().getMinPoolSize());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout() != null ? config.getConnectionTimeout() : 30000);
        hikariConfig.setConnectionTestQuery(config.getValidationQuery());
        hikariConfig.setPoolName("HikariCP-Tenant-" + config.getTenantId());
        hikariConfig.setAutoCommit(false); // Use transactions

        return new HikariDataSource(hikariConfig);
    }

    /**
     * Remove DataSource from cache and close it.
     *
     * @param tenantId the tenant ID
     */
    public void removeDataSource(String tenantId) {
        DataSource dataSource = dataSourceCache.remove(tenantId);
        if (dataSource != null && dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
            LOG.info("Removed and closed JDBC DataSource for tenant: {}", tenantId);
        }
    }

    /**
     * Get all cached tenant IDs.
     *
     * @return set of tenant IDs
     */
    public java.util.Set<String> getCachedTenantIds() {
        return dataSourceCache.keySet();
    }

    /**
     * Clear all DataSources.
     */
    public void clearAll() {
        LOG.info("Clearing all JDBC DataSources");
        dataSourceCache.forEach((tenantId, dataSource) -> {
            if (dataSource instanceof HikariDataSource) {
                ((HikariDataSource) dataSource).close();
            }
        });
        dataSourceCache.clear();
    }
}
