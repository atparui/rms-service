package com.atparui.rmsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.springframework.data.domain.Persistable;

/**
 * A PaymentMethod.
 */
@Entity
@Table(name = "payment_method")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentMethod implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "method_code")
    private String methodCode;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column(name = "method_name")
    private String methodName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @jakarta.persistence.Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public PaymentMethod id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMethodCode() {
        return this.methodCode;
    }

    public PaymentMethod methodCode(String methodCode) {
        this.setMethodCode(methodCode);
        return this;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public PaymentMethod methodName(String methodName) {
        this.setMethodName(methodName);
        return this;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return this.description;
    }

    public PaymentMethod description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public PaymentMethod isActive(Boolean isActive) {
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

    public PaymentMethod setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentMethod)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentMethod) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethod{" +
            "id=" + getId() +
            ", methodCode='" + getMethodCode() + "'" +
            ", methodName='" + getMethodName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
