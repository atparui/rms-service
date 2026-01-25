package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.BillDiscount;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BillDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillDiscountRepository extends JpaRepository<BillDiscount, UUID> {
    @Query(value = "SELECT * FROM bill_discount entity WHERE entity.bill_id = :id", nativeQuery = true)
    List<BillDiscount> findByBill(UUID id);

    @Query(value = "SELECT * FROM bill_discount entity WHERE entity.bill_id IS NULL", nativeQuery = true)
    List<BillDiscount> findAllByBillIsNull();

    @Query(value = "SELECT * FROM bill_discount entity WHERE entity.discount_id = :id", nativeQuery = true)
    List<BillDiscount> findByDiscount(UUID id);

    @Query(value = "SELECT * FROM bill_discount entity WHERE entity.discount_id IS NULL", nativeQuery = true)
    List<BillDiscount> findAllByDiscountIsNull();

    @Override
    <S extends BillDiscount> S save(S entity);

    @Override
    List<BillDiscount> findAll();

    @Override
    Optional<BillDiscount> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

