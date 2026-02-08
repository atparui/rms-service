package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

/**
 * A AppNavigationMenu.
 */
@Entity
@Table(name = "app_navigation_menu")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppNavigationMenu implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "menu_code")
    private String menuCode;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "description")
    private String description;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "menu_type")
    private String menuType;

    @Size(max = 100)
    @Column(name = "icon")
    private String icon;

    @Size(max = 500)
    @Column(name = "route_path")
    private String routePath;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @Transient
    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AppNavigationMenu id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMenuCode() {
        return this.menuCode;
    }

    public AppNavigationMenu menuCode(String menuCode) {
        this.setMenuCode(menuCode);
        return this;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public AppNavigationMenu menuName(String menuName) {
        this.setMenuName(menuName);
        return this;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getDescription() {
        return this.description;
    }

    public AppNavigationMenu description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenuType() {
        return this.menuType;
    }

    public AppNavigationMenu menuType(String menuType) {
        this.setMenuType(menuType);
        return this;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getIcon() {
        return this.icon;
    }

    public AppNavigationMenu icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRoutePath() {
        return this.routePath;
    }

    public AppNavigationMenu routePath(String routePath) {
        this.setRoutePath(routePath);
        return this;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public AppNavigationMenu displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppNavigationMenu isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public AppNavigationMenu setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppNavigationMenu)) {
            return false;
        }
        return getId() != null && getId().equals(((AppNavigationMenu) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppNavigationMenu{" +
            "id=" + getId() +
            ", menuCode='" + getMenuCode() + "'" +
            ", menuName='" + getMenuName() + "'" +
            ", description='" + getDescription() + "'" +
            ", menuType='" + getMenuType() + "'" +
            ", icon='" + getIcon() + "'" +
            ", routePath='" + getRoutePath() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
