package com.atparui.rmsservice.config;

import io.r2dbc.proxy.ProxyConnectionFactory;
import io.r2dbc.proxy.listener.ProxyExecutionListener;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * Helper to wrap ConnectionFactory instances with the R2DBC proxy for logging.
 */
@Component
public class R2dbcProxyFactory {

    private final ProxyExecutionListener proxyExecutionListener;

    public R2dbcProxyFactory(ProxyExecutionListener proxyExecutionListener) {
        this.proxyExecutionListener = proxyExecutionListener;
    }

    public ConnectionFactory wrap(ConnectionFactory delegate) {
        return ProxyConnectionFactory.builder(delegate).listener(proxyExecutionListener).build();
    }
}
