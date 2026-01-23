package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Link between a menu and required permissions.
 */
@Table("menu_permission")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "menupermission")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuPermission implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("app_menu_id")
    private UUID appMenuId;

    @NotNull(message = "must not be null")
    @Column("permission_id")
    private UUID permissionId;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public MenuPermission id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAppMenuId() {
        return this.appMenuId;
    }

    public void setAppMenuId(UUID appMenuId) {
        this.appMenuId = appMenuId;
    }

    public MenuPermission appMenuId(UUID appMenuId) {
        this.setAppMenuId(appMenuId);
        return this;
    }

    public UUID getPermissionId() {
        return this.permissionId;
    }

    public void setPermissionId(UUID permissionId) {
        this.permissionId = permissionId;
    }

    public MenuPermission permissionId(UUID permissionId) {
        this.setPermissionId(permissionId);
        return this;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public MenuPermission setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuPermission)) {
            return false;
        }
        return getId() != null && getId().equals(((MenuPermission) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuPermission{" +
            "id=" + getId() +
            ", appMenuId='" + getAppMenuId() + "'" +
            ", permissionId='" + getPermissionId() + "'" +
            "}";
    }
}
