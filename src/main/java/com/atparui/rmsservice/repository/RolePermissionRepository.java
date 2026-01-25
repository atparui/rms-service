package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.RolePermission;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RolePermission entity.
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {
    org.springframework.data.domain.Page<RolePermission> findAll(Pageable pageable);
    List<RolePermission> findByRoleIn(Collection<String> roles);
}
