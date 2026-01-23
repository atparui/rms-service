package com.atparui.rmsservice.tenant;

import com.atparui.rmsservice.tenant.domain.TenantDatabaseConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import org.slf4j.Logger;Gateway auth not configured
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Service to fetch tenant database configuration from Gateway.
 * Implements caching to reduce Gateway API calls.
 */
@Service
public class GatewayTenantService {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayTenantService.class);

    private final WebClient webClient;
    private final WebClient authClient;
    private final MultiTenantProperties properties;
    private final Cache<String, TenantDatabaseConfig> tenantConfigCache;
    private final Cache<String, TokenHolder> tokenCache;

    public GatewayTenantService(MultiTenantProperties properties) {
        this.properties = properties;

        // Build WebClient for Gateway communication
        this.webClient = WebClient.builder().baseUrl(properties.getGateway().getBaseUrl()).build();
        this.authClient = WebClient.builder().build();

        // Initialize cache with TTL
        this.tenantConfigCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(properties.getConnection().getCacheTtl()))
            .maximumSize(1000)
            .build();

        // Token cache keyed by clientId to avoid fetching tokens on every request
        this.tokenCache = Caffeine.newBuilder().maximumSize(10).build();
    }

    /**
     * Get tenant database configuration.
     * First checks cache, then fetches from Gateway if not cached.
     *
     * @param tenantId the tenant ID
     * @return Mono containing TenantDatabaseConfig
     */
    public Mono<TenantDatabaseConfig> getTenantDatabaseConfig(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return Mono.error(new IllegalArgumentException("Tenant ID cannot be null or blank"));
        }

        // Check cache first
        TenantDatabaseConfig cached = tenantConfigCache.getIfPresent(tenantId);
        if (cached != null) {
            LOG.debug("Retrieved tenant config from cache for tenant: {}", tenantId);
            return Mono.just(cached);
        }

        // Fetch from Gateway
        return fetchFromGateway(tenantId)
            .doOnNext(config -> {
                // Cache the result
                tenantConfigCache.put(tenantId, config);
                LOG.debug("Cached tenant config for tenant: {}", tenantId);
            })
            .doOnError(error -> {
                if (error instanceof WebClientResponseException) {
                    WebClientResponseException ex = (WebClientResponseException) error;
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        LOG.warn("Tenant not found in Gateway: {}", tenantId);
                    } else {
                        LOG.error("Error fetching tenant config from Gateway for tenant {}: {}", tenantId, ex.getMessage());
                    }
                } else {
                    LOG.error("Unexpected error fetching tenant config for tenant {}: {}", tenantId, error.getMessage());
                }
            });
    }

    private Mono<TenantDatabaseConfig> fetchFromGateway(String tenantId) {
        String endpoint = properties.getGateway().getTenantConfigEndpoint().replace("{tenantId}", tenantId);

        String url = properties.getGateway().getBaseUrl() + endpoint;
        LOG.debug("Fetching tenant config from Gateway: {}", url);

        return getAccessToken()
            .defaultIfEmpty("")
            .flatMap(token ->
                webClient
                    .get()
                    .uri(endpoint)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(headers -> {
                        if (!token.isBlank()) {
                            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                        }
                    })
                    .retrieve()
                    .bodyToMono(TenantDatabaseConfig.class)
                    .timeout(Duration.ofMillis(properties.getGateway().getReadTimeout()))
                    .doOnSuccess(config -> {
                        config.setTenantId(tenantId); // Ensure tenant ID is set
                        LOG.info("Successfully fetched tenant config for tenant: {}", tenantId);
                    })
                    .doOnError(error -> {
                        LOG.error("Failed to fetch tenant config from Gateway for tenant {}: {}", tenantId, error.getMessage());
                    })
            );
    }

    /**
     * Invalidate cached tenant configuration.
     * Useful when tenant configuration is updated in Gateway.
     *
     * @param tenantId the tenant ID
     */
    public void invalidateCache(String tenantId) {
        tenantConfigCache.invalidate(tenantId);
        LOG.debug("Invalidated cache for tenant: {}", tenantId);
    }

    /**
     * Clear all cached tenant configurations.
     */
    public void clearCache() {
        tenantConfigCache.invalidateAll();
        LOG.debug("Cleared all tenant config cache");
    }

    /**
     * Retrieve (and cache) an access token for Gateway calls when client credentials are configured.
     */
    private Mono<String> getAccessToken() {
        MultiTenantProperties.Gateway.Auth auth = properties.getGateway().getAuth();
        if (auth == null) {
            LOG.debug("Gateway auth block is null; calling without Authorization header");
            return Mono.empty();
        }

        boolean missingClientId = isBlank(auth.getClientId());
        boolean missingSecret = isBlank(auth.getClientSecret());
        boolean missingTokenUri = isBlank(auth.getTokenUri());

        if (missingClientId || missingSecret || missingTokenUri) {
            LOG.debug(
                "Gateway auth not configured; calling without Authorization header (clientIdPresent={}, secretPresent={}, tokenUriPresent={})",
                !missingClientId,
                !missingSecret,
                !missingTokenUri
            );
            return Mono.empty();
        }

        TokenHolder cached = tokenCache.getIfPresent(auth.getClientId());
        if (cached != null && cached.isValid()) {
            LOG.debug("Using cached access token for clientId {}", auth.getClientId());
            return Mono.just(cached.token);
        }

        LOG.debug("Fetching new access token for clientId {} via {}", auth.getClientId(), auth.getTokenUri());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", auth.getClientId());
        form.add("client_secret", auth.getClientSecret());
        if (!isBlank(auth.getScope())) {
            form.add("scope", auth.getScope());
        }

        return authClient
            .post()
            .uri(auth.getTokenUri())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(form)
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .doOnError(error ->
                LOG.error(
                    "Failed to fetch access token for clientId {} from {}: {}",
                    auth.getClientId(),
                    auth.getTokenUri(),
                    error.getMessage()
                )
            )
            .map(response -> {
                long expiresIn = response.getExpiresIn() != null ? response.getExpiresIn() : 300L;
                // Buffer expiry by 30 seconds to avoid using an about-to-expire token
                long validUntilEpoch = Instant.now().plusSeconds(Math.max(30, expiresIn - 30)).getEpochSecond();
                TokenHolder holder = new TokenHolder(response.getAccessToken(), validUntilEpoch);
                tokenCache.put(auth.getClientId(), holder);
                LOG.debug("Fetched and cached access token for clientId {} (expires in ~{}s)", auth.getClientId(), expiresIn);
                return holder.token;
            });
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static class TokenHolder {
        private final String token;
        private final long validUntilEpochSeconds;

        TokenHolder(String token, long validUntilEpochSeconds) {
            this.token = token;
            this.validUntilEpochSeconds = validUntilEpochSeconds;
        }

        boolean isValid() {
            return !Objects.isNull(token) && Instant.now().getEpochSecond() < validUntilEpochSeconds;
        }
    }

    private static class TokenResponse {
        private String access_token;
        private Long expires_in;

        public String getAccessToken() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public Long getExpiresIn() {
            return expires_in;
        }

        public void setExpires_in(Long expires_in) {
            this.expires_in = expires_in;
        }
    }
}
