package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.BranchTable;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BranchTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchTableRepository extends JpaRepository<BranchTable, UUID> {
    List<BranchTable> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM branch_table entity WHERE entity.branch_id = :id", nativeQuery = true)
    List<BranchTable> findByBranch(UUID id);

    @Query(value = "SELECT * FROM branch_table entity WHERE entity.branch_id IS NULL", nativeQuery = true)
    List<BranchTable> findAllByBranchIsNull();

    @Query(value = "SELECT * FROM branch_table entity WHERE entity.branch_id = :branchId AND entity.status = 'AVAILABLE'", nativeQuery = true)
    List<BranchTable> findAvailableByBranchId(UUID branchId);

    @Query(value = "SELECT * FROM branch_table entity WHERE entity.branch_id = :branchId AND entity.status = :status", nativeQuery = true)
    List<BranchTable> findByBranchIdAndStatus(UUID branchId, String status);

    @Override
    <S extends BranchTable> S save(S entity);

    @Override
    List<BranchTable> findAll();

    @Override
    Optional<BranchTable> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

