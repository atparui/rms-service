package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.OrderStatusHistory;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderStatusHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderStatusHistoryRepository
    extends JpaRepository<OrderStatusHistory, UUID> {
    @Query(value = "SELECT * FROM order_status_history entity WHERE entity.order_id = :id", nativeQuery = true)
    List<OrderStatusHistory> findByOrder(UUID id);

    @Query(value = "SELECT * FROM order_status_history entity WHERE entity.order_id IS NULL", nativeQuery = true)
    List<OrderStatusHistory> findAllByOrderIsNull();

    @Override
    <S extends OrderStatusHistory> S save(S entity);

    @Override
    List<OrderStatusHistory> findAll();

    @Override
    Optional<OrderStatusHistory> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

