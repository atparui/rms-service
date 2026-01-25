package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.CustomerLoyalty;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerLoyalty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerLoyaltyRepository extends JpaRepository<CustomerLoyalty, UUID> {
    @Query(value = "SELECT * FROM customer_loyalty entity WHERE entity.customer_id = :id", nativeQuery = true)
    List<CustomerLoyalty> findByCustomer(UUID id);

    @Query(value = "SELECT * FROM customer_loyalty entity WHERE entity.customer_id IS NULL", nativeQuery = true)
    List<CustomerLoyalty> findAllByCustomerIsNull();

    @Query(value = "SELECT * FROM customer_loyalty entity WHERE entity.restaurant_id = :id", nativeQuery = true)
    List<CustomerLoyalty> findByRestaurant(UUID id);

    @Query(value = "SELECT * FROM customer_loyalty entity WHERE entity.restaurant_id IS NULL", nativeQuery = true)
    List<CustomerLoyalty> findAllByRestaurantIsNull();

    @Override
    <S extends CustomerLoyalty> S save(S entity);

    @Override
    List<CustomerLoyalty> findAll();

    @Override
    Optional<CustomerLoyalty> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

