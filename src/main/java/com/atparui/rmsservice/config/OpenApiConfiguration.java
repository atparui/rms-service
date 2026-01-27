package com.atparui.rmsservice.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(ApplicationConstants.SPRING_PROFILE_API_DOCS)
public class OpenApiConfiguration {

    /**
     * Package containing REST controllers for API documentation.
     */
    public static final String API_REST_PACKAGE = "com.atparui.rmsservice.web.rest";

    /**
     * Creates a grouped OpenAPI configuration for RMS REST endpoints.
     * Scans all @RestController classes in com.atparui.rmsservice.web.rest package.
     */
    @Bean
    @ConditionalOnMissingBean(name = "apiFirstGroupedOpenAPI")
    public GroupedOpenApi apiFirstGroupedOpenAPI() {
        return GroupedOpenApi.builder()
            .group("openapi")
            .packagesToScan(API_REST_PACKAGE)
            .pathsToMatch("/api/**", "/management/**")
            .build();
    }
}
