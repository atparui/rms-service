package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Inventory;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Inventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    @Query(value = "SELECT * FROM inventory entity WHERE entity.branch_id = :id", nativeQuery = true)
    List<Inventory> findByBranch(UUID id);

    @Query(value = "SELECT * FROM inventory entity WHERE entity.branch_id IS NULL", nativeQuery = true)
    List<Inventory> findAllByBranchIsNull();

    @Query(value = "SELECT * FROM inventory entity WHERE entity.menu_item_id = :id", nativeQuery = true)
    List<Inventory> findByMenuItem(UUID id);

    @Query(value = "SELECT * FROM inventory entity WHERE entity.menu_item_id IS NULL", nativeQuery = true)
    List<Inventory> findAllByMenuItemIsNull();

    @Query(value = "SELECT * FROM inventory entity WHERE entity.branch_id = :branchId AND entity.current_stock < entity.minimum_stock", nativeQuery = true)
    List<Inventory> findLowStockByBranchId(UUID branchId);

    @Override
    <S extends Inventory> S save(S entity);

    @Override
    List<Inventory> findAll();

    @Override
    Optional<Inventory> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

