package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Application navigation menu with hierarchical structure and permission logic.
 */
@Entity
@Table(name = "app_menu")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appmenu")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column(name = "menu_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String menuKey;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column(name = "label")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String label;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String type;

    @Size(max = 500)
    @Column(name = "route_path")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String routePath;

    @Size(max = 100)
    @Column(name = "icon")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String icon;

    @Column(name = "sort_order")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer sortOrder;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column(name = "permission_logic")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String permissionLogic;

    @Size(max = 50)
    @Column(name = "app_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String appKey;

    @Column(name = "parent_id")
    private UUID parentId;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AppMenu id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMenuKey() {
        return this.menuKey;
    }

    public AppMenu menuKey(String menuKey) {
        this.setMenuKey(menuKey);
        return this;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getLabel() {
        return this.label;
    }

    public AppMenu label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return this.type;
    }

    public AppMenu type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoutePath() {
        return this.routePath;
    }

    public AppMenu routePath(String routePath) {
        this.setRoutePath(routePath);
        return this;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getIcon() {
        return this.icon;
    }

    public AppMenu icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public AppMenu sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppMenu isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getPermissionLogic() {
        return this.permissionLogic;
    }

    public AppMenu permissionLogic(String permissionLogic) {
        this.setPermissionLogic(permissionLogic);
        return this;
    }

    public void setPermissionLogic(String permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public AppMenu appKey(String appKey) {
        this.setAppKey(appKey);
        return this;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public UUID getParentId() {
        return this.parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public AppMenu parentId(UUID parentId) {
        this.setParentId(parentId);
        return this;
    }

    public AppMenu setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppMenu)) {
            return false;
        }
        return getId() != null && getId().equals(((AppMenu) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppMenu{" +
            "id=" + getId() +
            ", menuKey='" + getMenuKey() + "'" +
            ", label='" + getLabel() + "'" +
            ", type='" + getType() + "'" +
            ", routePath='" + getRoutePath() + "'" +
            ", icon='" + getIcon() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", isActive='" + getIsActive() + "'" +
            ", permissionLogic='" + getPermissionLogic() + "'" +
            ", appKey='" + getAppKey() + "'" +
            ", parentId='" + getParentId() + "'" +
            "}";
    }
}
