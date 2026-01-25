package com.atparui.rmsservice.tenant;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet Filter to extract tenant ID from HTTP header (primary) or JWT claim (fallback)
 * and set it in TenantContextHolder ThreadLocal for JDBC routing.
 */
@Component
@Order(-100) // Execute early in the filter chain, before security
public class TenantContextFilter extends HttpFilter {

    private static final Logger LOG = LoggerFactory.getLogger(TenantContextFilter.class);

    private final MultiTenantProperties multiTenantProperties;

    public TenantContextFilter(MultiTenantProperties multiTenantProperties) {
        this.multiTenantProperties = multiTenantProperties;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (!multiTenantProperties.isEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        String tenantId = request.getHeader(multiTenantProperties.getTenantIdHeader());
        LOG.debug("TenantContextFilter incoming headers {}: {}", multiTenantProperties.getTenantIdHeader(), tenantId);

        if (tenantId == null || tenantId.isBlank()) {
            tenantId = extractTenantFromJwt();
            if (tenantId != null) {
                LOG.debug("Extracted tenant ID from JWT: {}", tenantId);
            }
        } else {
            LOG.debug("Extracted tenant ID from header: {}", tenantId);
        }

        String path = request.getRequestURI();
        if (isPublicEndpoint(path)) {
            chain.doFilter(request, response);
            return;
        }

        if (tenantId == null || tenantId.isBlank()) {
            LOG.warn("Tenant ID not found in request. Path: {}", path);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        try {
            TenantContextHolder.setTenantIdThreadLocal(tenantId);
            chain.doFilter(request, response);
        } finally {
            TenantContextHolder.clearTenant();
        }
    }

    private String extractTenantFromJwt() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof JwtAuthenticationToken jwtToken) {
                Object tenantClaim = jwtToken.getToken().getClaims().get(multiTenantProperties.getJwtTenantClaim());
                return tenantClaim != null ? tenantClaim.toString() : null;
            }
        } catch (Exception e) {
            LOG.trace("Could not extract tenant from JWT: {}", e.getMessage());
        }
        return null;
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
