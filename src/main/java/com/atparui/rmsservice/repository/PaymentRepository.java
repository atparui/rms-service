package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.Payment;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findAllBy(Pageable pageable);

    @Query(value = "SELECT * FROM payment entity WHERE entity.bill_id = :id", nativeQuery = true)
    List<Payment> findByBill(UUID id);

    @Query(value = "SELECT * FROM payment entity WHERE entity.bill_id IS NULL", nativeQuery = true)
    List<Payment> findAllByBillIsNull();

    @Query(value = "SELECT * FROM payment entity WHERE entity.payment_method_id = :id", nativeQuery = true)
    List<Payment> findByPaymentMethod(UUID id);

    @Query(value = "SELECT * FROM payment entity WHERE entity.payment_method_id IS NULL", nativeQuery = true)
    List<Payment> findAllByPaymentMethodIsNull();

    @Query(value = "SELECT * FROM payment entity WHERE entity.bill_id = :billId", nativeQuery = true)
    List<Payment> findByBillId(UUID billId);

    @Override
    <S extends Payment> S save(S entity);

    @Override
    List<Payment> findAll();

    @Override
    Optional<Payment> findById(UUID id);

    @Override
    void deleteById(UUID id);
}

