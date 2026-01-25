package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.AppMenu;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppMenu entity.
 */
@Repository
public interface AppMenuRepository extends JpaRepository<AppMenu, UUID> {
    org.springframework.data.domain.Page<AppMenu> findAll(Pageable pageable);
    java.util.List<AppMenu> findByIsActiveTrue();
}
