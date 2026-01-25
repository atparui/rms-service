package com.atparui.rmsservice.config.liquibase;

import java.util.concurrent.Executor;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

/**
 * Async Spring Liquibase configuration.
 */
public class AsyncSpringLiquibase extends SpringLiquibase {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncSpringLiquibase.class);

    private final Executor executor;
    private final Environment env;

    public AsyncSpringLiquibase(Executor executor, Environment env) {
        this.executor = executor;
        this.env = env;
    }

    @Override
    public void afterPropertiesSet() {
        // Check if Liquibase should run - SpringLiquibase doesn't have a direct method
        // We check if contexts or dataSource is null as indicators
        if (getDataSource() == null) {
            LOG.debug("Liquibase is disabled - no datasource configured");
            return;
        }

        executor.execute(() -> {
            try {
                LOG.warn("Starting Liquibase asynchronously, your database might not be ready at startup!");
                initDb();
            } catch (Exception e) {
                LOG.error("Liquibase could not start correctly, your database is NOT ready: {}", e.getMessage(), e);
            }
        });
    }

    protected void initDb() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            super.afterPropertiesSet();
        } catch (Exception e) {
            LOG.error("Error during Liquibase initialization", e);
        }
        watch.stop();
        LOG.debug("Liquibase has updated your database in {} ms", watch.getTotalTimeMillis());
    }
}
