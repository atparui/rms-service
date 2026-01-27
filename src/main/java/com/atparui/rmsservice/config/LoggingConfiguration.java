package com.atparui.rmsservice.config;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/*
 * Configures the console logging
 */
@Configuration
@RefreshScope
public class LoggingConfiguration {

    public LoggingConfiguration(
        @Value("${spring.application.name}") String appName,
        @Value("${server.port}") String serverPort,
        ObjectProvider<ConsulRegistration> consulRegistration,
        ObjectProvider<BuildProperties> buildProperties,
        ObjectProvider<ObjectMapper> mapper
    ) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        Map<String, String> map = new HashMap<>();
        map.put("app_name", appName);
        map.put("app_port", serverPort);
        buildProperties.ifAvailable(it -> map.put("version", it.getVersion()));
        consulRegistration.ifAvailable(it -> map.put("instance_id", it.getInstanceId()));
        // Simplified logging configuration without JHipster dependencies
        // ObjectMapper is optional - only used if available
    }
}
