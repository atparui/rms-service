package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bill implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "bill_number")
    private String billNumber;

    @NotNull(message = "must not be null")
    @Column(name = "bill_date")
    private Instant billDate;

    @NotNull(message = "must not be null")
    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "service_charge")
    private BigDecimal serviceCharge;

    @NotNull(message = "must not be null")
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @NotNull(message = "must not be null")
    @Column(name = "amount_due")
    private BigDecimal amountDue;

    @Size(max = 50)
    @Column(name = "status")
    private String status;

    @Size(max = 255)
    @Column(name = "generated_by")
    private String generatedBy;

    @Column(name = "notes")
    private String notes;

    @Transient
    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "branch", "customer", "user", "branchTable" }, allowSetters = true)
    private Order order;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private Branch branch;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Customer customer;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "customer_id")
    private UUID customerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Bill id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBillNumber() {
        return this.billNumber;
    }

    public Bill billNumber(String billNumber) {
        this.setBillNumber(billNumber);
        return this;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Instant getBillDate() {
        return this.billDate;
    }

    public Bill billDate(Instant billDate) {
        this.setBillDate(billDate);
        return this;
    }

    public void setBillDate(Instant billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public Bill subtotal(BigDecimal subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal != null ? subtotal.stripTrailingZeros() : null;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public Bill taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount != null ? taxAmount.stripTrailingZeros() : null;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public Bill discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount != null ? discountAmount.stripTrailingZeros() : null;
    }

    public BigDecimal getServiceCharge() {
        return this.serviceCharge;
    }

    public Bill serviceCharge(BigDecimal serviceCharge) {
        this.setServiceCharge(serviceCharge);
        return this;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge != null ? serviceCharge.stripTrailingZeros() : null;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Bill totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount != null ? totalAmount.stripTrailingZeros() : null;
    }

    public BigDecimal getAmountPaid() {
        return this.amountPaid;
    }

    public Bill amountPaid(BigDecimal amountPaid) {
        this.setAmountPaid(amountPaid);
        return this;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid != null ? amountPaid.stripTrailingZeros() : null;
    }

    public BigDecimal getAmountDue() {
        return this.amountDue;
    }

    public Bill amountDue(BigDecimal amountDue) {
        this.setAmountDue(amountDue);
        return this;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue != null ? amountDue.stripTrailingZeros() : null;
    }

    public String getStatus() {
        return this.status;
    }

    public Bill status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGeneratedBy() {
        return this.generatedBy;
    }

    public Bill generatedBy(String generatedBy) {
        this.setGeneratedBy(generatedBy);
        return this;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getNotes() {
        return this.notes;
    }

    public Bill notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Transient
    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Bill setIsPersisted() {
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

    public Bill order(Order order) {
        this.setOrder(order);
        return this;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
        this.branchId = branch != null ? branch.getId() : null;
    }

    public Bill branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer != null ? customer.getId() : null;
    }

    public Bill customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public void setOrderId(UUID order) {
        this.orderId = order;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branch) {
        this.branchId = branch;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(UUID customer) {
        this.customerId = customer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bill)) {
            return false;
        }
        return getId() != null && getId().equals(((Bill) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", billNumber='" + getBillNumber() + "'" +
            ", billDate='" + getBillDate() + "'" +
            ", subtotal=" + getSubtotal() +
            ", taxAmount=" + getTaxAmount() +
            ", discountAmount=" + getDiscountAmount() +
            ", serviceCharge=" + getServiceCharge() +
            ", totalAmount=" + getTotalAmount() +
            ", amountPaid=" + getAmountPaid() +
            ", amountDue=" + getAmountDue() +
            ", status='" + getStatus() + "'" +
            ", generatedBy='" + getGeneratedBy() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
