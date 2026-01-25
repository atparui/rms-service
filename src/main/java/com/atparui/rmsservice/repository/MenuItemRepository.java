package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.MenuItem;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
@org.javers.spring.annotation.JaversSpringDataAuditable
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    List<MenuItem> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM menu_item entity WHERE entity.branch_id = :id", nativeQuery = true)
    List<MenuItem> findByBranch(UUID id);

    @Query(value = "SELECT * FROM menu_item entity WHERE entity.branch_id IS NULL", nativeQuery = true)
    List<MenuItem> findAllByBranchIsNull();

    @Query(value = "SELECT * FROM menu_item entity WHERE entity.menu_category_id = :id", nativeQuery = true)
    List<MenuItem> findByMenuCategory(UUID id);

    @Query(value = "SELECT * FROM menu_item entity WHERE entity.menu_category_id IS NULL", nativeQuery = true)
    List<MenuItem> findAllByMenuCategoryIsNull();

    @Query(value = "SELECT * FROM menu_item entity WHERE entity.branch_id = :branchId AND entity.is_available = true", nativeQuery = true)
    List<MenuItem> findAvailableByBranchId(UUID branchId);

    @Query(value = "SELECT * FROM menu_item entity WHERE entity.menu_category_id = :categoryId", nativeQuery = true)
    List<MenuItem> findByMenuCategoryId(UUID categoryId);

    @Override
    <S extends MenuItem> S save(S entity);

    @Override
    List<MenuItem> findAll();

    @Override
    Optional<MenuItem> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

