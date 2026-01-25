package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.TableWaiterAssignment;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TableWaiterAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TableWaiterAssignmentRepository
    extends JpaRepository<TableWaiterAssignment, UUID> {
    @Query(value = "SELECT * FROM table_waiter_assignment entity WHERE entity.table_assignment_id = :id", nativeQuery = true)
    List<TableWaiterAssignment> findByTableAssignment(UUID id);

    @Query(value = "SELECT * FROM table_waiter_assignment entity WHERE entity.table_assignment_id IS NULL", nativeQuery = true)
    List<TableWaiterAssignment> findAllByTableAssignmentIsNull();

    @Query(value = "SELECT * FROM table_waiter_assignment entity WHERE entity.waiter_id = :id", nativeQuery = true)
    List<TableWaiterAssignment> findByWaiter(UUID id);

    @Query(value = "SELECT * FROM table_waiter_assignment entity WHERE entity.waiter_id IS NULL", nativeQuery = true)
    List<TableWaiterAssignment> findAllByWaiterIsNull();

    @Override
    <S extends TableWaiterAssignment> S save(S entity);

    @Override
    List<TableWaiterAssignment> findAll();

    @Override
    Optional<TableWaiterAssignment> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

