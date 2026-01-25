package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A AppNavigationMenuItem.
 */
@Entity
@Table(name = "app_navigation_menu_item")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppNavigationMenuItem implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "item_code")
    private String itemCode;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column(name = "item_name")
    private String itemName;

    @Column(name = "description")
    private String description;

    @Size(max = 100)
    @Column(name = "icon")
    private String icon;

    @NotNull(message = "must not be null")
    @Size(max = 500)
    @Column(name = "route_path")
    private String routePath;

    @Size(max = 500)
    @Column(name = "component_path")
    private String componentPath;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Size(max = 50)
    @Column(name = "badge_text")
    private String badgeText;

    @Size(max = 50)
    @Column(name = "badge_color")
    private String badgeColor;

    @Column(name = "is_active")
    private Boolean isActive;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    private AppNavigationMenu parentMenu;

    @Column(name = "parent_menu_id")
    private UUID parentMenuId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AppNavigationMenuItem id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public AppNavigationMenuItem itemCode(String itemCode) {
        this.setItemCode(itemCode);
        return this;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return this.itemName;
    }

    public AppNavigationMenuItem itemName(String itemName) {
        this.setItemName(itemName);
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return this.description;
    }

    public AppNavigationMenuItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return this.icon;
    }

    public AppNavigationMenuItem icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRoutePath() {
        return this.routePath;
    }

    public AppNavigationMenuItem routePath(String routePath) {
        this.setRoutePath(routePath);
        return this;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getComponentPath() {
        return this.componentPath;
    }

    public AppNavigationMenuItem componentPath(String componentPath) {
        this.setComponentPath(componentPath);
        return this;
    }

    public void setComponentPath(String componentPath) {
        this.componentPath = componentPath;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public AppNavigationMenuItem displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getBadgeText() {
        return this.badgeText;
    }

    public AppNavigationMenuItem badgeText(String badgeText) {
        this.setBadgeText(badgeText);
        return this;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
    }

    public String getBadgeColor() {
        return this.badgeColor;
    }

    public AppNavigationMenuItem badgeColor(String badgeColor) {
        this.setBadgeColor(badgeColor);
        return this;
    }

    public void setBadgeColor(String badgeColor) {
        this.badgeColor = badgeColor;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppNavigationMenuItem isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public AppNavigationMenuItem setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public AppNavigationMenu getParentMenu() {
        return this.parentMenu;
    }

    public void setParentMenu(AppNavigationMenu appNavigationMenu) {
        this.parentMenu = appNavigationMenu;
        this.parentMenuId = appNavigationMenu != null ? appNavigationMenu.getId() : null;
    }

    public AppNavigationMenuItem parentMenu(AppNavigationMenu appNavigationMenu) {
        this.setParentMenu(appNavigationMenu);
        return this;
    }

    public UUID getParentMenuId() {
        return this.parentMenuId;
    }

    public void setParentMenuId(UUID appNavigationMenu) {
        this.parentMenuId = appNavigationMenu;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppNavigationMenuItem)) {
            return false;
        }
        return getId() != null && getId().equals(((AppNavigationMenuItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppNavigationMenuItem{" +
            "id=" + getId() +
            ", itemCode='" + getItemCode() + "'" +
            ", itemName='" + getItemName() + "'" +
            ", description='" + getDescription() + "'" +
            ", icon='" + getIcon() + "'" +
            ", routePath='" + getRoutePath() + "'" +
            ", componentPath='" + getComponentPath() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", badgeText='" + getBadgeText() + "'" +
            ", badgeColor='" + getBadgeColor() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
