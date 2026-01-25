package com.atparui.rmsservice.config;

import com.atparui.rmsservice.tenant.MultiTenantProperties;
import com.atparui.rmsservice.tenant.TenantContextHolder;
import com.atparui.rmsservice.tenant.TenantJdbcConnectionManager;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ConditionalOnProperty(prefix = "multi-tenant", name = "enabled", havingValue = "true", matchIfMissing = false)
public class JdbcRoutingDataSourceConfig {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcRoutingDataSourceConfig.class);

    private final TenantJdbcConnectionManager tenantJdbcConnectionManager;
    private final MultiTenantProperties properties;
    private final DataSourceProperties dataSourceProperties;

    public JdbcRoutingDataSourceConfig(
        TenantJdbcConnectionManager tenantJdbcConnectionManager,
        MultiTenantProperties properties,
        DataSourceProperties dataSourceProperties
    ) {
        this.tenantJdbcConnectionManager = tenantJdbcConnectionManager;
        this.properties = properties;
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    @Primary
    public DataSource dataSource(ObjectProvider<DataSource> defaultDataSourceProvider) {
        DataSource defaultDs = defaultDataSourceProvider.getIfAvailable(this::buildDefaultDataSource);

        AbstractRoutingDataSource routing = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return TenantContextHolder.getCurrentTenantId().blockOptional().orElse(properties.getFallback().getDefaultTenantId());
            }

            @Override
            protected DataSource determineTargetDataSource() {
                String tenantId = (String) determineCurrentLookupKey();
                try {
                    DataSource ds = tenantJdbcConnectionManager.getDataSource(tenantId);
                    LOG.debug("[TENANT-DS] Using DataSource for tenant {}", tenantId);
                    return ds;
                } catch (Exception e) {
                    LOG.warn("[TENANT-DS] Falling back to default DataSource for tenant {} due to {}", tenantId, e.getMessage());
                    return defaultDs;
                }
            }
        };
        // Spring requires a map even though we override determineTargetDataSource
        Map<Object, Object> targets = new HashMap<>();
        targets.put("default", defaultDs);
        routing.setTargetDataSources(targets);
        routing.setDefaultTargetDataSource(defaultDs);
        routing.afterPropertiesSet();
        return routing;
    }

    private DataSource buildDefaultDataSource() {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.atparui.rmsservice");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }

    @Bean
    public PlatformTransactionManager jdbcTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
