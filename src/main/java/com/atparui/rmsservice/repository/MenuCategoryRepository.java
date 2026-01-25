package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.MenuCategory;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, UUID> {
    List<MenuCategory> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM menu_category entity WHERE entity.restaurant_id = :id", nativeQuery = true)
    List<MenuCategory> findByRestaurant(UUID id);

    @Query(value = "SELECT * FROM menu_category entity WHERE entity.restaurant_id IS NULL", nativeQuery = true)
    List<MenuCategory> findAllByRestaurantIsNull();

    @Override
    <S extends MenuCategory> S save(S entity);

    @Override
    List<MenuCategory> findAll();

    @Override
    Optional<MenuCategory> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

