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
 * A OrderItemCustomization.
 */
@Entity
@Table(name = "order_item_customization")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItemCustomization implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "quantity")
    private Integer quantity;

    @NotNull(message = "must not be null")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @NotNull(message = "must not be null")
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Transient
    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "order", "menuItem", "menuItemVariant" }, allowSetters = true)
    private OrderItem orderItem;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "menuItem" }, allowSetters = true)
    private MenuItemAddon menuItemAddon;

    @Column(name = "order_item_id")
    private UUID orderItemId;

    @Column(name = "menu_item_addon_id")
    private UUID menuItemAddonId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public OrderItemCustomization id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderItemCustomization quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public OrderItemCustomization unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice != null ? unitPrice.stripTrailingZeros() : null;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderItemCustomization totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice != null ? totalPrice.stripTrailingZeros() : null;
    }

    @Transient
    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public OrderItemCustomization setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        this.orderItemId = orderItem != null ? orderItem.getId() : null;
    }

    public OrderItemCustomization orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public MenuItemAddon getMenuItemAddon() {
        return this.menuItemAddon;
    }

    public void setMenuItemAddon(MenuItemAddon menuItemAddon) {
        this.menuItemAddon = menuItemAddon;
        this.menuItemAddonId = menuItemAddon != null ? menuItemAddon.getId() : null;
    }

    public OrderItemCustomization menuItemAddon(MenuItemAddon menuItemAddon) {
        this.setMenuItemAddon(menuItemAddon);
        return this;
    }

    public UUID getOrderItemId() {
        return this.orderItemId;
    }

    public void setOrderItemId(UUID orderItem) {
        this.orderItemId = orderItem;
    }

    public UUID getMenuItemAddonId() {
        return this.menuItemAddonId;
    }

    public void setMenuItemAddonId(UUID menuItemAddon) {
        this.menuItemAddonId = menuItemAddon;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemCustomization)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderItemCustomization) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemCustomization{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
