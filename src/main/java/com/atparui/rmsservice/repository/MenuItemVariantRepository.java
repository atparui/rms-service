package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.MenuItemVariant;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuItemVariant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemVariantRepository extends JpaRepository<MenuItemVariant, UUID> {
    @Query(value = "SELECT * FROM menu_item_variant entity WHERE entity.menu_item_id = :id", nativeQuery = true)
    List<MenuItemVariant> findByMenuItem(UUID id);

    @Query(value = "SELECT * FROM menu_item_variant entity WHERE entity.menu_item_id IS NULL", nativeQuery = true)
    List<MenuItemVariant> findAllByMenuItemIsNull();

    @Override
    <S extends MenuItemVariant> S save(S entity);

    @Override
    List<MenuItemVariant> findAll();

    @Override
    Optional<MenuItemVariant> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

