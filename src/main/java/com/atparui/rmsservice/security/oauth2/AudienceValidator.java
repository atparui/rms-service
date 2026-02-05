package com.atparui.rmsservice.security.oauth2;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private static final Logger LOG = LoggerFactory.getLogger(AudienceValidator.class);
    private final OAuth2Error error = new OAuth2Error("invalid_token", "The required audience is missing", null);

    private final List<String> allowedAudience;

    public AudienceValidator(List<String> allowedAudience) {
        Assert.notEmpty(allowedAudience, "Allowed audience should not be null or empty.");
        this.allowedAudience = allowedAudience;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        List<String> audience = jwt.getAudience();
        // Allow tokens without audience claim (trusted issuer)
        if (audience == null || audience.isEmpty()) {
            LOG.debug("JWT token has no audience claim, allowing token from trusted issuer");
            return OAuth2TokenValidatorResult.success();
        }
        if (audience.stream().anyMatch(allowedAudience::contains)) {
            return OAuth2TokenValidatorResult.success();
        }
        // OIDC: client ID is often in azp (authorized party); accept if azp is in allowed list (web/mobile app clients)
        String azp = jwt.getClaimAsString("azp");
        if (azp != null && allowedAudience.contains(azp)) {
            LOG.debug("JWT azp {} is in allowed list, accepting", azp);
            return OAuth2TokenValidatorResult.success();
        }
        LOG.warn("Invalid audience: {} (azp: {})", audience, azp);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
