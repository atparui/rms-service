package com.atparui.rmsservice.tenant;

import com.atparui.rmsservice.tenant.domain.TenantDatabaseConfig;
import io.r2dbc.spi.ConnectionFactory;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implementation of TenantConnectionProvider that provides both R2DBC and JDBC connections
 * based on tenant configuration.
 */
@Component
public class TenantConnectionProviderImpl implements TenantConnectionProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TenantConnectionProviderImpl.class);

    private final TenantConnectionManager r2dbcConnectionManager;
    private final TenantJdbcConnectionManager jdbcConnectionManager;
    private final GatewayTenantService gatewayTenantService;
    private final MultiTenantProperties properties;

    public TenantConnectionProviderImpl(
        TenantConnectionManager r2dbcConnectionManager,
        TenantJdbcConnectionManager jdbcConnectionManager,
        GatewayTenantService gatewayTenantService,
        MultiTenantProperties properties
    ) {
        this.r2dbcConnectionManager = r2dbcConnectionManager;
        this.jdbcConnectionManager = jdbcConnectionManager;
        this.gatewayTenantService = gatewayTenantService;
        this.properties = properties;
    }

    @Override
    public Mono<ConnectionFactory> getR2dbcConnectionFactory() {
        return TenantContextHolder.getCurrentTenantId().flatMap(this::getR2dbcConnectionFactory).switchIfEmpty(handleMissingTenantR2dbc());
    }

    @Override
    public DataSource getJdbcDataSource() {
        String tenantId = TenantContextHolder.getCurrentTenantId().block();
        if (tenantId == null || tenantId.isBlank()) {
            return handleMissingTenantJdbc();
        }
        return getJdbcDataSource(tenantId);
    }

    @Override
    public Mono<ConnectionFactory> getR2dbcConnectionFactory(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return handleMissingTenantR2dbc();
        }

        // Check if tenant uses JDBC
        if (isJdbc(tenantId)) {
            LOG.debug("Tenant {} uses JDBC, returning empty for R2DBC connection", tenantId);
            return Mono.empty();
        }

        return r2dbcConnectionManager.getConnectionFactory(tenantId);
    }

    @Override
    public DataSource getJdbcDataSource(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return handleMissingTenantJdbc();
        }

        // Check if tenant uses R2DBC
        if (!isJdbc(tenantId)) {
            LOG.debug("Tenant {} uses R2DBC, returning null for JDBC DataSource", tenantId);
            return null;
        }

        return jdbcConnectionManager.getDataSource(tenantId);
    }

    @Override
    public boolean isJdbc() {
        String tenantId = TenantContextHolder.getCurrentTenantId().block();
        if (tenantId == null || tenantId.isBlank()) {
            // Use default tenant for fallback
            if (properties.getFallback().isEnabled()) {
                tenantId = properties.getFallback().getDefaultTenantId();
            } else {
                return false; // Default to R2DBC if no tenant
            }
        }
        return isJdbc(tenantId);
    }

    @Override
    public boolean isJdbc(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return false;
        }

        try {
            TenantDatabaseConfig config = gatewayTenantService
                .getTenantDatabaseConfig(tenantId)
                // Avoid blocking on event-loop / parallel workers
                .subscribeOn(Schedulers.boundedElastic())
                .block();
            return config != null && config.isJdbc();
        } catch (Exception e) {
            LOG.warn("Failed to get tenant config for {}: {}", tenantId, e.getMessage());
            return false; // Default to R2DBC on error
        }
    }

    private Mono<ConnectionFactory> handleMissingTenantR2dbc() {
        if (properties.getFallback().isEnabled()) {
            String defaultTenantId = properties.getFallback().getDefaultTenantId();
            LOG.warn("Tenant ID not found in context, using fallback tenant: {}", defaultTenantId);
            return getR2dbcConnectionFactory(defaultTenantId);
        } else {
            // No tenant present: allow upstream TenantAwareConnectionFactory to use the default
            // auto-configured ConnectionFactory instead of failing loudly.
            LOG.warn("Tenant ID not found in context; fallback disabled, using default ConnectionFactory");
            return Mono.empty();
        }
    }

    private DataSource handleMissingTenantJdbc() {
        if (properties.getFallback().isEnabled()) {
            String defaultTenantId = properties.getFallback().getDefaultTenantId();
            LOG.warn("Tenant ID not found in context, using fallback tenant: {}", defaultTenantId);
            return getJdbcDataSource(defaultTenantId);
        } else {
            LOG.error("Tenant ID not found in context and fallback is disabled");
            return null;
        }
    }
}
