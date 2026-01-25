package com.atparui.rmsservice.config;
import java.util.List;
import java.util.Optional;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

/**
 * Dynamic JWT Decoder that resolves the issuer from the token and validates against the correct Keycloak realm.
 * This allows the service to validate tokens from multiple tenant realms dynamically.
 */
@Component
@Primary
public class DynamicJwtDecoder implements JwtDecoder {

    private static final Logger log = LoggerFactory.getLogger(DynamicJwtDecoder.class);

    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
    private String defaultIssuerUri;

    private final tech.jhipster.config.JHipsterProperties jHipsterProperties;

    public DynamicJwtDecoder(tech.jhipster.config.JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    // Cache JWT decoders per issuer to avoid recreating them
    private final Cache<String, JwtDecoder> decoderCache = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build();

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Parse token to extract issuer without full validation
            String extractedIssuer = extractIssuerFromToken(token);
            final String issuer;
            if (extractedIssuer == null) {
                log.warn("Could not extract issuer from token, using default issuer");
                issuer = defaultIssuerUri;
            } else {
                issuer = extractedIssuer;
            }

            log.debug("Decoding token with issuer: {}", issuer);

            // Get or create decoder for this issuer
            final String finalIssuer = issuer;
            JwtDecoder decoder = decoderCache.get(issuer, this::createDecoderForIssuer);

            // Decode and validate token
            return decoder.decode(token);
        } catch (Exception e) {
            log.error("Error decoding token", e);
            throw new JwtException("Failed to decode token", e);
        }
    }

    /**
     * Extract issuer URI from JWT token without full validation.
     * This is a lightweight operation to determine which realm issued the token.
     */
    private String extractIssuerFromToken(String token) {
        try {
            // Decode JWT without validation to get claims
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return null;
            }

            // Decode payload (base64url)
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode claims = mapper.readTree(payload);

            if (claims.has("iss")) {
                return claims.get("iss").asText();
            }
        } catch (Exception e) {
            log.warn("Failed to extract issuer from token", e);
        }
        return null;
    }

    /**
     * Create a JWT decoder for a specific issuer (realm).
     */
    private JwtDecoder createDecoderForIssuer(String issuerUri) {
        try {
            log.info("Creating JWT decoder for issuer: {}", issuerUri);

            // Build JWK Set URI from issuer URI
            // Keycloak JWK Set URI format: {issuer-uri}/protocol/openid-connect/certs
            String jwkSetUri = issuerUri + "/protocol/openid-connect/certs";

            // Create decoder from JWK Set URI
            NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

            // Create validators
            OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);

            OAuth2TokenValidator<Jwt> tokenValidator = withIssuer;

            // Add audience validator if configured
            java.util.List<String> audiences = jHipsterProperties.getSecurity().getOauth2().getAudience();
            if (audiences != null && !audiences.isEmpty()) {
                OAuth2TokenValidator<Jwt> audienceValidator = new com.atparui.rmsservice.security.oauth2.AudienceValidator(audiences);
                tokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
            }

            jwtDecoder.setJwtValidator(tokenValidator);

            log.info("Successfully created JWT decoder for issuer: {}", issuerUri);
            return jwtDecoder;
        } catch (Exception e) {
            log.error("Failed to create JWT decoder for issuer: {}", issuerUri, e);
            throw new RuntimeException("Failed to create JWT decoder for issuer: " + issuerUri, e);
        }
    }
}
