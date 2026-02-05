package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

/**
 * A TaxConfig.
 */
@Entity
@Table(name = "tax_config")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxConfig implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column(name = "tax_name")
    private String taxName;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "tax_code")
    private String taxCode;

    @NotNull(message = "must not be null")
    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "tax_type")
    private String taxType;

    @Column(name = "is_applicable_to_food")
    private Boolean isApplicableToFood;

    @Column(name = "is_applicable_to_beverage")
    private Boolean isApplicableToBeverage;

    @Column(name = "is_applicable_to_alcohol")
    private Boolean isApplicableToAlcohol;

    @NotNull(message = "must not be null")
    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "is_active")
    private Boolean isActive;

    @Transient
    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @Transient
    @org.springframework.data.annotation.Transient
    private Restaurant restaurant;

    @Column(name = "restaurant_id")
    private UUID restaurantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public TaxConfig id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTaxName() {
        return this.taxName;
    }

    public TaxConfig taxName(String taxName) {
        this.setTaxName(taxName);
        return this;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxCode() {
        return this.taxCode;
    }

    public TaxConfig taxCode(String taxCode) {
        this.setTaxCode(taxCode);
        return this;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public TaxConfig taxRate(BigDecimal taxRate) {
        this.setTaxRate(taxRate);
        return this;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate != null ? taxRate.stripTrailingZeros() : null;
    }

    public String getTaxType() {
        return this.taxType;
    }

    public TaxConfig taxType(String taxType) {
        this.setTaxType(taxType);
        return this;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Boolean getIsApplicableToFood() {
        return this.isApplicableToFood;
    }

    public TaxConfig isApplicableToFood(Boolean isApplicableToFood) {
        this.setIsApplicableToFood(isApplicableToFood);
        return this;
    }

    public void setIsApplicableToFood(Boolean isApplicableToFood) {
        this.isApplicableToFood = isApplicableToFood;
    }

    public Boolean getIsApplicableToBeverage() {
        return this.isApplicableToBeverage;
    }

    public TaxConfig isApplicableToBeverage(Boolean isApplicableToBeverage) {
        this.setIsApplicableToBeverage(isApplicableToBeverage);
        return this;
    }

    public void setIsApplicableToBeverage(Boolean isApplicableToBeverage) {
        this.isApplicableToBeverage = isApplicableToBeverage;
    }

    public Boolean getIsApplicableToAlcohol() {
        return this.isApplicableToAlcohol;
    }

    public TaxConfig isApplicableToAlcohol(Boolean isApplicableToAlcohol) {
        this.setIsApplicableToAlcohol(isApplicableToAlcohol);
        return this;
    }

    public void setIsApplicableToAlcohol(Boolean isApplicableToAlcohol) {
        this.isApplicableToAlcohol = isApplicableToAlcohol;
    }

    public LocalDate getEffectiveFrom() {
        return this.effectiveFrom;
    }

    public TaxConfig effectiveFrom(LocalDate effectiveFrom) {
        this.setEffectiveFrom(effectiveFrom);
        return this;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return this.effectiveTo;
    }

    public TaxConfig effectiveTo(LocalDate effectiveTo) {
        this.setEffectiveTo(effectiveTo);
        return this;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TaxConfig isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Transient
    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public TaxConfig setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurantId = restaurant != null ? restaurant.getId() : null;
    }

    public TaxConfig restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public UUID getRestaurantId() {
        return this.restaurantId;
    }

    public void setRestaurantId(UUID restaurant) {
        this.restaurantId = restaurant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((TaxConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxConfig{" +
            "id=" + getId() +
            ", taxName='" + getTaxName() + "'" +
            ", taxCode='" + getTaxCode() + "'" +
            ", taxRate=" + getTaxRate() +
            ", taxType='" + getTaxType() + "'" +
            ", isApplicableToFood='" + getIsApplicableToFood() + "'" +
            ", isApplicableToBeverage='" + getIsApplicableToBeverage() + "'" +
            ", isApplicableToAlcohol='" + getIsApplicableToAlcohol() + "'" +
            ", effectiveFrom='" + getEffectiveFrom() + "'" +
            ", effectiveTo='" + getEffectiveTo() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
