package com.atparui.rmsservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.annotation.PostConstruct;

/**
 * Database configuration for Service.
 * Creates a @Primary ConnectionFactory bean that uses the correct Docker service name (rms-postgresql).
 * This ensures the health check and all R2DBC operations use the correct database connection.
 */
@Configuration
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
@EnableR2dbcRepositories(basePackages = "com.atparui.rmsservice.repository")
@EnableTransactionManagement
public class ServiceDatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(ServiceDatabaseConfig.class);

    private final R2dbcProxyFactory r2dbcProxyFactory;

    public ServiceDatabaseConfig(R2dbcProxyFactory r2dbcProxyFactory) {
        this.r2dbcProxyFactory = r2dbcProxyFactory;
    }

    @Value("${DB_HOST:rms-postgresql}")
    private String dbHost;

    @Value("${DB_PORT:5432}")
    private int dbPort;

    @Value("${DB_USERNAME:rms-service}")
    private String dbUsername;

    @Value("${DB_PASSWORD:rms-service}")
    private String dbPassword;

    @Value("${DB_NAME:rms-service}")
    private String dbName;

    @PostConstruct
    public void initializeDatabaseConnection() {
        log.info("=== ServiceDatabaseConfig Initialization ===");
        log.info("DB_HOST from @Value: {}", dbHost);
        log.info("DB_HOST from environment: {}", System.getenv("DB_HOST"));
        log.info("DB_PORT: {}", dbPort);
        log.info("DB_NAME: {}", dbName);
        log.info("DB_USERNAME: {}", dbUsername);
        log.info("Initialized database connection to: {} at {}:{}", dbName, dbHost, dbPort);
        log.info("================================================");
    }

    /**
     * Creates the ConnectionFactory bean for Service.
     * Marked @Primary only when multi-tenancy is disabled.
     * When multi-tenancy is enabled, this bean is NOT created (tenantAwareConnectionFactory will be @Primary instead).
     * Uses @ConditionalOnProperty to explicitly check multi-tenancy status.
     *
     * Note: matchIfMissing=true means if the property is not set, assume multi-tenancy is disabled.
     */
    @Bean(name = "connectionFactory")
    @Primary
    @ConditionalOnProperty(prefix = "multi-tenant", name = "enabled", havingValue = "false", matchIfMissing = true)
    public ConnectionFactory connectionFactory() {
        log.info("=== Creating PRIMARY ConnectionFactory for Service (multi-tenancy disabled) ===");
        log.info("Database: {} at {}:{}", dbName, dbHost, dbPort);
        log.info("DB_HOST from @Value: {}", dbHost);
        log.info("DB_HOST from environment: {}", System.getenv("DB_HOST"));
        log.info("DB_USERNAME: {}", dbUsername);

        if ("localhost".equals(dbHost) || "127.0.0.1".equals(dbHost)) {
            log.error("WARNING: DB_HOST is set to localhost! This will fail in Docker. Expected: rms-postgresql");
        }

        ConnectionFactory factory = new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(dbHost)
                .port(dbPort)
                .database(dbName)
                .username(dbUsername)
                .password(dbPassword)
                .build()
        );

        log.info("ConnectionFactory created successfully for host: {}", dbHost);
        log.info("==========================================");
        return r2dbcProxyFactory.wrap(factory);
    }

    /**
     * Dedicated ConnectionFactory for health checks.
     * This ensures the health indicator always uses the correct database connection
     * (rms-postgresql) instead of falling back to localhost.
     */
    @Bean(name = "healthCheckConnectionFactory")
    public ConnectionFactory healthCheckConnectionFactory() {
        log.info("=== Creating Health Check ConnectionFactory for Service ===");
        log.info("Database: {} at {}:{}", dbName, dbHost, dbPort);
        log.info("DB_HOST: {}", dbHost);

        if ("localhost".equals(dbHost) || "127.0.0.1".equals(dbHost)) {
            log.error("ERROR: DB_HOST is set to localhost! This will fail in Docker. Expected: rms-postgresql");
        }

        ConnectionFactory factory = new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(dbHost)
                .port(dbPort)
                .database(dbName)
                .username(dbUsername)
                .password(dbPassword)
                .build()
        );

        log.info("Health check ConnectionFactory created successfully for host: {}", dbHost);
        log.info("==========================================");
        return r2dbcProxyFactory.wrap(factory);
    }

    /**
     * Creates the transaction manager for R2DBC.
     * Injects ConnectionFactory to use the @Primary bean (tenantAwareConnectionFactory when multi-tenancy is enabled,
     * or connectionFactory otherwise).
     */
    @Bean
    @Primary
    public R2dbcTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
