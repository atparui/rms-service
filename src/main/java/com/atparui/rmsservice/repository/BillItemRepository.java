package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.BillItem;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BillItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillItemRepository extends JpaRepository<BillItem, UUID> {
    @Query(value = "SELECT * FROM bill_item entity WHERE entity.bill_id = :id", nativeQuery = true)
    List<BillItem> findByBill(UUID id);

    @Query(value = "SELECT * FROM bill_item entity WHERE entity.bill_id IS NULL", nativeQuery = true)
    List<BillItem> findAllByBillIsNull();

    @Query(value = "SELECT * FROM bill_item entity WHERE entity.order_item_id = :id", nativeQuery = true)
    List<BillItem> findByOrderItem(UUID id);

    @Query(value = "SELECT * FROM bill_item entity WHERE entity.order_item_id IS NULL", nativeQuery = true)
    List<BillItem> findAllByOrderItemIsNull();

    @Override
    <S extends BillItem> S save(S entity);

    @Override
    List<BillItem> findAll();

    @Override
    Optional<BillItem> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

