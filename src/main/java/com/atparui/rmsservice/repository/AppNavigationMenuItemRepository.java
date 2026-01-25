package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.AppNavigationMenuItem;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppNavigationMenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppNavigationMenuItemRepository
    extends JpaRepository<AppNavigationMenuItem, UUID> {
    List<AppNavigationMenuItem> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM app_navigation_menu_item entity WHERE entity.parent_menu_id = :id", nativeQuery = true)
    List<AppNavigationMenuItem> findByParentMenu(UUID id);

    @Query(value = "SELECT * FROM app_navigation_menu_item entity WHERE entity.parent_menu_id IS NULL", nativeQuery = true)
    List<AppNavigationMenuItem> findAllByParentMenuIsNull();

    @Override
    <S extends AppNavigationMenuItem> S save(S entity);

    @Override
    List<AppNavigationMenuItem> findAll();

    @Override
    Optional<AppNavigationMenuItem> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

