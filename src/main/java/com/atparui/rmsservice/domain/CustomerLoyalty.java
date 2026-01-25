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
 * A CustomerLoyalty.
 */
@Entity
@Table(name = "customer_loyalty")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerLoyalty implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "loyalty_points")
    private BigDecimal loyaltyPoints;

    @Size(max = 50)
    @Column(name = "tier")
    private String tier;

    @NotNull(message = "must not be null")
    @Column(name = "enrolled_at")
    private Instant enrolledAt;

    @Column(name = "last_points_earned_at")
    private Instant lastPointsEarnedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Customer customer;

    @org.springframework.data.annotation.Transient
    private Restaurant restaurant;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "restaurant_id")
    private UUID restaurantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public CustomerLoyalty id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getLoyaltyPoints() {
        return this.loyaltyPoints;
    }

    public CustomerLoyalty loyaltyPoints(BigDecimal loyaltyPoints) {
        this.setLoyaltyPoints(loyaltyPoints);
        return this;
    }

    public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints != null ? loyaltyPoints.stripTrailingZeros() : null;
    }

    public String getTier() {
        return this.tier;
    }

    public CustomerLoyalty tier(String tier) {
        this.setTier(tier);
        return this;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Instant getEnrolledAt() {
        return this.enrolledAt;
    }

    public CustomerLoyalty enrolledAt(Instant enrolledAt) {
        this.setEnrolledAt(enrolledAt);
        return this;
    }

    public void setEnrolledAt(Instant enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public Instant getLastPointsEarnedAt() {
        return this.lastPointsEarnedAt;
    }

    public CustomerLoyalty lastPointsEarnedAt(Instant lastPointsEarnedAt) {
        this.setLastPointsEarnedAt(lastPointsEarnedAt);
        return this;
    }

    public void setLastPointsEarnedAt(Instant lastPointsEarnedAt) {
        this.lastPointsEarnedAt = lastPointsEarnedAt;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public CustomerLoyalty isActive(Boolean isActive) {
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

    public CustomerLoyalty setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer != null ? customer.getId() : null;
    }

    public CustomerLoyalty customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurantId = restaurant != null ? restaurant.getId() : null;
    }

    public CustomerLoyalty restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(UUID customer) {
        this.customerId = customer;
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
        if (!(o instanceof CustomerLoyalty)) {
            return false;
        }
        return getId() != null && getId().equals(((CustomerLoyalty) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerLoyalty{" +
            "id=" + getId() +
            ", loyaltyPoints=" + getLoyaltyPoints() +
            ", tier='" + getTier() + "'" +
            ", enrolledAt='" + getEnrolledAt() + "'" +
            ", lastPointsEarnedAt='" + getLastPointsEarnedAt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
