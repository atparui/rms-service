package com.atparui.rmsservice.web.filter;

import com.atparui.rmsservice.security.UserProvisioningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Ensures user provisioning occurs on the first authenticated call.
 * Runs after authentication so the SecurityContext is populated.
 */
@Component
public class UserProvisioningFilter implements WebFilter {

    private static final Logger LOG = LoggerFactory.getLogger(UserProvisioningFilter.class);

    private final UserProvisioningService userProvisioningService;

    public UserProvisioningFilter(UserProvisioningService userProvisioningService) {
        this.userProvisioningService = userProvisioningService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange
            .getPrincipal()
            .cast(Authentication.class)
            .flatMap(auth -> userProvisioningService.provisionIfNeeded(auth))
            .doOnError(ex -> LOG.error("User provisioning failed: {}", ex.getMessage(), ex))
            .onErrorResume(ex -> Mono.empty())
            .then(chain.filter(exchange));
    }
}
