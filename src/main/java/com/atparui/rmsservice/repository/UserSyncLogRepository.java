package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.UserSyncLog;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSyncLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSyncLogRepository extends JpaRepository<UserSyncLog, UUID> {
    @Query(value = "SELECT * FROM user_sync_log entity WHERE entity.user_id = :id", nativeQuery = true)
    List<UserSyncLog> findByUser(UUID id);

    @Query(value = "SELECT * FROM user_sync_log entity WHERE entity.user_id IS NULL", nativeQuery = true)
    List<UserSyncLog> findAllByUserIsNull();

    @Override
    <S extends UserSyncLog> S save(S entity);

    @Override
    List<UserSyncLog> findAll();

    @Override
    Optional<UserSyncLog> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

