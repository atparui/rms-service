package com.atparui.rmsservice.security;

import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.repository.RmsUserRepository;
import com.atparui.rmsservice.service.UserService;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provision local users on first successful authentication.
 *
 * Constraint preserved:
 * - A row in rms_user must not exist without the corresponding jhi_user row.
 *
 * Implementation notes:
 * - No servlet Filter (avoids early JPA init + bean cycles).
 * - Uses ObjectProvider to avoid forcing repository initialization during context startup.
 */
@Component
public class UserProvisioningAuthSuccessListener {

    private static final Logger LOG = LoggerFactory.getLogger(UserProvisioningAuthSuccessListener.class);

    private final ObjectProvider<UserService> userServiceProvider;
    private final ObjectProvider<RmsUserRepository> rmsUserRepositoryProvider;

    public UserProvisioningAuthSuccessListener(
        ObjectProvider<UserService> userServiceProvider,
        ObjectProvider<RmsUserRepository> rmsUserRepositoryProvider
    ) {
        this.userServiceProvider = userServiceProvider;
        this.rmsUserRepositoryProvider = rmsUserRepositoryProvider;
    }

    @EventListener(AuthenticationSuccessEvent.class)
    @Transactional("transactionManager")
    public void onAuthSuccess(AuthenticationSuccessEvent event) {
        if (!(event.getAuthentication() instanceof AbstractAuthenticationToken authToken)) {
            return;
        }

        Map<String, Object> claims = extractClaims(authToken);
        String externalUserId = stringClaim(claims, "sub", authToken.getName());
        String username = stringClaim(claims, "preferred_username", authToken.getName());
        String email = stringClaim(claims, "email", null);
        String firstName = stringClaim(claims, "given_name", stringClaim(claims, "name", null));
        String lastName = stringClaim(claims, "family_name", null);
        String imageUrl = stringClaim(claims, "picture", null);

        if (externalUserId == null || username == null) {
            LOG.debug("Skipping provisioning: missing required claims (sub/preferred_username)");
            return;
        }

        // 1) Ensure jhi_user exists (UserService already handles find-or-create)
        UserService userService = userServiceProvider.getIfAvailable();
        if (userService == null) {
            LOG.warn("UserService not available; cannot provision jhi_user/rms_user");
            return;
        }
        try {
            userService.getUserFromAuthentication(authToken);
        } catch (Exception ex) {
            LOG.warn("Failed to provision/sync jhi_user for {}: {}", username, ex.getMessage());
            return; // preserve constraint: don't create rms_user if jhi_user failed
        }

        // 2) Ensure rms_user exists (only after jhi_user exists)
        RmsUserRepository rmsRepo = rmsUserRepositoryProvider.getIfAvailable();
        if (rmsRepo == null) {
            LOG.warn("RmsUserRepository not available; cannot provision rms_user");
            return;
        }

        Optional<RmsUser> existing = rmsRepo.findByExternalUserId(externalUserId);
        if (existing.isPresent()) {
            return;
        }

        RmsUser rmsUser = new RmsUser();
        rmsUser.setId(UUID.randomUUID());
        rmsUser.setExternalUserId(externalUserId);
        rmsUser.setUsername(username);
        rmsUser.setEmail(email);
        rmsUser.setFirstName(firstName);
        rmsUser.setLastName(lastName);
        rmsUser.setDisplayName(buildDisplayName(firstName, lastName, username));
        rmsUser.setProfileImageUrl(imageUrl);
        rmsUser.setIsActive(Boolean.TRUE);
        rmsUser.setLastSyncAt(Instant.now());
        rmsUser.setSyncStatus("SYNCED");

        try {
            rmsRepo.save(rmsUser);
            LOG.info("Provisioned rms_user externalUserId={} username={}", externalUserId, username);
        } catch (Exception ex) {
            // ignore duplicates / races
            LOG.warn("Provision rms_user skipped for {}: {}", externalUserId, ex.getMessage());
        }
    }

    private static Map<String, Object> extractClaims(AbstractAuthenticationToken authToken) {
        if (authToken instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getTokenAttributes();
        }
        if (authToken instanceof OAuth2AuthenticationToken oauthAuth) {
            return oauthAuth.getPrincipal().getAttributes();
        }
        return Map.of();
    }

    private static String stringClaim(Map<String, Object> claims, String key, String fallback) {
        Object value = claims.get(key);
        return value != null ? value.toString() : fallback;
    }

    private static String buildDisplayName(String firstName, String lastName, String username) {
        if (firstName != null && lastName != null) {
            return (firstName + " " + lastName).trim();
        }
        if (firstName != null) {
            return firstName;
        }
        return username;
    }
}

