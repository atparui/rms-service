package com.atparui.rmsservice.config;

import com.atparui.rmsservice.security.SecurityUtils;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 * Used by JPA Auditing and JaVers to track who made changes.
 * 
 * Note: This is a synchronous implementation. For reactive contexts,
 * the SecurityUtils.getCurrentUserLogin() will block to get the user.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityUtils.getCurrentUserLogin();
    }
}
