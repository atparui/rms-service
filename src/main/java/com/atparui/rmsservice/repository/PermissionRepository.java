package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Permission;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data R2DBC repository for the Permission entity.
 */
@Repository
public interface PermissionRepository extends R2dbcRepository<Permission, UUID> {
    Flux<Permission> findAllBy(Pageable pageable);

    @Query("SELECT * FROM permission p WHERE p.code IN (:codes)")
    Flux<Permission> findByCodeIn(Collection<String> codes);
}
