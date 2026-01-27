package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "payment_number")
    private String paymentNumber;

    @NotNull(message = "must not be null")
    @Column(name = "amount")
    private BigDecimal amount;

    @NotNull(message = "must not be null")
    @Column(name = "payment_date")
    private Instant paymentDate;

    @Size(max = 255)
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "payment_gateway_response")
    private String paymentGatewayResponse;

    @Size(max = 50)
    @Column(name = "status")
    private String status;

    @Size(max = 255)
    @Column(name = "processed_by")
    private String processedBy;

    @Column(name = "notes")
    private String notes;

    @Column(name = "refunded_at")
    private Instant refundedAt;

    @Size(max = 255)
    @Column(name = "refunded_by")
    private String refundedBy;

    @Column(name = "refund_reason")
    private String refundReason;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "order", "branch", "customer" }, allowSetters = true)
    private Bill bill;

    @org.springframework.data.annotation.Transient
    private PaymentMethod paymentMethod;

    @Column(name = "bill_id")
    private UUID billId;

    @Column(name = "payment_method_id")
    private UUID paymentMethodId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Payment id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return this.paymentNumber;
    }

    public Payment paymentNumber(String paymentNumber) {
        this.setPaymentNumber(paymentNumber);
        return this;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount.stripTrailingZeros() : null;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Payment transactionId(String transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentGatewayResponse() {
        return this.paymentGatewayResponse;
    }

    public Payment paymentGatewayResponse(String paymentGatewayResponse) {
        this.setPaymentGatewayResponse(paymentGatewayResponse);
        return this;
    }

    public void setPaymentGatewayResponse(String paymentGatewayResponse) {
        this.paymentGatewayResponse = paymentGatewayResponse;
    }

    public String getStatus() {
        return this.status;
    }

    public Payment status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessedBy() {
        return this.processedBy;
    }

    public Payment processedBy(String processedBy) {
        this.setProcessedBy(processedBy);
        return this;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    public String getNotes() {
        return this.notes;
    }

    public Payment notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getRefundedAt() {
        return this.refundedAt;
    }

    public Payment refundedAt(Instant refundedAt) {
        this.setRefundedAt(refundedAt);
        return this;
    }

    public void setRefundedAt(Instant refundedAt) {
        this.refundedAt = refundedAt;
    }

    public String getRefundedBy() {
        return this.refundedBy;
    }

    public Payment refundedBy(String refundedBy) {
        this.setRefundedBy(refundedBy);
        return this;
    }

    public void setRefundedBy(String refundedBy) {
        this.refundedBy = refundedBy;
    }

    public String getRefundReason() {
        return this.refundReason;
    }

    public Payment refundReason(String refundReason) {
        this.setRefundReason(refundReason);
        return this;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Payment setIsPersisted() {
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

    public Payment bill(Bill bill) {
        this.setBill(bill);
        return this;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.paymentMethodId = paymentMethod != null ? paymentMethod.getId() : null;
    }

    public Payment paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public UUID getBillId() {
        return this.billId;
    }

    public void setBillId(UUID bill) {
        this.billId = bill;
    }

    public UUID getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public void setPaymentMethodId(UUID paymentMethod) {
        this.paymentMethodId = paymentMethod;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", amount=" + getAmount() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", paymentGatewayResponse='" + getPaymentGatewayResponse() + "'" +
            ", status='" + getStatus() + "'" +
            ", processedBy='" + getProcessedBy() + "'" +
            ", notes='" + getNotes() + "'" +
            ", refundedAt='" + getRefundedAt() + "'" +
            ", refundedBy='" + getRefundedBy() + "'" +
            ", refundReason='" + getRefundReason() + "'" +
            "}";
    }
}
