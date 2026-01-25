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
        // Build JDBC URL from properties
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort, dbName);

        LOG.info("Creating Liquibase DataSource: {}@{}:{}/{}", dbUsername, dbHost, dbPort, dbName);

        return DataSourceBuilder.create().url(jdbcUrl).username(dbUsername).password(dbPassword).build();
    }
}
