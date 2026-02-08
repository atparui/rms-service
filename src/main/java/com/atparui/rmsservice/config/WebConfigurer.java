package com.atparui.rmsservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration of web application with Servlet APIs.
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfigurer.class);

    @Value("${application.cors.allowed-origins:http://localhost:9000,https://localhost:9000,http://localhost:9060,https://localhost:9060}")
    private String allowedOrigins;

    @Value("${application.cors.allowed-methods:*}")
    private String allowedMethods;

    @Value("${application.cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${application.cors.allow-credentials:true}")
    private boolean allowCredentials;

    // CORS is handled by the API Gateway (console)
    // Backend services behind the gateway should NOT have CORS filters
    // to avoid conflicts and because they're only called internally by the gateway
    //
    // @Bean
    // public CorsFilter corsFilter() {
    //     ...
    // }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageableHandlerMethodArgumentResolver());
        resolvers.add(new SortHandlerMethodArgumentResolver());
    }
}
