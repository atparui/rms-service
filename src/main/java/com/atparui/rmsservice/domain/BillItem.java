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
 * A BillItem.
 */
@Entity
@Table(name = "bill_item")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillItem implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column(name = "item_name")
    private String itemName;

    @NotNull(message = "must not be null")
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull(message = "must not be null")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @NotNull(message = "must not be null")
    @Column(name = "item_total")
    private BigDecimal itemTotal;

    @Transient
    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "order", "branch", "customer" }, allowSetters = true)
    private Bill bill;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "order", "menuItem", "menuItemVariant" }, allowSetters = true)
    private OrderItem orderItem;

    @Column(name = "bill_id")
    private UUID billId;

    @Column(name = "order_item_id")
    private UUID orderItemId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public BillItem id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getItemName() {
        return this.itemName;
    }

    public BillItem itemName(String itemName) {
        this.setItemName(itemName);
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BillItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public BillItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice != null ? unitPrice.stripTrailingZeros() : null;
    }

    public BigDecimal getItemTotal() {
        return this.itemTotal;
    }

    public BillItem itemTotal(BigDecimal itemTotal) {
        this.setItemTotal(itemTotal);
        return this;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal != null ? itemTotal.stripTrailingZeros() : null;
    }

    @Transient
    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public BillItem setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Bill getBill() {
        return this.bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
        this.billId = bill != null ? bill.getId() : null;
    }

    public BillItem bill(Bill bill) {
        this.setBill(bill);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        this.orderItemId = orderItem != null ? orderItem.getId() : null;
    }

    public BillItem orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public UUID getBillId() {
        return this.billId;
    }

    public void setBillId(UUID bill) {
        this.billId = bill;
    }

    public UUID getOrderItemId() {
        return this.orderItemId;
    }

    public void setOrderItemId(UUID orderItem) {
        this.orderItemId = orderItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillItem)) {
            return false;
        }
        return getId() != null && getId().equals(((BillItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillItem{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", itemTotal=" + getItemTotal() +
            "}";
    }
}
