package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A Inventory.
 */
@Entity
@Table(name = "inventory")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inventory implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "current_stock")
    private BigDecimal currentStock;

    @Size(max = 50)
    @Column(name = "unit")
    private String unit;

    @Column(name = "min_stock_level")
    private BigDecimal minStockLevel;

    @Column(name = "max_stock_level")
    private BigDecimal maxStockLevel;

    @NotNull(message = "must not be null")
    @Column(name = "last_updated_at")
    private Instant lastUpdatedAt;

    @Size(max = 255)
    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private Branch branch;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "branch", "menuCategory" }, allowSetters = true)
    private MenuItem menuItem;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "menu_item_id")
    private UUID menuItemId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Inventory id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getCurrentStock() {
        return this.currentStock;
    }

    public Inventory currentStock(BigDecimal currentStock) {
        this.setCurrentStock(currentStock);
        return this;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock != null ? currentStock.stripTrailingZeros() : null;
    }

    public String getUnit() {
        return this.unit;
    }

    public Inventory unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getMinStockLevel() {
        return this.minStockLevel;
    }

    public Inventory minStockLevel(BigDecimal minStockLevel) {
        this.setMinStockLevel(minStockLevel);
        return this;
    }

    public void setMinStockLevel(BigDecimal minStockLevel) {
        this.minStockLevel = minStockLevel != null ? minStockLevel.stripTrailingZeros() : null;
    }

    public BigDecimal getMaxStockLevel() {
        return this.maxStockLevel;
    }

    public Inventory maxStockLevel(BigDecimal maxStockLevel) {
        this.setMaxStockLevel(maxStockLevel);
        return this;
    }

    public void setMaxStockLevel(BigDecimal maxStockLevel) {
        this.maxStockLevel = maxStockLevel != null ? maxStockLevel.stripTrailingZeros() : null;
    }

    public Instant getLastUpdatedAt() {
        return this.lastUpdatedAt;
    }

    public Inventory lastUpdatedAt(Instant lastUpdatedAt) {
        this.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public Inventory lastUpdatedBy(String lastUpdatedBy) {
        this.setLastUpdatedBy(lastUpdatedBy);
        return this;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Inventory setIsPersisted() {
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

    public Inventory branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    public MenuItem getMenuItem() {
        return this.menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        this.menuItemId = menuItem != null ? menuItem.getId() : null;
    }

    public Inventory menuItem(MenuItem menuItem) {
        this.setMenuItem(menuItem);
        return this;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branch) {
        this.branchId = branch;
    }

    public UUID getMenuItemId() {
        return this.menuItemId;
    }

    public void setMenuItemId(UUID menuItem) {
        this.menuItemId = menuItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventory)) {
            return false;
        }
        return getId() != null && getId().equals(((Inventory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", currentStock=" + getCurrentStock() +
            ", unit='" + getUnit() + "'" +
            ", minStockLevel=" + getMinStockLevel() +
            ", maxStockLevel=" + getMaxStockLevel() +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            "}";
    }
}
