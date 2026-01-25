package com.atparui.rmsservice.tenant;

import javax.sql.DataSource;

/**
 * JDBC-only tenant connection provider used by routing DataSource.
 */
public interface TenantConnectionProvider {
    DataSource getJdbcDataSource();

    DataSource getJdbcDataSource(String tenantId);
}
