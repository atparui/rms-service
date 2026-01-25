package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Authority;
import com.atparui.rmsservice.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of custom UserRepository methods using JPA.
 */
@Repository
public class UserRepositoryInternalImpl implements UserRepositoryInternal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void resetUserAuthorityMappings() {
        entityManager.createNativeQuery("DELETE FROM jhi_user_authority").executeUpdate();
    }

    @Override
    @Transactional
    public void deleteUserAuthorities(String userId) {
        entityManager
            .createNativeQuery("DELETE FROM jhi_user_authority WHERE user_id = :userId")
            .setParameter("userId", userId)
            .executeUpdate();
    }

    @Override
    @Transactional
    public void saveUserAuthority(String userId, String authority) {
        entityManager
            .createNativeQuery("INSERT INTO jhi_user_authority (user_id, authority_name) VALUES (:userId, :authority)")
            .setParameter("userId", userId)
            .setParameter("authority", authority)
            .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findOneWithAuthoritiesByLogin(String login) {
        List<User> users = entityManager
            .createQuery(
                "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.authorities WHERE LOWER(u.login) = LOWER(:login)",
                User.class
            )
            .setParameter("login", login)
            .getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    @Transactional
    public User create(User user) {
        entityManager.persist(user);
        entityManager.flush();
        
        // Save authorities
        if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
            for (Authority authority : user.getAuthorities()) {
                saveUserAuthority(user.getId(), authority.getName());
            }
        }
        
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllWithAuthorities(Pageable pageable) {
        // Get total count
        Long total = entityManager
            .createQuery("SELECT COUNT(DISTINCT u) FROM User u", Long.class)
            .getSingleResult();

        // Get page of users with authorities
        List<User> users = entityManager
            .createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.authorities", User.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        return new PageImpl<>(users, pageable, total);
    }
}
