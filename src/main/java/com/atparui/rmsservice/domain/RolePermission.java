package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Link between a role (authority) and a permission.
 */
@Entity
@Table(name = "role_permission")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolePermission implements Serializable {

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

    @Column(name = "permission_id")
    private UUID permissionId;

    @jakarta.persistence.Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public RolePermission id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return this.role;
    }

    public RolePermission role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public RolePermission isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UUID getPermissionId() {
        return this.permissionId;
    }

    public void setPermissionId(UUID permissionId) {
        this.permissionId = permissionId;
    }

    public RolePermission permissionId(UUID permissionId) {
        this.setPermissionId(permissionId);
        return this;
    }

    public RolePermission setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolePermission)) {
            return false;
        }
        return getId() != null && getId().equals(((RolePermission) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermission{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", permissionId='" + getPermissionId() + "'" +
            "}";
    }
}
