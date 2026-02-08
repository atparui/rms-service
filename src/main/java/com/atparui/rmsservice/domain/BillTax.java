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
import org.springframework.data.domain.Persistable;

/**
 * A BillTax.
 */
@Entity
@Table(name = "bill_tax")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillTax implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column(name = "tax_name")
    private String taxName;

    @NotNull(message = "must not be null")
    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @NotNull(message = "must not be null")
    @Column(name = "taxable_amount")
    private BigDecimal taxableAmount;

    @NotNull(message = "must not be null")
    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @jakarta.persistence.Transient
    private boolean isPersisted;

    @jakarta.persistence.Transient
    @JsonIgnoreProperties(value = { "order", "branch", "customer" }, allowSetters = true)
    private Bill bill;

    @jakarta.persistence.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private TaxConfig taxConfig;

    @Column(name = "bill_id")
    private UUID billId;

    @Column(name = "tax_config_id")
    private UUID taxConfigId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public BillTax id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTaxName() {
        return this.taxName;
    }

    public BillTax taxName(String taxName) {
        this.setTaxName(taxName);
        return this;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public BillTax taxRate(BigDecimal taxRate) {
        this.setTaxRate(taxRate);
        return this;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate != null ? taxRate.stripTrailingZeros() : null;
    }

    public BigDecimal getTaxableAmount() {
        return this.taxableAmount;
    }

    public BillTax taxableAmount(BigDecimal taxableAmount) {
        this.setTaxableAmount(taxableAmount);
        return this;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount != null ? taxableAmount.stripTrailingZeros() : null;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public BillTax taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount != null ? taxAmount.stripTrailingZeros() : null;
    }

    @jakarta.persistence.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public BillTax setIsPersisted() {
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

    public BillTax bill(Bill bill) {
        this.setBill(bill);
        return this;
    }

    public TaxConfig getTaxConfig() {
        return this.taxConfig;
    }

    public void setTaxConfig(TaxConfig taxConfig) {
        this.taxConfig = taxConfig;
        this.taxConfigId = taxConfig != null ? taxConfig.getId() : null;
    }

    public BillTax taxConfig(TaxConfig taxConfig) {
        this.setTaxConfig(taxConfig);
        return this;
    }

    public UUID getBillId() {
        return this.billId;
    }

    public void setBillId(UUID bill) {
        this.billId = bill;
    }

    public UUID getTaxConfigId() {
        return this.taxConfigId;
    }

    public void setTaxConfigId(UUID taxConfig) {
        this.taxConfigId = taxConfig;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillTax)) {
            return false;
        }
        return getId() != null && getId().equals(((BillTax) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillTax{" +
            "id=" + getId() +
            ", taxName='" + getTaxName() + "'" +
            ", taxRate=" + getTaxRate() +
            ", taxableAmount=" + getTaxableAmount() +
            ", taxAmount=" + getTaxAmount() +
            "}";
    }
}
