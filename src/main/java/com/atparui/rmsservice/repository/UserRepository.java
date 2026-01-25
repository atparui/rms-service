package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.User;
import java.util.Optional;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
@JaversSpringDataAuditable
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryInternal {
    Optional<User> findOneByLogin(String login);

    Page<User> findAllByIdNotNull(Pageable pageable);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    long count();
}

