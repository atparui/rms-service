package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.OrderItemCustomization;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderItemCustomization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemCustomizationRepository
    extends JpaRepository<OrderItemCustomization, UUID> {
    @Query(value = "SELECT * FROM order_item_customization entity WHERE entity.order_item_id = :id", nativeQuery = true)
    List<OrderItemCustomization> findByOrderItem(UUID id);

    @Query(value = "SELECT * FROM order_item_customization entity WHERE entity.order_item_id IS NULL", nativeQuery = true)
    List<OrderItemCustomization> findAllByOrderItemIsNull();

    @Query(value = "SELECT * FROM order_item_customization entity WHERE entity.menu_item_addon_id = :id", nativeQuery = true)
    List<OrderItemCustomization> findByMenuItemAddon(UUID id);

    @Query(value = "SELECT * FROM order_item_customization entity WHERE entity.menu_item_addon_id IS NULL", nativeQuery = true)
    List<OrderItemCustomization> findAllByMenuItemAddonIsNull();

    @Override
    <S extends OrderItemCustomization> S save(S entity);

    @Override
    List<OrderItemCustomization> findAll();

    @Override
    Optional<OrderItemCustomization> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

