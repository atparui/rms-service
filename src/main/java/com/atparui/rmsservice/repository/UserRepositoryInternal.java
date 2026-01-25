package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Internal repository interface for User operations that require custom database access.
 * This interface is public to allow access from test classes.
 */
public interface UserRepositoryInternal {
    void resetUserAuthorityMappings();
    void deleteUserAuthorities(String userId);
    void saveUserAuthority(String userId, String authority);
    Optional<User> findOneWithAuthoritiesByLogin(String login);
    User create(User user);
    Page<User> findAllWithAuthorities(Pageable pageable);
}
