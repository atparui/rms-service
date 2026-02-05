package com.atparui.rmsservice.security;

import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.domain.UserSyncLog;
import com.atparui.rmsservice.repository.RmsUserRepository;
import com.atparui.rmsservice.repository.UserSyncLogRepository;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
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
 * Provision users in tenant-specific RMS database on successful authentication.
 *
 * Implementation:
 * - Creates/updates user in rms_user table (tenant-specific database)
 * - No dependency on JHipster jhi_user table
 * - Roles managed via user_branch_role (branch-tied roles pattern)
 * - Logs sync activity in user_sync_log table
 */
@Component
public class UserProvisioningAuthSuccessListener {

    private static final Logger LOG = LoggerFactory.getLogger(UserProvisioningAuthSuccessListener.class);

    private final ObjectProvider<RmsUserRepository> rmsUserRepositoryProvider;
    private final ObjectProvider<UserSyncLogRepository> userSyncLogRepositoryProvider;

    public UserProvisioningAuthSuccessListener(
        ObjectProvider<RmsUserRepository> rmsUserRepositoryProvider,
        ObjectProvider<UserSyncLogRepository> userSyncLogRepositoryProvider
    ) {
        this.rmsUserRepositoryProvider = rmsUserRepositoryProvider;
        this.userSyncLogRepositoryProvider = userSyncLogRepositoryProvider;
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
        String tenantId = stringClaim(claims, "tenant_id", null);

        if (externalUserId == null || username == null) {
            LOG.debug("Skipping provisioning: missing required claims (sub/preferred_username)");
            return;
        }

        RmsUserRepository rmsRepo = rmsUserRepositoryProvider.getIfAvailable();
        if (rmsRepo == null) {
            LOG.warn("RmsUserRepository not available; cannot provision rms_user");
            return;
        }

        try {
            // Find or create RMS user in tenant database
            Optional<RmsUser> existingOpt = rmsRepo.findByExternalUserId(externalUserId);
            RmsUser rmsUser;
            boolean isNewUser = false;

            if (existingOpt.isPresent()) {
                // Update existing user
                rmsUser = existingOpt.get();
                rmsUser.setUsername(username);
                rmsUser.setEmail(email);
                rmsUser.setFirstName(firstName);
                rmsUser.setLastName(lastName);
                rmsUser.setDisplayName(buildDisplayName(firstName, lastName, username));
                rmsUser.setProfileImageUrl(imageUrl);
                rmsUser.setLastSyncAt(Instant.now());
                rmsUser.setSyncStatus("SYNCED");
                LOG.debug("Updating existing rms_user: {}", username);
            } else {
                // Create new user
                rmsUser = new RmsUser();
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
                rmsUser.setCreatedAt(Instant.now());
                rmsUser.setCreatedBy(username);
                isNewUser = true;
                LOG.info("Creating new rms_user: externalUserId={}, username={}, tenantId={}", 
                    externalUserId, username, tenantId);
            }

            rmsUser = rmsRepo.save(rmsUser);
            
            // Log sync activity
            logSyncActivity(rmsUser.getId(), externalUserId, username, isNewUser, claims);
            
            LOG.info("Provisioned rms_user in tenant database: externalUserId={}, username={}, isNew={}", 
                externalUserId, username, isNewUser);

        } catch (Exception ex) {
            LOG.error("Failed to provision rms_user for {}: {}", username, ex.getMessage(), ex);
        }
    }

    private void logSyncActivity(UUID userId, String externalUserId, String username, boolean isNewUser, Map<String, Object> claims) {
        UserSyncLogRepository syncLogRepo = userSyncLogRepositoryProvider.getIfAvailable();
        if (syncLogRepo == null) {
            return;
        }

        try {
            UserSyncLog syncLog = new UserSyncLog();
            syncLog.setId(UUID.randomUUID());
            syncLog.setUserId(userId);
            syncLog.setExternalUserId(externalUserId);
            syncLog.setSyncStatus("SUCCESS");
            syncLog.setSyncType(isNewUser ? "CREATE" : "UPDATE");
            syncLog.setSyncedAt(Instant.now());
            syncLog.setSourceSystem("KEYCLOAK");
            
            // Extract roles from JWT
            List<String> roles = extractRoles(claims);
            if (!roles.isEmpty()) {
                syncLog.setSyncDetails("Roles from JWT: " + String.join(", ", roles));
            }
            
            syncLogRepo.save(syncLog);
        } catch (Exception ex) {
            LOG.warn("Failed to create sync log for {}: {}", username, ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRoles(Map<String, Object> claims) {
        // Try to extract roles from various JWT claim structures
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof Collection) {
            return ((Collection<?>) rolesObj).stream()
                .map(Object::toString)
                .filter(s -> s.startsWith("ROLE_"))
                .toList();
        }
        
        Object realmAccess = claims.get("realm_access");
        if (realmAccess instanceof Map) {
            Object realmRoles = ((Map<?, ?>) realmAccess).get("roles");
            if (realmRoles instanceof Collection) {
                return ((Collection<?>) realmRoles).stream()
                    .map(Object::toString)
                    .toList();
            }
        }
        
        return List.of();
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
