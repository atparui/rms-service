package com.atparui.rmsservice.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Utility class for Spring Security (imperative/JDBC).
 */
public final class SecurityUtils {

    public static final String CLAIMS_NAMESPACE = "https://www.jhipster.tech/";

    private SecurityUtils() {}

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(extractPrincipal(authentication));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return (String) jwtToken.getToken().getClaims().get("preferred_username");
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            Map<String, Object> attributes = oidcUser.getAttributes();
            if (attributes.containsKey("preferred_username")) {
                return (String) attributes.get("preferred_username");
            }
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> Arrays.asList(authorities).contains(authority));
    }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
    public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
        return !hasCurrentUserAnyOfAuthorities(authorities);
    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean hasCurrentUserThisAuthority(String authority) {
        return hasCurrentUserAnyOfAuthorities(authority);
    }

    /**
     * Get the roles of the current user.
     *
     * @return a List of role strings
     */
    public static List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return List.of();
        }
        return authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(role -> role.startsWith("ROLE_"))
            .collect(Collectors.toList());
    }

    /**
     * Get authorities with the default CLAIMS_NAMESPACE roles mapping for JWT/Keycloak.
     */
    public static List<GrantedAuthority> getAuthorities(Authentication authentication) {
        if (authentication == null) {
            return List.of();
        }
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            return new ArrayList<>(authentication.getAuthorities());
        }
        return List.of();
    }

    /**
     * Extract authorities from JWT claims (groups or roles).
     *
     * @param claims the JWT claims
     * @return a list of authorities
     */
    public static List<GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
        return extractRolesFromClaims(claims)
            .stream()
            .map(role -> (GrantedAuthority) new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static List<String> extractRolesFromClaims(Map<String, Object> claims) {
        // Try to get roles from different claim locations
        // 1. Check for "groups" claim (common in Keycloak)
        Object groups = claims.get("groups");
        if (groups instanceof List) {
            return ((List<Object>) groups).stream()
                .map(Object::toString)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
                .collect(Collectors.toList());
        }

        // 2. Check for "roles" claim
        Object roles = claims.get("roles");
        if (roles instanceof List) {
            return ((List<Object>) roles).stream()
                .map(Object::toString)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
                .collect(Collectors.toList());
        }

        // 3. Check for namespaced "groups"
        Object namespacedGroups = claims.get(CLAIMS_NAMESPACE + "groups");
        if (namespacedGroups instanceof List) {
            return ((List<Object>) namespacedGroups).stream()
                .map(Object::toString)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
                .collect(Collectors.toList());
        }

        // 4. Check for namespaced "roles"
        Object namespacedRoles = claims.get(CLAIMS_NAMESPACE + "roles");
        if (namespacedRoles instanceof List) {
            return ((List<Object>) namespacedRoles).stream()
                .map(Object::toString)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
                .collect(Collectors.toList());
        }

        // Default to ROLE_USER if no roles found
        return List.of("ROLE_USER");
    }
}
