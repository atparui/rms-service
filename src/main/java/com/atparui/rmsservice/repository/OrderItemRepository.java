package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.OrderItem;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    @Query(value = "SELECT * FROM order_item entity WHERE entity.order_id = :id", nativeQuery = true)
    List<OrderItem> findByOrder(UUID id);

    @Query(value = "SELECT * FROM order_item entity WHERE entity.order_id IS NULL", nativeQuery = true)
    List<OrderItem> findAllByOrderIsNull();

    @Query(value = "SELECT * FROM order_item entity WHERE entity.menu_item_id = :id", nativeQuery = true)
    List<OrderItem> findByMenuItem(UUID id);

    @Query(value = "SELECT * FROM order_item entity WHERE entity.menu_item_id IS NULL", nativeQuery = true)
    List<OrderItem> findAllByMenuItemIsNull();

    @Query(value = "SELECT * FROM order_item entity WHERE entity.menu_item_variant_id = :id", nativeQuery = true)
    List<OrderItem> findByMenuItemVariant(UUID id);

    @Query(value = "SELECT * FROM order_item entity WHERE entity.menu_item_variant_id IS NULL", nativeQuery = true)
    List<OrderItem> findAllByMenuItemVariantIsNull();

    @Override
    <S extends OrderItem> S save(S entity);

    @Override
    List<OrderItem> findAll();

    @Override
    Optional<OrderItem> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

