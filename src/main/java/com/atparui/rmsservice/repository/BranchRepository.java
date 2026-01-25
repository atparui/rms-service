package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Branch;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Branch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {
    List<Branch> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM branch entity WHERE entity.restaurant_id = :id", nativeQuery = true)
    List<Branch> findByRestaurant(UUID id);

    @Query(value = "SELECT * FROM branch entity WHERE entity.restaurant_id IS NULL", nativeQuery = true)
    List<Branch> findAllByRestaurantIsNull();

    @Override
    <S extends Branch> S save(S entity);

    @Override
    List<Branch> findAll();

    @Override
    Optional<Branch> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

