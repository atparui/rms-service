package com.atparui.rmsservice.config;

import java.util.Optional;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import liquibase.integration.spring.SpringLiquibase;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.liquibase.AsyncSpringLiquibase;

@Configuration
public class LiquibaseConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    private final Environment env;

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

    public LiquibaseConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public SpringLiquibase liquibase(@Qualifier("taskExecutor") Executor executor, LiquibaseProperties liquibaseProperties) {
        SpringLiquibase liquibase = new AsyncSpringLiquibase(executor, env);
        liquibase.setDataSource(createLiquibaseDataSource(liquibaseProperties));
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        if (!CollectionUtils.isEmpty(liquibaseProperties.getContexts())) {
            liquibase.setContexts(StringUtils.collectionToCommaDelimitedString(liquibaseProperties.getContexts()));
        }
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        if (!CollectionUtils.isEmpty(liquibaseProperties.getLabelFilter())) {
            liquibase.setLabelFilter(StringUtils.collectionToCommaDelimitedString(liquibaseProperties.getLabelFilter()));
        }
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        if (env.matchesProfiles(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            LOG.debug("Configuring Liquibase");
        }
        return liquibase;
    }

    private DataSource createLiquibaseDataSource(LiquibaseProperties liquibaseProperties) {
        // Build JDBC URL from properties
        String jdbcUrl = Optional.ofNullable(liquibaseProperties.getUrl()).orElse(
            String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort, dbName)
        );

        String user = Optional.ofNullable(liquibaseProperties.getUser()).orElse(dbUsername);
        String password = Optional.ofNullable(liquibaseProperties.getPassword()).orElse(dbPassword);

        LOG.info("Creating Liquibase DataSource: {}@{}:{}/{}", user, dbHost, dbPort, dbName);

        return DataSourceBuilder.create().url(jdbcUrl).username(user).password(password).build();
    }
}
