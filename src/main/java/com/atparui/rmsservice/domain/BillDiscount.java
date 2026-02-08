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
 * A BillDiscount.
 */
@Entity
@Table(name = "bill_discount")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillDiscount implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @Size(max = 50)
    @Column(name = "discount_code")
    private String discountCode;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "discount_type")
    private String discountType;

    @NotNull(message = "must not be null")
    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @NotNull(message = "must not be null")
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Transient
    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "order", "branch", "customer" }, allowSetters = true)
    private Bill bill;

    @Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private Discount discount;

    @Column(name = "bill_id")
    private UUID billId;

    @Column(name = "discount_id")
    private UUID discountId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public BillDiscount id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDiscountCode() {
        return this.discountCode;
    }

    public BillDiscount discountCode(String discountCode) {
        this.setDiscountCode(discountCode);
        return this;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public BillDiscount discountType(String discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public BillDiscount discountValue(BigDecimal discountValue) {
        this.setDiscountValue(discountValue);
        return this;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue != null ? discountValue.stripTrailingZeros() : null;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public BillDiscount discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount != null ? discountAmount.stripTrailingZeros() : null;
    }

    @Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public BillDiscount setIsPersisted() {
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

    public BillDiscount bill(Bill bill) {
        this.setBill(bill);
        return this;
    }

    public Discount getDiscount() {
        return this.discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
        this.discountId = discount != null ? discount.getId() : null;
    }

    public BillDiscount discount(Discount discount) {
        this.setDiscount(discount);
        return this;
    }

    public UUID getBillId() {
        return this.billId;
    }

    public void setBillId(UUID bill) {
        this.billId = bill;
    }

    public UUID getDiscountId() {
        return this.discountId;
    }

    public void setDiscountId(UUID discount) {
        this.discountId = discount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillDiscount)) {
            return false;
        }
        return getId() != null && getId().equals(((BillDiscount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillDiscount{" +
            "id=" + getId() +
            ", discountCode='" + getDiscountCode() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountValue=" + getDiscountValue() +
            ", discountAmount=" + getDiscountAmount() +
            "}";
    }
}
