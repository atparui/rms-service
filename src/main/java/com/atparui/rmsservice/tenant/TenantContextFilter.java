package com.atparui.rmsservice.tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * WebFilter to extract tenant ID from HTTP header (primary) or JWT claim (fallback)
 * and set it in the reactive context for downstream processing.
 */
@Component
@Order(-100) // Execute early in the filter chain, before security
public class TenantContextFilter implements WebFilter {

    private static final Logger LOG = LoggerFactory.getLogger(TenantContextFilter.class);

    private final MultiTenantProperties multiTenantProperties;

    public TenantContextFilter(MultiTenantProperties multiTenantProperties) {
        this.multiTenantProperties = multiTenantProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!multiTenantProperties.isEnabled()) {
            return chain.filter(exchange);
        }

        // Try to get tenant ID from header first (Option 1)
        final String headerTenantId = exchange.getRequest().getHeaders().getFirst(multiTenantProperties.getTenantIdHeader());
        LOG.debug("TenantContextFilter incoming headers X-Tenant-ID: {}", headerTenantId);

        // If not found in header, try JWT claim (Option 2 - fallback)
        if (headerTenantId == null || headerTenantId.isBlank()) {
            return extractTenantFromJwt()
                .flatMap(jwtTenantId -> {
                    final String finalTenantId = (jwtTenantId != null && !jwtTenantId.isBlank()) ? jwtTenantId : null;
                    if (finalTenantId != null) {
                        LOG.debug("Extracted tenant ID from JWT: {}", finalTenantId);
                    }
                    return processRequest(exchange, chain, finalTenantId);
                })
                .switchIfEmpty(processRequest(exchange, chain, null));
        } else {
            LOG.debug("Extracted tenant ID from header: {}", headerTenantId);
            return processRequest(exchange, chain, headerTenantId);
        }
    }

    private Mono<String> extractTenantFromJwt() {
        return ReactiveSecurityContextHolder.getContext()
            .cast(org.springframework.security.core.context.SecurityContext.class)
            .map(org.springframework.security.core.context.SecurityContext::getAuthentication)
            .cast(JwtAuthenticationToken.class)
            .map(token -> {
                Object tenantClaim = token.getToken().getClaims().get(multiTenantProperties.getJwtTenantClaim());
                return tenantClaim != null ? tenantClaim.toString() : null;
            })
            .onErrorResume(e -> {
                LOG.trace("Could not extract tenant from JWT: {}", e.getMessage());
                return Mono.empty();
            });
    }

    private Mono<Void> processRequest(ServerWebExchange exchange, WebFilterChain chain, String tenantId) {
        // Skip tenant validation for public endpoints
        String path = exchange.getRequest().getPath().value();
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        // Validate tenant ID is present for protected endpoints
        if (tenantId == null || tenantId.isBlank()) {
            LOG.warn("Tenant ID not found in request. Path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        // Set tenant ID in Reactor Context
        return chain.filter(exchange).contextWrite(ctx -> ctx.put("TENANT_ID", tenantId));
    }

    private boolean isPublicEndpoint(String path) {
        return (
            path.startsWith("/actuator/health") ||
            path.startsWith("/management/health") ||
            path.startsWith("/management/info") ||
            path.startsWith("/management/jhiopenapigroups") ||
            path.startsWith("/api/authenticate") ||
            path.startsWith("/api/auth-info") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-ui")
        );
    }
}
