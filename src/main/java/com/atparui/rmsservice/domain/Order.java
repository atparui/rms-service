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
import org.springframework.data.domain.Persistable;

/**
 * A Order.
 */
@Entity
@Table(name = "orders")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "order_number")
    private String orderNumber;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "order_type")
    private String orderType;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "order_source")
    private String orderSource;

    @Size(max = 50)
    @Column(name = "status")
    private String status;

    @NotNull(message = "must not be null")
    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "estimated_ready_time")
    private Instant estimatedReadyTime;

    @Column(name = "actual_ready_time")
    private Instant actualReadyTime;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Size(max = 255)
    @Column(name = "cancelled_by")
    private String cancelledBy;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @jakarta.persistence.Transient
    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private Branch branch;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Customer customer;

    @org.springframework.data.annotation.Transient
    private RmsUser user;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "branch" }, allowSetters = true)
    private BranchTable branchTable;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "branch_table_id")
    private UUID branchTableId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Order id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public Order orderNumber(String orderNumber) {
        this.setOrderNumber(orderNumber);
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderType() {
        return this.orderType;
    }

    public Order orderType(String orderType) {
        this.setOrderType(orderType);
        return this;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderSource() {
        return this.orderSource;
    }

    public Order orderSource(String orderSource) {
        this.setOrderSource(orderSource);
        return this;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getStatus() {
        return this.status;
    }

    public Order status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public Order orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Instant getEstimatedReadyTime() {
        return this.estimatedReadyTime;
    }

    public Order estimatedReadyTime(Instant estimatedReadyTime) {
        this.setEstimatedReadyTime(estimatedReadyTime);
        return this;
    }

    public void setEstimatedReadyTime(Instant estimatedReadyTime) {
        this.estimatedReadyTime = estimatedReadyTime;
    }

    public Instant getActualReadyTime() {
        return this.actualReadyTime;
    }

    public Order actualReadyTime(Instant actualReadyTime) {
        this.setActualReadyTime(actualReadyTime);
        return this;
    }

    public void setActualReadyTime(Instant actualReadyTime) {
        this.actualReadyTime = actualReadyTime;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public Order specialInstructions(String specialInstructions) {
        this.setSpecialInstructions(specialInstructions);
        return this;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public Order subtotal(BigDecimal subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal != null ? subtotal.stripTrailingZeros() : null;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public Order taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount != null ? taxAmount.stripTrailingZeros() : null;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public Order discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount != null ? discountAmount.stripTrailingZeros() : null;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Order totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount != null ? totalAmount.stripTrailingZeros() : null;
    }

    public Boolean getIsPaid() {
        return this.isPaid;
    }

    public Order isPaid(Boolean isPaid) {
        this.setIsPaid(isPaid);
        return this;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Instant getCancelledAt() {
        return this.cancelledAt;
    }

    public Order cancelledAt(Instant cancelledAt) {
        this.setCancelledAt(cancelledAt);
        return this;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancelledBy() {
        return this.cancelledBy;
    }

    public Order cancelledBy(String cancelledBy) {
        this.setCancelledBy(cancelledBy);
        return this;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public Order cancellationReason(String cancellationReason) {
        this.setCancellationReason(cancellationReason);
        return this;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Order setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
        this.branchId = branch != null ? branch.getId() : null;
    }

    public Order branch(Branch branch) {
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

    public Order customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public RmsUser getUser() {
        return this.user;
    }

    public void setUser(RmsUser rmsUser) {
        this.user = rmsUser;
        this.userId = rmsUser != null ? rmsUser.getId() : null;
    }

    public Order user(RmsUser rmsUser) {
        this.setUser(rmsUser);
        return this;
    }

    public BranchTable getBranchTable() {
        return this.branchTable;
    }

    public void setBranchTable(BranchTable branchTable) {
        this.branchTable = branchTable;
        this.branchTableId = branchTable != null ? branchTable.getId() : null;
    }

    public Order branchTable(BranchTable branchTable) {
        this.setBranchTable(branchTable);
        return this;
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

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID rmsUser) {
        this.userId = rmsUser;
    }

    public UUID getBranchTableId() {
        return this.branchTableId;
    }

    public void setBranchTableId(UUID branchTable) {
        this.branchTableId = branchTable;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", orderType='" + getOrderType() + "'" +
            ", orderSource='" + getOrderSource() + "'" +
            ", status='" + getStatus() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", estimatedReadyTime='" + getEstimatedReadyTime() + "'" +
            ", actualReadyTime='" + getActualReadyTime() + "'" +
            ", specialInstructions='" + getSpecialInstructions() + "'" +
            ", subtotal=" + getSubtotal() +
            ", taxAmount=" + getTaxAmount() +
            ", discountAmount=" + getDiscountAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", isPaid='" + getIsPaid() + "'" +
            ", cancelledAt='" + getCancelledAt() + "'" +
            ", cancelledBy='" + getCancelledBy() + "'" +
            ", cancellationReason='" + getCancellationReason() + "'" +
            "}";
    }
}
