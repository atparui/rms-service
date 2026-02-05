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
 * A UserBranchRole.
 */
@Entity
@Table(name = "user_branch_role")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserBranchRole implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "role")
    private String role;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull(message = "must not be null")
    @Column(name = "assigned_at")
    private Instant assignedAt;

    @Size(max = 255)
    @Column(name = "assigned_by")
    private String assignedBy;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Size(max = 255)
    @Column(name = "revoked_by")
    private String revokedBy;

    @Transient
    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @Transient
    @org.springframework.data.annotation.Transient
    private RmsUser user;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private Branch branch;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "branch_id")
    private UUID branchId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public UserBranchRole id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return this.role;
    }

    public UserBranchRole role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public UserBranchRole isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getAssignedAt() {
        return this.assignedAt;
    }

    public UserBranchRole assignedAt(Instant assignedAt) {
        this.setAssignedAt(assignedAt);
        return this;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getAssignedBy() {
        return this.assignedBy;
    }

    public UserBranchRole assignedBy(String assignedBy) {
        this.setAssignedBy(assignedBy);
        return this;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Instant getRevokedAt() {
        return this.revokedAt;
    }

    public UserBranchRole revokedAt(Instant revokedAt) {
        this.setRevokedAt(revokedAt);
        return this;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevokedBy() {
        return this.revokedBy;
    }

    public UserBranchRole revokedBy(String revokedBy) {
        this.setRevokedBy(revokedBy);
        return this;
    }

    public void setRevokedBy(String revokedBy) {
        this.revokedBy = revokedBy;
    }

    @Transient
    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UserBranchRole setIsPersisted() {
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

    public UserBranchRole user(RmsUser rmsUser) {
        this.setUser(rmsUser);
        return this;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
        this.branchId = branch != null ? branch.getId() : null;
    }

    public UserBranchRole branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID rmsUser) {
        this.userId = rmsUser;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branch) {
        this.branchId = branch;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserBranchRole)) {
            return false;
        }
        return getId() != null && getId().equals(((UserBranchRole) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserBranchRole{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", assignedAt='" + getAssignedAt() + "'" +
            ", assignedBy='" + getAssignedBy() + "'" +
            ", revokedAt='" + getRevokedAt() + "'" +
            ", revokedBy='" + getRevokedBy() + "'" +
            "}";
    }
}
