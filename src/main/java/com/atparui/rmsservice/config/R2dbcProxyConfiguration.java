package com.atparui.rmsservice.config;

import io.r2dbc.proxy.listener.ProxyExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class R2dbcProxyConfiguration {

    @Bean
    public ProxyExecutionListener r2dbcProxyExecutionListener() {
        return new R2dbcProxyQueryLoggingListener();
    }
}
