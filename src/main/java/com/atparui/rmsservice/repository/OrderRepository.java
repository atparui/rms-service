package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Order;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.branch_id = :id", nativeQuery = true)
    List<Order> findByBranch(UUID id);

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.branch_id IS NULL", nativeQuery = true)
    List<Order> findAllByBranchIsNull();

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.customer_id = :id", nativeQuery = true)
    List<Order> findByCustomer(UUID id);

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.customer_id IS NULL", nativeQuery = true)
    List<Order> findAllByCustomerIsNull();

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.user_id = :id", nativeQuery = true)
    List<Order> findByUser(UUID id);

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.user_id IS NULL", nativeQuery = true)
    List<Order> findAllByUserIsNull();

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.branch_table_id = :id", nativeQuery = true)
    List<Order> findByBranchTable(UUID id);

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.branch_table_id IS NULL", nativeQuery = true)
    List<Order> findAllByBranchTableIsNull();

    @Query(value = "SELECT * FROM jhi_order entity WHERE entity.branch_id = :branchId AND entity.status = :status", nativeQuery = true)
    List<Order> findByBranchIdAndStatus(UUID branchId, String status);

    @Query(
        "SELECT * FROM jhi_order entity WHERE entity.branch_id = :branchId AND entity.order_date >= :startDate AND entity.order_date <= :endDate"
    )
    List<Order> findByBranchIdAndOrderDateBetween(UUID branchId, java.time.Instant startDate, java.time.Instant endDate);

    @Override
    <S extends Order> S save(S entity);

    @Override
    List<Order> findAll();

    @Override
    Optional<Order> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

