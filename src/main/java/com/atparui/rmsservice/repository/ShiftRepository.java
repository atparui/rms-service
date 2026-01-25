package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Shift;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Shift entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM shift entity WHERE entity.branch_id = :id", nativeQuery = true)
    List<Shift> findByBranch(UUID id);

    @Query(value = "SELECT * FROM shift entity WHERE entity.branch_id IS NULL", nativeQuery = true)
    List<Shift> findAllByBranchIsNull();

    @Override
    <S extends Shift> S save(S entity);

    @Override
    List<Shift> findAll();

    @Override
    Optional<Shift> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

