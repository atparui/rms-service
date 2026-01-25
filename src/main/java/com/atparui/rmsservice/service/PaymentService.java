package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.Payment;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.PaymentRepository;
import com.atparui.rmsservice.service.dto.PartialPaymentRequestDTO;
import com.atparui.rmsservice.service.dto.PaymentDTO;
import com.atparui.rmsservice.service.dto.PaymentRequestDTO;
import com.atparui.rmsservice.service.dto.PaymentSummaryDTO;
import com.atparui.rmsservice.service.dto.RefundRequestDTO;
import com.atparui.rmsservice.service.mapper.PaymentMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;
    public PaymentService(
        PaymentRepository paymentRepository,
        PaymentMapper paymentMapper
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;    }
    @Transactional
    public Optional<PaymentDTO> save(PaymentDTO paymentDTO) {
        LOG.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return Optional.of(paymentMapper.toDto(payment));
    }

    @Transactional
    public Optional<PaymentDTO> update(PaymentDTO paymentDTO) {
        LOG.debug("Request to update Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return Optional.of(paymentMapper.toDto(payment));
    }

    @Transactional
    public Optional<PaymentDTO> partialUpdate(PaymentDTO paymentDTO) {
        LOG.debug("Request to partially update Payment : {}", paymentDTO);
        return paymentRepository
            .findById(paymentDTO.getId())
            .map(existingPayment -> {
                paymentMapper.partialUpdate(existingPayment, paymentDTO);
                return paymentRepository.save(existingPayment);
            })
            .map(paymentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Payments");
        Page<Payment> page = paymentRepository.findAll(pageable);
        return page.getContent().stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return paymentRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(UUID id) {
        LOG.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id).map(paymentMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }
}
