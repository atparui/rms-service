package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.AppNavigationMenuRole;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppNavigationMenuRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppNavigationMenuRoleRepository
    extends JpaRepository<AppNavigationMenuRole, UUID> {
    List<AppNavigationMenuRole> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM app_navigation_menu_role entity WHERE entity.app_navigation_menu_id = :id", nativeQuery = true)
    List<AppNavigationMenuRole> findByAppNavigationMenu(UUID id);

    @Query(value = "SELECT * FROM app_navigation_menu_role entity WHERE entity.app_navigation_menu_id IS NULL", nativeQuery = true)
    List<AppNavigationMenuRole> findAllByAppNavigationMenuIsNull();

    @Query(value = "SELECT * FROM app_navigation_menu_role entity WHERE entity.app_navigation_menu_item_id = :id", nativeQuery = true)
    List<AppNavigationMenuRole> findByAppNavigationMenuItem(UUID id);

    @Query(value = "SELECT * FROM app_navigation_menu_role entity WHERE entity.app_navigation_menu_item_id IS NULL", nativeQuery = true)
    List<AppNavigationMenuRole> findAllByAppNavigationMenuItemIsNull();

    @Query(value = "SELECT * FROM app_navigation_menu_role entity WHERE entity.role = :role AND entity.is_active = true", nativeQuery = true)
    List<AppNavigationMenuRole> findByRole(String role);

    @Query(
        value = "SELECT * FROM app_navigation_menu_role entity WHERE entity.app_navigation_menu_id = :menuId AND entity.role = :role AND entity.is_active = true",
        nativeQuery = true
    )
    Optional<AppNavigationMenuRole> findByMenuIdAndRole(UUID menuId, String role);

    @Query(
        value = "SELECT * FROM app_navigation_menu_role entity WHERE entity.app_navigation_menu_item_id = :itemId AND entity.role = :role AND entity.is_active = true",
        nativeQuery = true
    )
    Optional<AppNavigationMenuRole> findByMenuItemIdAndRole(UUID itemId, String role);

    @Override
    <S extends AppNavigationMenuRole> S save(S entity);

    @Override
    List<AppNavigationMenuRole> findAll();

    @Override
    Optional<AppNavigationMenuRole> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

