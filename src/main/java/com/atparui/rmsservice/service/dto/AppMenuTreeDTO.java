package com.atparui.rmsservice.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO for delivering menu tree to clients.
 */
public class AppMenuTreeDTO implements Serializable {

    private UUID id;
    private String menuKey;
    private String label;
    private String type;
    private String routePath;
    private String icon;
    private Integer sortOrder;
    private List<AppMenuTreeDTO> children = new ArrayList<>();
    private List<String> requiredPermissions = new ArrayList<>();

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

    public List<AppMenuTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<AppMenuTreeDTO> children) {
        this.children = children;
    }

    public List<String> getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(List<String> requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }
}
