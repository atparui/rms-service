package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Customer;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM customer entity WHERE entity.user_id = :id", nativeQuery = true)
    List<Customer> findByUser(UUID id);

    @Query(value = "SELECT * FROM customer entity WHERE entity.user_id IS NULL", nativeQuery = true)
    List<Customer> findAllByUserIsNull();

    @Override
    <S extends Customer> S save(S entity);

    @Override
    List<Customer> findAll();

    @Override
    Optional<Customer> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

