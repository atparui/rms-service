package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.MenuPermission;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data R2DBC repository for the MenuPermission entity.
 */
@Repository
public interface MenuPermissionRepository extends R2dbcRepository<MenuPermission, UUID> {
    Flux<MenuPermission> findAllBy(Pageable pageable);

    @Query("SELECT * FROM menu_permission mp WHERE mp.app_menu_id IN (:menuIds)")
    Flux<MenuPermission> findByAppMenuIdIn(Collection<UUID> menuIds);
}
