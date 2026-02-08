package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A TableAssignment.
 */
@Entity
@Table(name = "table_assignment")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TableAssignment implements Serializable, Persistable<UUID> {

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

    @jakarta.persistence.Transient
    private boolean isPersisted;

    @jakarta.persistence.Transient
    @JsonIgnoreProperties(value = { "branch" }, allowSetters = true)
    private BranchTable branchTable;

    @jakarta.persistence.Transient
    @JsonIgnoreProperties(value = { "branch" }, allowSetters = true)
    private Shift shift;

    @jakarta.persistence.Transient
    private RmsUser supervisor;

    @Column(name = "branch_table_id")
    private UUID branchTableId;

    @Column(name = "shift_id")
    private UUID shiftId;

    @Column(name = "supervisor_id")
    private UUID supervisorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public TableAssignment id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getAssignmentDate() {
        return this.assignmentDate;
    }

    public TableAssignment assignmentDate(LocalDate assignmentDate) {
        this.setAssignmentDate(assignmentDate);
        return this;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public TableAssignment startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public TableAssignment endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TableAssignment isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @jakarta.persistence.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public TableAssignment setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public BranchTable getBranchTable() {
        return this.branchTable;
    }

    public void setBranchTable(BranchTable branchTable) {
        this.branchTable = branchTable;
        this.branchTableId = branchTable != null ? branchTable.getId() : null;
    }

    public TableAssignment branchTable(BranchTable branchTable) {
        this.setBranchTable(branchTable);
        return this;
    }

    public Shift getShift() {
        return this.shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
        this.shiftId = shift != null ? shift.getId() : null;
    }

    public TableAssignment shift(Shift shift) {
        this.setShift(shift);
        return this;
    }

    public RmsUser getSupervisor() {
        return this.supervisor;
    }

    public void setSupervisor(RmsUser rmsUser) {
        this.supervisor = rmsUser;
        this.supervisorId = rmsUser != null ? rmsUser.getId() : null;
    }

    public TableAssignment supervisor(RmsUser rmsUser) {
        this.setSupervisor(rmsUser);
        return this;
    }

    public UUID getBranchTableId() {
        return this.branchTableId;
    }

    public void setBranchTableId(UUID branchTable) {
        this.branchTableId = branchTable;
    }

    public UUID getShiftId() {
        return this.shiftId;
    }

    public void setShiftId(UUID shift) {
        this.shiftId = shift;
    }

    public UUID getSupervisorId() {
        return this.supervisorId;
    }

    public void setSupervisorId(UUID rmsUser) {
        this.supervisorId = rmsUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TableAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((TableAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TableAssignment{" +
            "id=" + getId() +
            ", assignmentDate='" + getAssignmentDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
