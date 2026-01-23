package com.atparui.rmsservice.service.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.atparui.rmsservice.domain.MenuPermission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuPermissionDTO implements Serializable {

    private UUID id;
    private UUID appMenuId;
    private UUID permissionId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAppMenuId() {
        return appMenuId;
    }

    public void setAppMenuId(UUID appMenuId) {
        this.appMenuId = appMenuId;
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
        if (!(o instanceof MenuPermissionDTO)) {
            return false;
        }
        MenuPermissionDTO that = (MenuPermissionDTO) o;
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
        return "MenuPermissionDTO{" +
            "id='" + getId() + "'" +
            ", appMenuId='" + getAppMenuId() + "'" +
            ", permissionId='" + getPermissionId() + "'" +
            "}";
    }
}
