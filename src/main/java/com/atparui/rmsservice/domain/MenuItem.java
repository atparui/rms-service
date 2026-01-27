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
 * A MenuItem.
 */
@Entity
@Table(name = "menu_item")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuItem implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column(name = "item_type")
    private String itemType;

    @Size(max = 100)
    @Column(name = "cuisine_type")
    private String cuisineType;

    @Column(name = "is_vegetarian")
    private Boolean isVegetarian;

    @Column(name = "is_vegan")
    private Boolean isVegan;

    @Column(name = "is_alcoholic")
    private Boolean isAlcoholic;

    @Column(name = "spice_level")
    private Integer spiceLevel;

    @Column(name = "preparation_time")
    private Integer preparationTime;

    @NotNull(message = "must not be null")
    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Size(max = 500)
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "display_order")
    private Integer displayOrder;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private Branch branch;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private MenuCategory menuCategory;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "menu_category_id")
    private UUID menuCategoryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public MenuItem id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MenuItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public MenuItem code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public MenuItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemType() {
        return this.itemType;
    }

    public MenuItem itemType(String itemType) {
        this.setItemType(itemType);
        return this;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getCuisineType() {
        return this.cuisineType;
    }

    public MenuItem cuisineType(String cuisineType) {
        this.setCuisineType(cuisineType);
        return this;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public Boolean getIsVegetarian() {
        return this.isVegetarian;
    }

    public MenuItem isVegetarian(Boolean isVegetarian) {
        this.setIsVegetarian(isVegetarian);
        return this;
    }

    public void setIsVegetarian(Boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    public Boolean getIsVegan() {
        return this.isVegan;
    }

    public MenuItem isVegan(Boolean isVegan) {
        this.setIsVegan(isVegan);
        return this;
    }

    public void setIsVegan(Boolean isVegan) {
        this.isVegan = isVegan;
    }

    public Boolean getIsAlcoholic() {
        return this.isAlcoholic;
    }

    public MenuItem isAlcoholic(Boolean isAlcoholic) {
        this.setIsAlcoholic(isAlcoholic);
        return this;
    }

    public void setIsAlcoholic(Boolean isAlcoholic) {
        this.isAlcoholic = isAlcoholic;
    }

    public Integer getSpiceLevel() {
        return this.spiceLevel;
    }

    public MenuItem spiceLevel(Integer spiceLevel) {
        this.setSpiceLevel(spiceLevel);
        return this;
    }

    public void setSpiceLevel(Integer spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public Integer getPreparationTime() {
        return this.preparationTime;
    }

    public MenuItem preparationTime(Integer preparationTime) {
        this.setPreparationTime(preparationTime);
        return this;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public MenuItem basePrice(BigDecimal basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice != null ? basePrice.stripTrailingZeros() : null;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public MenuItem imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsAvailable() {
        return this.isAvailable;
    }

    public MenuItem isAvailable(Boolean isAvailable) {
        this.setIsAvailable(isAvailable);
        return this;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MenuItem isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public MenuItem displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public MenuItem setIsPersisted() {
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

    public MenuItem branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    public MenuCategory getMenuCategory() {
        return this.menuCategory;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
        this.menuCategoryId = menuCategory != null ? menuCategory.getId() : null;
    }

    public MenuItem menuCategory(MenuCategory menuCategory) {
        this.setMenuCategory(menuCategory);
        return this;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branch) {
        this.branchId = branch;
    }

    public UUID getMenuCategoryId() {
        return this.menuCategoryId;
    }

    public void setMenuCategoryId(UUID menuCategory) {
        this.menuCategoryId = menuCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItem)) {
            return false;
        }
        return getId() != null && getId().equals(((MenuItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", itemType='" + getItemType() + "'" +
            ", cuisineType='" + getCuisineType() + "'" +
            ", isVegetarian='" + getIsVegetarian() + "'" +
            ", isVegan='" + getIsVegan() + "'" +
            ", isAlcoholic='" + getIsAlcoholic() + "'" +
            ", spiceLevel=" + getSpiceLevel() +
            ", preparationTime=" + getPreparationTime() +
            ", basePrice=" + getBasePrice() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", isAvailable='" + getIsAvailable() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            "}";
    }
}
