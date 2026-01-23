package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.RolePermission;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data R2DBC repository for the RolePermission entity.
 */
@Repository
public interface RolePermissionRepository extends R2dbcRepository<RolePermission, UUID> {
    Flux<RolePermission> findAllBy(Pageable pageable);

    @Query("SELECT * FROM role_permission rp WHERE rp.role IN (:roles)")
    Flux<RolePermission> findByRoleIn(Collection<String> roles);
}
