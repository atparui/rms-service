package com.atparui.rmsservice.tenant;

import javax.sql.DataSource;
import org.springframework.stereotype.Component;

/**
 * JDBC tenant connection provider backed by TenantJdbcConnectionManager.
 */
@Component
public class TenantConnectionProviderImpl implements TenantConnectionProvider {

    private final TenantJdbcConnectionManager jdbcConnectionManager;
    private final MultiTenantProperties properties;

    public TenantConnectionProviderImpl(TenantJdbcConnectionManager jdbcConnectionManager, MultiTenantProperties properties) {
        this.jdbcConnectionManager = jdbcConnectionManager;
        this.properties = properties;
    }

    @Override
    public DataSource getJdbcDataSource() {
        String tenantId = TenantContextHolder.getCurrentTenantId().blockOptional().orElse(null);
        if (tenantId == null || tenantId.isBlank()) {
            if (properties.getFallback().isEnabled()) {
                tenantId = properties.getFallback().getDefaultTenantId();
            }
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalStateException("Tenant ID not available for JDBC DataSource resolution");
        }
        return jdbcConnectionManager.getDataSource(tenantId);
    }

    @Override
    public DataSource getJdbcDataSource(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or blank");
        }
        return jdbcConnectionManager.getDataSource(tenantId);
    }
}
