package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.AppNavigationMenu;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppNavigationMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppNavigationMenuRepository extends JpaRepository<AppNavigationMenu, UUID> {
    List<AppNavigationMenu> findAllBy(Pageable pageable);

    @Override
    <S extends AppNavigationMenu> S save(S entity);

    @Override
    List<AppNavigationMenu> findAll();

    @Override
    Optional<AppNavigationMenu> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

