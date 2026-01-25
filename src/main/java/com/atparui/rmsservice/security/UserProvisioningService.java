package com.atparui.rmsservice.security;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.repository.RmsUserRepository;
import com.atparui.rmsservice.repository.UserRepository;
import com.atparui.rmsservice.service.UserService;


/**
 * Ensures a user is provisioned locally (jhi_user + rms_user) on first authenticated call.
 * Uses token claims only; external IdP API lookup can be added later if needed.
 */
@Service
public class UserProvisioningService {

    private static final Logger LOG = LoggerFactory.getLogger(UserProvisioningService.class);

    private final RmsUserRepository rmsUserRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public UserProvisioningService(RmsUserRepository rmsUserRepository, UserRepository userRepository, UserService userService) {
        this.rmsUserRepository = rmsUserRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void provisionIfNeeded(org.springframework.security.core.Authentication authentication) {
        if (!(authentication instanceof AbstractAuthenticationToken authToken)) {
            return;
        }

        Map<String, Object> claims = extractClaims(authToken);
        String externalUserId = stringClaim(claims, "sub", authToken.getName());
        String username = stringClaim(claims, "preferred_username", authToken.getName());
        String email = stringClaim(claims, "email", null);
        String firstName = stringClaim(claims, "given_name", stringClaim(claims, "name", null));
        String lastName = stringClaim(claims, "family_name", null);
        String displayName = buildDisplayName(firstName, lastName, username);
        String imageUrl = stringClaim(claims, "picture", null);

        if (externalUserId == null || username == null) {
            LOG.debug("Skipping provisioning: missing required claims (sub/preferred_username)");
            return;
        }

        String userId = externalUserId;

        // Ensure jhi_user exists
        try {
            if (userRepository.findById(userId).isPresent()) {
                LOG.debug("jhi_user already exists by id={}, skipping insert", userId);
            } else if (userRepository.findOneByLogin(username).isPresent()) {
                LOG.debug("jhi_user already exists by login={}, skipping insert", username);
            } else {
                LOG.info("Provisioning jhi_user id={} login={} email={}", userId, username, email);
                userService.getUserFromAuthentication(authToken);
            }
        } catch (Exception ex) {
            LOG.warn("Provision jhi_user skipped due to error for {}: {}", username, ex.getMessage());
        }

        // Ensure rms_user exists
        try {
            Optional<RmsUser> existingRmsUser = rmsUserRepository.findByExternalUserId(externalUserId);
            if (existingRmsUser.isPresent()) {
                LOG.debug("RMS user already exists for externalUserId={}, skipping insert", externalUserId);
            } else {
                RmsUser rmsUser = new RmsUser();
                rmsUser.setId(UUID.randomUUID());
                rmsUser.setExternalUserId(externalUserId);
                rmsUser.setUsername(username);
                rmsUser.setEmail(email);
                rmsUser.setFirstName(firstName);
                rmsUser.setLastName(lastName);
                rmsUser.setDisplayName(displayName);
                rmsUser.setProfileImageUrl(imageUrl);
                rmsUser.setIsActive(Boolean.TRUE);
                rmsUser.setLastSyncAt(Instant.now());
                rmsUser.setSyncStatus("SYNCED");
                LOG.info(
                    "Provisioning rms_user externalUserId={} username={} email={} firstName={} lastName={}",
                    externalUserId,
                    username,
                    email,
                    firstName,
                    lastName
                );
                rmsUserRepository.save(rmsUser);
                LOG.debug("Provisioned RMS user for externalUserId={}", externalUserId);
            }
        } catch (Exception ex) {
            // Ignore duplicate errors in case of race/previous manual inserts
            LOG.warn("Provision RMS user skipped due to insert error for {}: {}", externalUserId, ex.getMessage());
        }
    }

    private static Map<String, Object> extractClaims(AbstractAuthenticationToken authToken) {
        if (authToken instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaims();
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
