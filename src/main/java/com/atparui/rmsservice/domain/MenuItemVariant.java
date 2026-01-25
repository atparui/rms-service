package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A MenuItemVariant.
 */
@Entity
@Table(name = "menu_item_variant")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuItemVariant implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column(name = "variant_name")
    private String variantName;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "variant_code")
    private String variantCode;

    @Column(name = "price_modifier")
    private BigDecimal priceModifier;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean isActive;

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

    public MenuItemVariant id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVariantName() {
        return this.variantName;
    }

    public MenuItemVariant variantName(String variantName) {
        this.setVariantName(variantName);
        return this;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getVariantCode() {
        return this.variantCode;
    }

    public MenuItemVariant variantCode(String variantCode) {
        this.setVariantCode(variantCode);
        return this;
    }

    public void setVariantCode(String variantCode) {
        this.variantCode = variantCode;
    }

    public BigDecimal getPriceModifier() {
        return this.priceModifier;
    }

    public MenuItemVariant priceModifier(BigDecimal priceModifier) {
        this.setPriceModifier(priceModifier);
        return this;
    }

    public void setPriceModifier(BigDecimal priceModifier) {
        this.priceModifier = priceModifier != null ? priceModifier.stripTrailingZeros() : null;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public MenuItemVariant isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public MenuItemVariant displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MenuItemVariant isActive(Boolean isActive) {
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

    public MenuItemVariant setIsPersisted() {
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

    public MenuItemVariant menuItem(MenuItem menuItem) {
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
        if (!(o instanceof MenuItemVariant)) {
            return false;
        }
        return getId() != null && getId().equals(((MenuItemVariant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItemVariant{" +
            "id=" + getId() +
            ", variantName='" + getVariantName() + "'" +
            ", variantCode='" + getVariantCode() + "'" +
            ", priceModifier=" + getPriceModifier() +
            ", isDefault='" + getIsDefault() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
