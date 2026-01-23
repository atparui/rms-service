package com.atparui.rmsservice.tenant;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Provides the appropriate ConnectionFactory based on the current tenant context.
 * Now uses TenantConnectionProvider to support both R2DBC and JDBC tenants.
 */
@Component
public class TenantConnectionFactoryProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TenantConnectionFactoryProvider.class);

    private final TenantConnectionProvider connectionProvider;
    private final MultiTenantProperties properties;

    public TenantConnectionFactoryProvider(TenantConnectionProvider connectionProvider, MultiTenantProperties properties) {
        this.connectionProvider = connectionProvider;
        this.properties = properties;
    }

    /**
     * Get ConnectionFactory for the current tenant from Reactor Context.
     * Returns empty if tenant uses JDBC (not R2DBC).
     *
     * @return Mono containing ConnectionFactory, or empty if tenant uses JDBC
     */
    public Mono<ConnectionFactory> getConnectionFactory() {
        return connectionProvider.getR2dbcConnectionFactory();
    }

    /**
     * Get ConnectionFactory for a specific tenant ID.
     * Returns empty if tenant uses JDBC (not R2DBC).
     *
     * @param tenantId the tenant ID
     * @return Mono containing ConnectionFactory, or empty if tenant uses JDBC
     */
    public Mono<ConnectionFactory> getConnectionFactory(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return handleMissingTenant();
        }
        return connectionProvider.getR2dbcConnectionFactory(tenantId);
    }

    private Mono<ConnectionFactory> handleMissingTenant() {
        if (properties.getFallback().isEnabled()) {
            String defaultTenantId = properties.getFallback().getDefaultTenantId();
            LOG.warn("Tenant ID not found in context, using fallback tenant: {}", defaultTenantId);
            return connectionProvider.getR2dbcConnectionFactory(defaultTenantId);
        } else {
            // Let TenantAwareConnectionFactory fall back to the default (boot auto-configured) factory
            LOG.warn("Tenant ID not found in context; fallback disabled, using default ConnectionFactory");
            return Mono.empty();
        }
    }
}
