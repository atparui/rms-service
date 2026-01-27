package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A AppNavigationMenuRole.
 */
@Entity
@Table(name = "app_navigation_menu_role")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppNavigationMenuRole implements Serializable, Persistable<UUID> {

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

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    private AppNavigationMenu appNavigationMenu;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "parentMenu" }, allowSetters = true)
    private AppNavigationMenuItem appNavigationMenuItem;

    @Column(name = "app_navigation_menu_id")
    private UUID appNavigationMenuId;

    @Column(name = "app_navigation_menu_item_id")
    private UUID appNavigationMenuItemId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AppNavigationMenuRole id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return this.role;
    }

    public AppNavigationMenuRole role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppNavigationMenuRole isActive(Boolean isActive) {
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

    public AppNavigationMenuRole setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public AppNavigationMenu getAppNavigationMenu() {
        return this.appNavigationMenu;
    }

    public void setAppNavigationMenu(AppNavigationMenu appNavigationMenu) {
        this.appNavigationMenu = appNavigationMenu;
        this.appNavigationMenuId = appNavigationMenu != null ? appNavigationMenu.getId() : null;
    }

    public AppNavigationMenuRole appNavigationMenu(AppNavigationMenu appNavigationMenu) {
        this.setAppNavigationMenu(appNavigationMenu);
        return this;
    }

    public AppNavigationMenuItem getAppNavigationMenuItem() {
        return this.appNavigationMenuItem;
    }

    public void setAppNavigationMenuItem(AppNavigationMenuItem appNavigationMenuItem) {
        this.appNavigationMenuItem = appNavigationMenuItem;
        this.appNavigationMenuItemId = appNavigationMenuItem != null ? appNavigationMenuItem.getId() : null;
    }

    public AppNavigationMenuRole appNavigationMenuItem(AppNavigationMenuItem appNavigationMenuItem) {
        this.setAppNavigationMenuItem(appNavigationMenuItem);
        return this;
    }

    public UUID getAppNavigationMenuId() {
        return this.appNavigationMenuId;
    }

    public void setAppNavigationMenuId(UUID appNavigationMenu) {
        this.appNavigationMenuId = appNavigationMenu;
    }

    public UUID getAppNavigationMenuItemId() {
        return this.appNavigationMenuItemId;
    }

    public void setAppNavigationMenuItemId(UUID appNavigationMenuItem) {
        this.appNavigationMenuItemId = appNavigationMenuItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppNavigationMenuRole)) {
            return false;
        }
        return getId() != null && getId().equals(((AppNavigationMenuRole) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppNavigationMenuRole{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
