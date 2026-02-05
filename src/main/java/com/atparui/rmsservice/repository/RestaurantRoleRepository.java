package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.RestaurantRole;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RestaurantRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantRoleRepository extends JpaRepository<RestaurantRole, String> {
    /**
     * Find all active restaurant roles ordered by sort order.
     */
    List<RestaurantRole> findAllByIsActiveTrueOrderBySortOrder();

    /**
     * Find all roles (active and inactive) ordered by sort order.
     */
    List<RestaurantRole> findAllByOrderBySortOrder();
}
