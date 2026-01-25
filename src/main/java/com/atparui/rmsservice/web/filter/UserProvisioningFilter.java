package com.atparui.rmsservice.web.filter;

import com.atparui.rmsservice.security.UserProvisioningService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Ensures user provisioning occurs on the first authenticated call.
 * Runs after authentication so the SecurityContext is populated.
 */
@Component
public class UserProvisioningFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(UserProvisioningFilter.class);

    private final UserProvisioningService userProvisioningService;

    public UserProvisioningFilter(UserProvisioningService userProvisioningService) {
        this.userProvisioningService = userProvisioningService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                userProvisioningService.provisionIfNeeded(authentication);
            }
        } catch (Exception ex) {
            LOG.error("User provisioning failed: {}", ex.getMessage(), ex);
        }
        chain.doFilter(request, response);
    }
}
