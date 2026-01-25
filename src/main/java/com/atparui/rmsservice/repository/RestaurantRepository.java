package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Restaurant;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Restaurant entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    List<Restaurant> findAllBy(Pageable pageable);

    @Override
    <S extends Restaurant> S save(S entity);

    @Override
    List<Restaurant> findAll();

    @Override
    Optional<Restaurant> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

