package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A OrderStatusHistory.
 */
@Entity
@Table(name = "order_status_history")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderStatusHistory implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @Size(max = 50)
    @Column(name = "previous_status")
    private String previousStatus;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "new_status")
    private String newStatus;

    @NotNull(message = "must not be null")
    @Column(name = "changed_at")
    private Instant changedAt;

    @Size(max = 255)
    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "notes")
    private String notes;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "branch", "customer", "user", "branchTable" }, allowSetters = true)
    private Order order;

    @Column(name = "order_id")
    private UUID orderId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public OrderStatusHistory id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPreviousStatus() {
        return this.previousStatus;
    }

    public OrderStatusHistory previousStatus(String previousStatus) {
        this.setPreviousStatus(previousStatus);
        return this;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getNewStatus() {
        return this.newStatus;
    }

    public OrderStatusHistory newStatus(String newStatus) {
        this.setNewStatus(newStatus);
        return this;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public Instant getChangedAt() {
        return this.changedAt;
    }

    public OrderStatusHistory changedAt(Instant changedAt) {
        this.setChangedAt(changedAt);
        return this;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangedBy() {
        return this.changedBy;
    }

    public OrderStatusHistory changedBy(String changedBy) {
        this.setChangedBy(changedBy);
        return this;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getNotes() {
        return this.notes;
    }

    public OrderStatusHistory notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public OrderStatusHistory setIsPersisted() {
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

    public OrderStatusHistory order(Order order) {
        this.setOrder(order);
        return this;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public void setOrderId(UUID order) {
        this.orderId = order;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderStatusHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderStatusHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderStatusHistory{" +
            "id=" + getId() +
            ", previousStatus='" + getPreviousStatus() + "'" +
            ", newStatus='" + getNewStatus() + "'" +
            ", changedAt='" + getChangedAt() + "'" +
            ", changedBy='" + getChangedBy() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
