package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.PaymentMethod;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    @Override
    <S extends PaymentMethod> S save(S entity);

    @Override
    List<PaymentMethod> findAll();

    @Override
    Optional<PaymentMethod> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

