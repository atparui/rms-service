package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Permission;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Permission entity.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    org.springframework.data.domain.Page<Permission> findAll(Pageable pageable);
    List<Permission> findByCodeIn(Collection<String> codes);
}
