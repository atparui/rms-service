package com.atparui.rmsservice.service.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.atparui.rmsservice.domain.AppMenu} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppMenuDTO implements Serializable {

    private UUID id;
    private String menuKey;
    private String label;
    private String type;
    private String routePath;
    private String icon;
    private Integer sortOrder;
    private Boolean isActive;
    private String permissionLogic;
    private String appKey;
    private UUID parentId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getPermissionLogic() {
        return permissionLogic;
    }

    public void setPermissionLogic(String permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppMenuDTO)) {
            return false;
        }
        AppMenuDTO that = (AppMenuDTO) o;
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
        return "AppMenuDTO{" +
            "id='" + getId() + "'" +
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
