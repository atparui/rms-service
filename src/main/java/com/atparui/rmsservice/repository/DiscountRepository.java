package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Discount;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Discount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    @Query(value = "SELECT * FROM discount entity WHERE entity.restaurant_id = :id", nativeQuery = true)
    List<Discount> findByRestaurant(UUID id);

    @Query(value = "SELECT * FROM discount entity WHERE entity.restaurant_id IS NULL", nativeQuery = true)
    List<Discount> findAllByRestaurantIsNull();

    @Query(
        "SELECT * FROM discount entity WHERE entity.restaurant_id = :restaurantId AND entity.is_active = true AND (entity.valid_from <= CURRENT_DATE AND (entity.valid_to IS NULL OR entity.valid_to >= CURRENT_DATE))"
    )
    List<Discount> findActiveByRestaurantId(UUID restaurantId);

    @Query(value = "SELECT * FROM discount entity WHERE entity.discount_code = :discountCode AND entity.restaurant_id = :restaurantId", nativeQuery = true)
    Optional<Discount> findByDiscountCodeAndRestaurantId(String discountCode, UUID restaurantId);

    @Override
    <S extends Discount> S save(S entity);

    @Override
    List<Discount> findAll();

    @Override
    Optional<Discount> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

