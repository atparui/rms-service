package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

/**
 * A MenuItemAddon.
 */
@Entity
@Table(name = "menu_item_addon")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuItemAddon implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column(name = "addon_name")
    private String addonName;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "addon_code")
    private String addonCode;

    @NotNull(message = "must not be null")
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "is_active")
    private Boolean isActive;

    @Transient
    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "branch", "menuCategory" }, allowSetters = true)
    private MenuItem menuItem;

    @Column(name = "menu_item_id")
    private UUID menuItemId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public MenuItemAddon id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAddonName() {
        return this.addonName;
    }

    public MenuItemAddon addonName(String addonName) {
        this.setAddonName(addonName);
        return this;
    }

    public void setAddonName(String addonName) {
        this.addonName = addonName;
    }

    public String getAddonCode() {
        return this.addonCode;
    }

    public MenuItemAddon addonCode(String addonCode) {
        this.setAddonCode(addonCode);
        return this;
    }

    public void setAddonCode(String addonCode) {
        this.addonCode = addonCode;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public MenuItemAddon price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price != null ? price.stripTrailingZeros() : null;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MenuItemAddon isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Transient
    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public MenuItemAddon setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public MenuItem getMenuItem() {
        return this.menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        this.menuItemId = menuItem != null ? menuItem.getId() : null;
    }

    public MenuItemAddon menuItem(MenuItem menuItem) {
        this.setMenuItem(menuItem);
        return this;
    }

    public UUID getMenuItemId() {
        return this.menuItemId;
    }

    public void setMenuItemId(UUID menuItem) {
        this.menuItemId = menuItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItemAddon)) {
            return false;
        }
        return getId() != null && getId().equals(((MenuItemAddon) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItemAddon{" +
            "id=" + getId() +
            ", addonName='" + getAddonName() + "'" +
            ", addonCode='" + getAddonCode() + "'" +
            ", price=" + getPrice() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
