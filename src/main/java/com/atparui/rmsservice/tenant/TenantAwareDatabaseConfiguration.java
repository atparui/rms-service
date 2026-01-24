package com.atparui.rmsservice.tenant;

import com.atparui.rmsservice.config.R2dbcProxyFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for tenant-aware database connections.
 * Replaces the default ConnectionFactory with a tenant-aware one when multi-tenancy is enabled.
 * The default ConnectionFactory is auto-configured by Spring Boot's R2dbcAutoConfiguration.
 */
@Configuration
@ConditionalOnProperty(prefix = "multi-tenant", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean(ConnectionFactory.class)
@AutoConfigureAfter(R2dbcAutoConfiguration.class)
public class TenantAwareDatabaseConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(TenantAwareDatabaseConfiguration.class);

    private final TenantConnectionFactoryProvider connectionFactoryProvider;
    private final BeanFactory beanFactory;
    private final R2dbcProxyFactory r2dbcProxyFactory;

    /**
     * Constructor that receives BeanFactory to look up the auto-configured ConnectionFactory.
     * We use BeanFactory to avoid circular dependency issues.
     */
    public TenantAwareDatabaseConfiguration(
        TenantConnectionFactoryProvider connectionFactoryProvider,
        BeanFactory beanFactory,
        R2dbcProxyFactory r2dbcProxyFactory
    ) {
        this.connectionFactoryProvider = connectionFactoryProvider;
        this.beanFactory = beanFactory;
        this.r2dbcProxyFactory = r2dbcProxyFactory;
        LOG.info("Multi-tenant database configuration enabled");
    }

    /**
     * Provides a tenant-aware ConnectionFactory as the primary bean.
     * This will be used by all R2DBC repositories.
     * Note: We don't use @DependsOn because the auto-configured ConnectionFactory
     * bean name may vary. Instead, we handle the lookup with proper error handling.
     */
    @Bean
    @Primary
    public ConnectionFactory tenantAwareConnectionFactory() {
        LOG.info("Creating tenant-aware ConnectionFactory");

        // Get the auto-configured ConnectionFactory (not the tenant-aware one)
        ConnectionFactory defaultConnectionFactory = r2dbcProxyFactory.wrap(getDefaultConnectionFactory());

        return new TenantAwareConnectionFactory(connectionFactoryProvider, defaultConnectionFactory);
    }

    /**
     * Gets the default (auto-configured) ConnectionFactory, excluding the tenant-aware one.
     * This method looks up ConnectionFactory beans by name to find the auto-configured one.
     */
    private ConnectionFactory getDefaultConnectionFactory() {
        ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
        String[] beanNames = listableBeanFactory.getBeanNamesForType(ConnectionFactory.class);

        LOG.debug("Found {} ConnectionFactory bean(s): {}", beanNames.length, String.join(", ", beanNames));

        // Find the ConnectionFactory that's not the tenant-aware one
        // Spring Boot auto-configuration typically creates a bean with a specific name
        // or we can find it by excluding our tenant-aware bean
        for (String beanName : beanNames) {
            if (!beanName.equals("tenantAwareConnectionFactory")) {
                try {
                    ConnectionFactory factory = beanFactory.getBean(beanName, ConnectionFactory.class);
                    // Double-check it's not a TenantAwareConnectionFactory (shouldn't happen, but be safe)
                    if (!(factory instanceof TenantAwareConnectionFactory)) {
                        LOG.debug("Using default ConnectionFactory bean: {}", beanName);
                        return factory;
                    }
                } catch (Exception e) {
                    // Bean might be in creation, skip it and try next
                    LOG.debug("Skipping bean {} due to creation state: {}", beanName, e.getMessage());
                    continue;
                }
            }
        }

        // If we couldn't find a non-primary one, it means the auto-configured ConnectionFactory
        // hasn't been created yet. This can happen if there's a configuration issue.
        throw new IllegalStateException(
            "Could not find default ConnectionFactory. " +
            "Make sure R2DBC auto-configuration is enabled and the ConnectionFactory bean is created before " +
            "tenantAwareConnectionFactory. Found bean names: " +
            String.join(", ", beanNames)
        );
    }
}
