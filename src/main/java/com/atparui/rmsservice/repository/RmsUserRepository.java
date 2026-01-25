package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.RmsUser;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RmsUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RmsUserRepository extends JpaRepository<RmsUser, UUID> {
    List<RmsUser> findAllBy(Pageable pageable);

    Optional<RmsUser> findByExternalUserId(String externalUserId);

    @Override
    <S extends RmsUser> S save(S entity);

    @Override
    List<RmsUser> findAll();

    @Override
    Optional<RmsUser> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

