package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.UserBranchRole;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserBranchRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserBranchRoleRepository extends JpaRepository<UserBranchRole, UUID> {
    List<UserBranchRole> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM user_branch_role entity WHERE entity.user_id = :id", nativeQuery = true)
    List<UserBranchRole> findByUser(UUID id);

    @Query(value = "SELECT * FROM user_branch_role entity WHERE entity.user_id IS NULL", nativeQuery = true)
    List<UserBranchRole> findAllByUserIsNull();

    @Query(value = "SELECT * FROM user_branch_role entity WHERE entity.branch_id = :id", nativeQuery = true)
    List<UserBranchRole> findByBranch(UUID id);

    @Query(value = "SELECT * FROM user_branch_role entity WHERE entity.branch_id IS NULL", nativeQuery = true)
    List<UserBranchRole> findAllByBranchIsNull();

    @Query(value = "SELECT * FROM user_branch_role entity WHERE entity.branch_id = :branchId AND entity.role = :role AND entity.is_active = true", nativeQuery = true)
    List<UserBranchRole> findByBranchIdAndRole(UUID branchId, String role);

    @Override
    <S extends UserBranchRole> S save(S entity);

    @Override
    List<UserBranchRole> findAll();

    @Override
    Optional<UserBranchRole> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

