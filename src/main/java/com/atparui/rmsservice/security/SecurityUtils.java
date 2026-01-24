package com.atparui.rmsservice.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    public static final String CLAIMS_NAMESPACE = "https://www.jhipster.tech/";

    private SecurityUtils() {}

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Mono<String> getCurrentUserLogin() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .flatMap(authentication -> Mono.justOrEmpty(extractPrincipal(authentication)));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication instanceof JwtAuthenticationToken) {
            return (String) ((JwtAuthenticationToken) authentication).getToken().getClaims().get("preferred_username");
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            Map<String, Object> attributes = ((DefaultOidcUser) authentication.getPrincipal()).getAttributes();
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
    public static Mono<Boolean> isAuthenticated() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getAuthorities)
            .map(authorities -> authorities.stream().map(GrantedAuthority::getAuthority).noneMatch(AuthoritiesConstants.ANONYMOUS::equals));
    }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    public static Mono<Boolean> hasCurrentUserAnyOfAuthorities(String... authorities) {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getAuthorities)
            .map(authorityList ->
                authorityList
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> Arrays.asList(authorities).contains(authority))
            );
    }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
    public static Mono<Boolean> hasCurrentUserNoneOfAuthorities(String... authorities) {
        return hasCurrentUserAnyOfAuthorities(authorities).map(result -> !result);
    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static Mono<Boolean> hasCurrentUserThisAuthority(String authority) {
        return hasCurrentUserAnyOfAuthorities(authority);
    }

    /**
     * Get the roles of the current user.
     *
     * @return a Flux of role strings
     */
    public static Flux<String> getCurrentUserRoles() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getAuthorities)
            .flatMapMany(authorities ->
                Flux.fromIterable(authorities).map(GrantedAuthority::getAuthority).filter(role -> role.startsWith("ROLE_"))
            );
    }

    public static List<GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
        return mapRolesToGrantedAuthorities(getRolesFromClaims(claims));
    }

    private static Collection<String> getRolesFromClaims(Map<String, Object> claims) {
        Collection<String> flat = new ArrayList<>();

        // legacy/groups
        Object groups = claims.get("groups");
        if (groups instanceof Collection<?> c) {
            c.forEach(v -> flat.add(String.valueOf(v)));
        }

        // roles at root or namespaced
        Object roles = claims.getOrDefault("roles", claims.get(CLAIMS_NAMESPACE + "roles"));
        if (roles instanceof Collection<?> c) {
            c.forEach(v -> flat.add(String.valueOf(v)));
        }

        // realm_access.roles
        Object realmAccess = claims.get("realm_access");
        if (realmAccess instanceof Map<?, ?> ra) {
            Object raRoles = ra.get("roles");
            if (raRoles instanceof Collection<?> c) {
                c.forEach(v -> flat.add(String.valueOf(v)));
            }
        }

        // resource_access.<client>.roles (collect all client roles)
        Object resourceAccess = claims.get("resource_access");
        if (resourceAccess instanceof Map<?, ?> resMap) {
            resMap.values().forEach(entry -> {
                if (entry instanceof Map<?, ?> client) {
                    Object clientRoles = client.get("roles");
                    if (clientRoles instanceof Collection<?> c) {
                        c.forEach(v -> flat.add(String.valueOf(v)));
                    }
                }
            });
        }

        return flat;
    }

    private static List<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles) {
        return roles
            .stream()
            .filter(role -> role != null && !role.isBlank())
            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
