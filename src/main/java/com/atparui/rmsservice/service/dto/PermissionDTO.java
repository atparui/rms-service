package com.atparui.rmsservice.service.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.atparui.rmsservice.domain.Permission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionDTO implements Serializable {

    private UUID id;
    private String code;
    private String description;
    private String scope;
    private Boolean isActive;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionDTO)) {
            return false;
        }
        PermissionDTO that = (PermissionDTO) o;
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
        return "PermissionDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", scope='" + getScope() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
