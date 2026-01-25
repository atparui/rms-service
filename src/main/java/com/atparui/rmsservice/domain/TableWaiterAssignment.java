package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A TableWaiterAssignment.
 */
@Entity
@Table(name = "table_waiter_assignment")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TableWaiterAssignment implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column(name = "assignment_date")
    private LocalDate assignmentDate;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "is_active")
    private Boolean isActive;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "branchTable", "shift", "supervisor" }, allowSetters = true)
    private TableAssignment tableAssignment;

    @org.springframework.data.annotation.Transient
    private RmsUser waiter;

    @Column(name = "table_assignment_id")
    private UUID tableAssignmentId;

    @Column(name = "waiter_id")
    private UUID waiterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public TableWaiterAssignment id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getAssignmentDate() {
        return this.assignmentDate;
    }

    public TableWaiterAssignment assignmentDate(LocalDate assignmentDate) {
        this.setAssignmentDate(assignmentDate);
        return this;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public TableWaiterAssignment startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public TableWaiterAssignment endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TableWaiterAssignment isActive(Boolean isActive) {
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

    public TableWaiterAssignment setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public TableAssignment getTableAssignment() {
        return this.tableAssignment;
    }

    public void setTableAssignment(TableAssignment tableAssignment) {
        this.tableAssignment = tableAssignment;
        this.tableAssignmentId = tableAssignment != null ? tableAssignment.getId() : null;
    }

    public TableWaiterAssignment tableAssignment(TableAssignment tableAssignment) {
        this.setTableAssignment(tableAssignment);
        return this;
    }

    public RmsUser getWaiter() {
        return this.waiter;
    }

    public void setWaiter(RmsUser rmsUser) {
        this.waiter = rmsUser;
        this.waiterId = rmsUser != null ? rmsUser.getId() : null;
    }

    public TableWaiterAssignment waiter(RmsUser rmsUser) {
        this.setWaiter(rmsUser);
        return this;
    }

    public UUID getTableAssignmentId() {
        return this.tableAssignmentId;
    }

    public void setTableAssignmentId(UUID tableAssignment) {
        this.tableAssignmentId = tableAssignment;
    }

    public UUID getWaiterId() {
        return this.waiterId;
    }

    public void setWaiterId(UUID rmsUser) {
        this.waiterId = rmsUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TableWaiterAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((TableWaiterAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TableWaiterAssignment{" +
            "id=" + getId() +
            ", assignmentDate='" + getAssignmentDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
