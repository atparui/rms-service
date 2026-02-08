package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A Shift.
 */
@Entity
@Table(name = "shift")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Shift implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column(name = "shift_name")
    private String shiftName;

    @NotNull(message = "must not be null")
    @Column(name = "start_time")
    private LocalTime startTime;

    @NotNull(message = "must not be null")
    @Column(name = "end_time")
    private LocalTime endTime;

    @NotNull(message = "must not be null")
    @Column(name = "shift_date")
    private LocalDate shiftDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @jakarta.persistence.Transient
    private boolean isPersisted;

    @jakarta.persistence.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private Branch branch;

    @Column(name = "branch_id")
    private UUID branchId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Shift id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getShiftName() {
        return this.shiftName;
    }

    public Shift shiftName(String shiftName) {
        this.setShiftName(shiftName);
        return this;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public Shift startTime(LocalTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public Shift endTime(LocalTime endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getShiftDate() {
        return this.shiftDate;
    }

    public Shift shiftDate(LocalDate shiftDate) {
        this.setShiftDate(shiftDate);
        return this;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Shift isActive(Boolean isActive) {
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

    public Shift setIsPersisted() {
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

    public Shift branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branch) {
        this.branchId = branch;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shift)) {
            return false;
        }
        return getId() != null && getId().equals(((Shift) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shift{" +
            "id=" + getId() +
            ", shiftName='" + getShiftName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", shiftDate='" + getShiftDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
