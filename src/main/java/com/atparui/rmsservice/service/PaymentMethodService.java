package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.PaymentMethod;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.PaymentMethodRepository;
import com.atparui.rmsservice.service.dto.PaymentMethodDTO;
import com.atparui.rmsservice.service.mapper.PaymentMethodMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.PaymentMethod}.
 */
@Service
@Transactional
public class PaymentMethodService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMethodService.class);

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }
    @Transactional
    public Optional<PaymentMethodDTO> save(PaymentMethodDTO paymentMethodDTO) {
        LOG.debug("Request to save PaymentMethod : {}", paymentMethodDTO);
        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        return Optional.of(paymentMethodMapper.toDto(paymentMethod));
    }

    @Transactional
    public Optional<PaymentMethodDTO> update(PaymentMethodDTO paymentMethodDTO) {
        LOG.debug("Request to update PaymentMethod : {}", paymentMethodDTO);
        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        return Optional.of(paymentMethodMapper.toDto(paymentMethod));
    }

    @Transactional
    public Optional<PaymentMethodDTO> partialUpdate(PaymentMethodDTO paymentMethodDTO) {
        LOG.debug("Request to partially update PaymentMethod : {}", paymentMethodDTO);
        return paymentMethodRepository
            .findById(paymentMethodDTO.getId())
            .map(existingPaymentMethod -> {
                paymentMethodMapper.partialUpdate(existingPaymentMethod, paymentMethodDTO);
                return paymentMethodRepository.save(existingPaymentMethod);
            })
            .map(paymentMethodMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PaymentMethods");
        Page<PaymentMethod> page = paymentMethodRepository.findAll(pageable);
        return page.getContent().stream()
            .map(paymentMethodMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return paymentMethodRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<PaymentMethodDTO> findOne(UUID id) {
        LOG.debug("Request to get PaymentMethod : {}", id);
        return paymentMethodRepository.findById(id).map(paymentMethodMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete PaymentMethod : {}", id);
        paymentMethodRepository.deleteById(id);
    }
}
