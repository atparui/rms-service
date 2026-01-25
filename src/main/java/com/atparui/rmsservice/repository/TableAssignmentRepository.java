package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.TableAssignment;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TableAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TableAssignmentRepository extends JpaRepository<TableAssignment, UUID> {
    @Query(value = "SELECT * FROM table_assignment entity WHERE entity.branch_table_id = :id", nativeQuery = true)
    List<TableAssignment> findByBranchTable(UUID id);

    @Query(value = "SELECT * FROM table_assignment entity WHERE entity.branch_table_id IS NULL", nativeQuery = true)
    List<TableAssignment> findAllByBranchTableIsNull();

    @Query(value = "SELECT * FROM table_assignment entity WHERE entity.shift_id = :id", nativeQuery = true)
    List<TableAssignment> findByShift(UUID id);

    @Query(value = "SELECT * FROM table_assignment entity WHERE entity.shift_id IS NULL", nativeQuery = true)
    List<TableAssignment> findAllByShiftIsNull();

    @Query(value = "SELECT * FROM table_assignment entity WHERE entity.supervisor_id = :id", nativeQuery = true)
    List<TableAssignment> findBySupervisor(UUID id);

    @Query(value = "SELECT * FROM table_assignment entity WHERE entity.supervisor_id IS NULL", nativeQuery = true)
    List<TableAssignment> findAllBySupervisorIsNull();

    @Query(value = "SELECT * FROM table_assignment entity WHERE entity.assignment_date = :date", nativeQuery = true)
    List<TableAssignment> findByAssignmentDate(java.time.LocalDate date);

    @Override
    <S extends TableAssignment> S save(S entity);

    @Override
    List<TableAssignment> findAll();

    @Override
    Optional<TableAssignment> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

