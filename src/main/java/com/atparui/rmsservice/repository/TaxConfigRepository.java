package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.TaxConfig;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaxConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxConfigRepository extends JpaRepository<TaxConfig, UUID> {
    @Query(value = "SELECT * FROM tax_config entity WHERE entity.restaurant_id = :id", nativeQuery = true)
    List<TaxConfig> findByRestaurant(UUID id);

    @Query(value = "SELECT * FROM tax_config entity WHERE entity.restaurant_id IS NULL", nativeQuery = true)
    List<TaxConfig> findAllByRestaurantIsNull();

    @Override
    <S extends TaxConfig> S save(S entity);

    @Override
    List<TaxConfig> findAll();

    @Override
    Optional<TaxConfig> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

