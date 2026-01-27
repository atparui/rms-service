package com.atparui.rmsservice.config;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception handling async task executor.
 */
public class ExceptionHandlingAsyncTaskExecutor implements Executor {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingAsyncTaskExecutor.class);

    private final Executor executor;

    public ExceptionHandlingAsyncTaskExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(createWrappedRunnable(task));
    }

    private Runnable createWrappedRunnable(final Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception ex) {
                handle(ex);
            }
        };
    }

    protected void handle(Exception ex) {
        LOG.error("Caught async exception", ex);
    }
}
