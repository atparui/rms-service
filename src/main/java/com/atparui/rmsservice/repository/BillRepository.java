package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Bill;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {
    List<Bill> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM bill entity WHERE entity.order_id = :id", nativeQuery = true)
    List<Bill> findByOrder(UUID id);

    @Query(value = "SELECT * FROM bill entity WHERE entity.order_id IS NULL", nativeQuery = true)
    List<Bill> findAllByOrderIsNull();

    @Query(value = "SELECT * FROM bill entity WHERE entity.branch_id = :id", nativeQuery = true)
    List<Bill> findByBranch(UUID id);

    @Query(value = "SELECT * FROM bill entity WHERE entity.branch_id IS NULL", nativeQuery = true)
    List<Bill> findAllByBranchIsNull();

    @Query(value = "SELECT * FROM bill entity WHERE entity.customer_id = :id", nativeQuery = true)
    List<Bill> findByCustomer(UUID id);

    @Query(value = "SELECT * FROM bill entity WHERE entity.customer_id IS NULL", nativeQuery = true)
    List<Bill> findAllByCustomerIsNull();

    @Query(
        "SELECT * FROM bill entity WHERE entity.branch_id = :branchId AND entity.bill_date >= :startDate AND entity.bill_date <= :endDate"
    )
    List<Bill> findByBranchIdAndBillDateBetween(UUID branchId, java.time.Instant startDate, java.time.Instant endDate);

    @Override
    <S extends Bill> S save(S entity);

    @Override
    List<Bill> findAll();

    @Override
    Optional<Bill> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

