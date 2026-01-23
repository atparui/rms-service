package com.atparui.rmsservice.tenant;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for multi-tenant support.
 */
@ConfigurationProperties(prefix = "multi-tenant")
public class MultiTenantProperties {

    private boolean enabled = true;
    private String tenantIdHeader = "X-Tenant-ID";
    private String jwtTenantClaim = "tenant_id";

    private Gateway gateway = new Gateway();
    private Connection connection = new Connection();
    private Fallback fallback = new Fallback();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTenantIdHeader() {
        return tenantIdHeader;
    }

    public void setTenantIdHeader(String tenantIdHeader) {
        this.tenantIdHeader = tenantIdHeader;
    }

    public String getJwtTenantClaim() {
        return jwtTenantClaim;
    }

    public void setJwtTenantClaim(String jwtTenantClaim) {
        this.jwtTenantClaim = jwtTenantClaim;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Fallback getFallback() {
        return fallback;
    }

    public void setFallback(Fallback fallback) {
        this.fallback = fallback;
    }

    public static class Gateway {

        private String baseUrl = "http://localhost:8080";
        private String tenantConfigEndpoint = "/api/tenants/{tenantId}/database-config";
        private int connectionTimeout = 5000;
        private int readTimeout = 10000;
        private Auth auth = new Auth();

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getTenantConfigEndpoint() {
            return tenantConfigEndpoint;
        }

        public void setTenantConfigEndpoint(String tenantConfigEndpoint) {
            this.tenantConfigEndpoint = tenantConfigEndpoint;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public Auth getAuth() {
            return auth;
        }

        public void setAuth(Auth auth) {
            this.auth = auth;
        }

        public static class Auth {

            private String clientId;
            private String clientSecret;
            private String tokenUri;
            private String scope;

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }

            public String getTokenUri() {
                return tokenUri;
            }

            public void setTokenUri(String tokenUri) {
                this.tokenUri = tokenUri;
            }

            public String getScope() {
                return scope;
            }

            public void setScope(String scope) {
                this.scope = scope;
            }
        }
    }

    public static class Connection {

        private long cacheTtl = 3600; // seconds
        private int maxPoolSize = 20;
        private int minPoolSize = 5;
        private int connectionTimeout = 30000;
        private String validationQuery = "SELECT 1";

        public long getCacheTtl() {
            return cacheTtl;
        }

        public void setCacheTtl(long cacheTtl) {
            this.cacheTtl = cacheTtl;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getMinPoolSize() {
            return minPoolSize;
        }

        public void setMinPoolSize(int minPoolSize) {
            this.minPoolSize = minPoolSize;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }
    }

    public static class Fallback {

        private boolean enabled = false;
        private String defaultTenantId = "default";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getDefaultTenantId() {
            return defaultTenantId;
        }

        public void setDefaultTenantId(String defaultTenantId) {
            this.defaultTenantId = defaultTenantId;
        }
    }
}
