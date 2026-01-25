package com.atparui.rmsservice.tenant.domain;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Tenant database configuration retrieved from Gateway.
 */
public class TenantDatabaseConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("databaseUrl")
    private String databaseUrl;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("maxPoolSize")
    private Integer maxPoolSize = 20;

    @JsonProperty("connectionTimeout")
    private Integer connectionTimeout = 30000;

    @JsonProperty("validationQuery")
    private String validationQuery = "SELECT 1";

    @JsonProperty("driverType")
    private String driverType = "JDBC"; // Default to JDBC

    @JsonProperty("clients")
    private java.util.List<TenantClientConfig> clients;

    @JsonProperty("keycloakBaseUrl")
    private String keycloakBaseUrl;

    @JsonProperty("realmName")
    private String realmName;

    public TenantDatabaseConfig() {}

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType != null ? driverType.toUpperCase() : "JDBC";
    }

    /**
     * Check if this tenant uses JDBC driver.
     */
    public boolean isJdbc() {
        return "JDBC".equalsIgnoreCase(driverType);
    }

    /**
     * Check if this tenant uses R2DBC driver.
     */
    public boolean isR2dbc() {
        return "R2DBC".equalsIgnoreCase(driverType);
    }

    public java.util.List<TenantClientConfig> getClients() {
        return clients;
    }

    public void setClients(java.util.List<TenantClientConfig> clients) {
        this.clients = clients;
    }

    public String getKeycloakBaseUrl() {
        return keycloakBaseUrl;
    }

    public void setKeycloakBaseUrl(String keycloakBaseUrl) {
        this.keycloakBaseUrl = keycloakBaseUrl;
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    /**
     * Get the issuer URI for this tenant's realm.
     * Format: {keycloakBaseUrl}/realms/{realmName}
     */
    public String getIssuerUri() {
        if (keycloakBaseUrl == null || realmName == null) {
            return null;
        }
        String baseUrl = keycloakBaseUrl.endsWith("/") ? keycloakBaseUrl.substring(0, keycloakBaseUrl.length() - 1) : keycloakBaseUrl;
        return baseUrl + "/realms/" + realmName;
    }

    /**
     * Get client configuration by client type (web or mobile).
     */
    public TenantClientConfig getClientByType(String clientType) {
        if (clients == null || clientType == null) {
            return null;
        }
        return clients.stream().filter(client -> clientType.equalsIgnoreCase(client.getClientType())).findFirst().orElse(null);
    }

    /**
     * Inner class for tenant client configuration.
     */
    public static class TenantClientConfig implements Serializable {

        private static final long serialVersionUID = 1L;

        @com.fasterxml.jackson.annotation.JsonProperty("clientId")
        private String clientId;

        @com.fasterxml.jackson.annotation.JsonProperty("clientSecret")
        private String clientSecret;

        @com.fasterxml.jackson.annotation.JsonProperty("clientType")
        private String clientType;

        public TenantClientConfig() {}

        public TenantClientConfig(String clientId, String clientSecret, String clientType) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.clientType = clientType;
        }

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

        public String getClientType() {
            return clientType;
        }

        public void setClientType(String clientType) {
            this.clientType = clientType;
        }
    }

    @Override
    public String toString() {
        return (
            "TenantDatabaseConfig{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", databaseUrl='" +
            databaseUrl +
            '\'' +
            ", username='" +
            username +
            '\'' +
            ", maxPoolSize=" +
            maxPoolSize +
            ", connectionTimeout=" +
            connectionTimeout +
            ", driverType='" +
            driverType +
            '\'' +
            ", keycloakBaseUrl='" +
            keycloakBaseUrl +
            '\'' +
            ", realmName='" +
            realmName +
            '\'' +
            ", issuerUri='" +
            getIssuerUri() +
            '\'' +
            '}'
        );
    }
}
