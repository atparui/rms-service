package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.BillTax;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BillTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillTaxRepository extends JpaRepository<BillTax, UUID> {
    @Query(value = "SELECT * FROM bill_tax entity WHERE entity.bill_id = :id", nativeQuery = true)
    List<BillTax> findByBill(UUID id);

    @Query(value = "SELECT * FROM bill_tax entity WHERE entity.bill_id IS NULL", nativeQuery = true)
    List<BillTax> findAllByBillIsNull();

    @Query(value = "SELECT * FROM bill_tax entity WHERE entity.tax_config_id = :id", nativeQuery = true)
    List<BillTax> findByTaxConfig(UUID id);

    @Query(value = "SELECT * FROM bill_tax entity WHERE entity.tax_config_id IS NULL", nativeQuery = true)
    List<BillTax> findAllByTaxConfigIsNull();

    @Override
    <S extends BillTax> S save(S entity);

    @Override
    List<BillTax> findAll();

    @Override
    Optional<BillTax> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

