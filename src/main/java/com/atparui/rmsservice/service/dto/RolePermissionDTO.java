package com.atparui.rmsservice.service.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.atparui.rmsservice.domain.RolePermission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolePermissionDTO implements Serializable {

    private UUID id;
    private String role;
    private Boolean isActive;
    private UUID permissionId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(UUID permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolePermissionDTO)) {
            return false;
        }
        RolePermissionDTO that = (RolePermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermissionDTO{" +
            "id='" + getId() + "'" +
            ", role='" + getRole() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", permissionId='" + getPermissionId() + "'" +
            "}";
    }
}
