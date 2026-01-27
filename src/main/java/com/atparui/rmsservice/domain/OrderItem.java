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
import org.springframework.data.domain.Persistable;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItem implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull(message = "must not be null")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @NotNull(message = "must not be null")
    @Column(name = "item_total")
    private BigDecimal itemTotal;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Size(max = 50)
    @Column(name = "status")
    private String status;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "branch", "customer", "user", "branchTable" }, allowSetters = true)
    private Order order;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "branch", "menuCategory" }, allowSetters = true)
    private MenuItem menuItem;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "menuItem" }, allowSetters = true)
    private MenuItemVariant menuItemVariant;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "menu_item_id")
    private UUID menuItemId;

    @Column(name = "menu_item_variant_id")
    private UUID menuItemVariantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public OrderItem id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public OrderItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice != null ? unitPrice.stripTrailingZeros() : null;
    }

    public BigDecimal getItemTotal() {
        return this.itemTotal;
    }

    public OrderItem itemTotal(BigDecimal itemTotal) {
        this.setItemTotal(itemTotal);
        return this;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal != null ? itemTotal.stripTrailingZeros() : null;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public OrderItem specialInstructions(String specialInstructions) {
        this.setSpecialInstructions(specialInstructions);
        return this;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getStatus() {
        return this.status;
    }

    public OrderItem status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public OrderItem setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.orderId = order != null ? order.getId() : null;
    }

    public OrderItem order(Order order) {
        this.setOrder(order);
        return this;
    }

    public MenuItem getMenuItem() {
        return this.menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        this.menuItemId = menuItem != null ? menuItem.getId() : null;
    }

    public OrderItem menuItem(MenuItem menuItem) {
        this.setMenuItem(menuItem);
        return this;
    }

    public MenuItemVariant getMenuItemVariant() {
        return this.menuItemVariant;
    }

    public void setMenuItemVariant(MenuItemVariant menuItemVariant) {
        this.menuItemVariant = menuItemVariant;
        this.menuItemVariantId = menuItemVariant != null ? menuItemVariant.getId() : null;
    }

    public OrderItem menuItemVariant(MenuItemVariant menuItemVariant) {
        this.setMenuItemVariant(menuItemVariant);
        return this;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public void setOrderId(UUID order) {
        this.orderId = order;
    }

    public UUID getMenuItemId() {
        return this.menuItemId;
    }

    public void setMenuItemId(UUID menuItem) {
        this.menuItemId = menuItem;
    }

    public UUID getMenuItemVariantId() {
        return this.menuItemVariantId;
    }

    public void setMenuItemVariantId(UUID menuItemVariant) {
        this.menuItemVariantId = menuItemVariant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", itemTotal=" + getItemTotal() +
            ", specialInstructions='" + getSpecialInstructions() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
