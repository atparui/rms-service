package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.MenuItemAddon;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuItemAddon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemAddonRepository extends JpaRepository<MenuItemAddon, UUID> {
    @Query(value = "SELECT * FROM menu_item_addon entity WHERE entity.menu_item_id = :id", nativeQuery = true)
    List<MenuItemAddon> findByMenuItem(UUID id);

    @Query(value = "SELECT * FROM menu_item_addon entity WHERE entity.menu_item_id IS NULL", nativeQuery = true)
    List<MenuItemAddon> findAllByMenuItemIsNull();

    @Override
    <S extends MenuItemAddon> S save(S entity);

    @Override
    List<MenuItemAddon> findAll();

    @Override
    Optional<MenuItemAddon> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

