package com.atparui.rmsservice.config;

import org.javers.spring.auditable.AuthorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * JaVers Configuration for entity auditing.
 * 
 * JaVers provides comprehensive audit trail by tracking:
 * - What changed (field-level changes)
 * - Who made the change (user)
 * - When it changed (timestamp)
 * - Complete change history with snapshots
 * 
 * The Spring Boot Starter (javers-spring-boot-starter-sql) auto-configures:
 * - Javers instance connected to the application's database
 * - SQL repository with appropriate dialect
 * - Auto-audit aspects for @JaversSpringDataAuditable repositories
 * 
 * This configuration ensures the custom JaversAuthorProvider is used.
 * Additional configuration is in application.yml.
 */
@Configuration
public class JaversConfiguration {

    private final com.atparui.rmsservice.config.audit.JaversAuthorProvider javersAuthorProvider;

    public JaversConfiguration(com.atparui.rmsservice.config.audit.JaversAuthorProvider javersAuthorProvider) {
        this.javersAuthorProvider = javersAuthorProvider;
    }

    /**
     * Make the custom JaversAuthorProvider the primary bean.
     * This ensures it's used instead of the default SpringSecurityAuthorProvider.
     */
    @Bean
    @Primary
    public AuthorProvider authorProvider() {
        return javersAuthorProvider;
    }
}
