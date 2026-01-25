package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.MenuPermission;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuPermission entity.
 */
@Repository
public interface MenuPermissionRepository extends JpaRepository<MenuPermission, UUID> {
    org.springframework.data.domain.Page<MenuPermission> findAll(Pageable pageable);
    List<MenuPermission> findByAppMenuIdIn(Collection<UUID> menuIds);
}
