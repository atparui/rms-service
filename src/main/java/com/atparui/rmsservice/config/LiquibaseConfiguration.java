package com.atparui.rmsservice.config;

import java.util.Optional;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import liquibase.integration.spring.SpringLiquibase;
import com.atparui.rmsservice.config.ApplicationConstants;
import com.atparui.rmsservice.config.liquibase.AsyncSpringLiquibase;

@Configuration
public class LiquibaseConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    private final Environment env;

    // RMS-Service specific Liquibase properties for multi-tenant separation
    @Value("${spring.liquibase.url:jdbc:postgresql://db:5432/rms-service?currentSchema=public}")
    private String liquibaseUrl;

    @Value("${spring.liquibase.user:rms-service}")
    private String liquibaseUsername;

    @Value("${spring.liquibase.password:rms-service}")
    private String liquibasePassword;

    @Value("${spring.liquibase.enabled:true}")
    private boolean liquibaseEnabled;

    @Value("${spring.liquibase.change-log:classpath:config/liquibase/master.xml}")
    private String changeLog;

    public LiquibaseConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public SpringLiquibase liquibase(@Qualifier("taskExecutor") Executor executor) {
        SpringLiquibase liquibase = new AsyncSpringLiquibase(executor, env);
        liquibase.setDataSource(createLiquibaseDataSource());
        liquibase.setChangeLog(changeLog);
        if (env.matchesProfiles(ApplicationConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseEnabled);
            LOG.debug("Configuring Liquibase");
        }
        return liquibase;
    }

    private DataSource createLiquibaseDataSource() {
        LOG.info("Creating RMS-Service Liquibase DataSource with URL: {}, User: {}", liquibaseUrl, liquibaseUsername);

        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource();
        dataSource.setJdbcUrl(liquibaseUrl);
        dataSource.setUsername(liquibaseUsername);
        dataSource.setPassword(liquibasePassword);
        dataSource.setPoolName("rms-service-liquibase-hikari"); // Unique name for rms-service
        dataSource.setMaximumPoolSize(2); // Liquibase only needs a small pool
        dataSource.setMinimumIdle(1);
        return dataSource;
    }
}
