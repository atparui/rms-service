package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

/**
 * A UserSyncLog.
 */
@Entity
@Table(name = "user_sync_log")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSyncLog implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "sync_type")
    private String syncType;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "sync_status")
    private String syncStatus;

    @Size(max = 255)
    @Column(name = "external_user_id")
    private String externalUserId;

    @Column(name = "request_payload")
    private String requestPayload;

    @Column(name = "response_payload")
    private String responsePayload;

    @Column(name = "error_message")
    private String errorMessage;

    @NotNull(message = "must not be null")
    @Column(name = "synced_at")
    private Instant syncedAt;

    @Size(max = 255)
    @Column(name = "synced_by")
    private String syncedBy;

    @Transient
    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @Transient
    @org.springframework.data.annotation.Transient
    private RmsUser user;

    @Column(name = "user_id")
    private UUID userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public UserSyncLog id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSyncType() {
        return this.syncType;
    }

    public UserSyncLog syncType(String syncType) {
        this.setSyncType(syncType);
        return this;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getSyncStatus() {
        return this.syncStatus;
    }

    public UserSyncLog syncStatus(String syncStatus) {
        this.setSyncStatus(syncStatus);
        return this;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getExternalUserId() {
        return this.externalUserId;
    }

    public UserSyncLog externalUserId(String externalUserId) {
        this.setExternalUserId(externalUserId);
        return this;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getRequestPayload() {
        return this.requestPayload;
    }

    public UserSyncLog requestPayload(String requestPayload) {
        this.setRequestPayload(requestPayload);
        return this;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getResponsePayload() {
        return this.responsePayload;
    }

    public UserSyncLog responsePayload(String responsePayload) {
        this.setResponsePayload(responsePayload);
        return this;
    }

    public void setResponsePayload(String responsePayload) {
        this.responsePayload = responsePayload;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public UserSyncLog errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getSyncedAt() {
        return this.syncedAt;
    }

    public UserSyncLog syncedAt(Instant syncedAt) {
        this.setSyncedAt(syncedAt);
        return this;
    }

    public void setSyncedAt(Instant syncedAt) {
        this.syncedAt = syncedAt;
    }

    public String getSyncedBy() {
        return this.syncedBy;
    }

    public UserSyncLog syncedBy(String syncedBy) {
        this.setSyncedBy(syncedBy);
        return this;
    }

    public void setSyncedBy(String syncedBy) {
        this.syncedBy = syncedBy;
    }

    @Transient
    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UserSyncLog setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public RmsUser getUser() {
        return this.user;
    }

    public void setUser(RmsUser rmsUser) {
        this.user = rmsUser;
        this.userId = rmsUser != null ? rmsUser.getId() : null;
    }

    public UserSyncLog user(RmsUser rmsUser) {
        this.setUser(rmsUser);
        return this;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID rmsUser) {
        this.userId = rmsUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSyncLog)) {
            return false;
        }
        return getId() != null && getId().equals(((UserSyncLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSyncLog{" +
            "id=" + getId() +
            ", syncType='" + getSyncType() + "'" +
            ", syncStatus='" + getSyncStatus() + "'" +
            ", externalUserId='" + getExternalUserId() + "'" +
            ", requestPayload='" + getRequestPayload() + "'" +
            ", responsePayload='" + getResponsePayload() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", syncedAt='" + getSyncedAt() + "'" +
            ", syncedBy='" + getSyncedBy() + "'" +
            "}";
    }
}
