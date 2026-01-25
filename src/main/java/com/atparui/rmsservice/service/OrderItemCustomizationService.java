package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.OrderItemCustomization;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.OrderItemCustomizationRepository;
import com.atparui.rmsservice.service.dto.OrderItemCustomizationDTO;
import com.atparui.rmsservice.service.mapper.OrderItemCustomizationMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.OrderItemCustomization}.
 */
@Service
@Transactional
public class OrderItemCustomizationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemCustomizationService.class);

    private final OrderItemCustomizationRepository orderItemCustomizationRepository;

    private final OrderItemCustomizationMapper orderItemCustomizationMapper;

    public OrderItemCustomizationService(
        OrderItemCustomizationRepository orderItemCustomizationRepository,
        OrderItemCustomizationMapper orderItemCustomizationMapper
    ) {
        this.orderItemCustomizationRepository = orderItemCustomizationRepository;
        this.orderItemCustomizationMapper = orderItemCustomizationMapper;
    }
    @Transactional
    public Optional<OrderItemCustomizationDTO> save(OrderItemCustomizationDTO orderItemCustomizationDTO) {
        LOG.debug("Request to save OrderItemCustomization : {}", orderItemCustomizationDTO);
        OrderItemCustomization orderItemCustomization = orderItemCustomizationMapper.toEntity(orderItemCustomizationDTO);
        orderItemCustomization = orderItemCustomizationRepository.save(orderItemCustomization);
        return Optional.of(orderItemCustomizationMapper.toDto(orderItemCustomization));
    }

    @Transactional
    public Optional<OrderItemCustomizationDTO> update(OrderItemCustomizationDTO orderItemCustomizationDTO) {
        LOG.debug("Request to update OrderItemCustomization : {}", orderItemCustomizationDTO);
        OrderItemCustomization orderItemCustomization = orderItemCustomizationMapper.toEntity(orderItemCustomizationDTO);
        orderItemCustomization = orderItemCustomizationRepository.save(orderItemCustomization);
        return Optional.of(orderItemCustomizationMapper.toDto(orderItemCustomization));
    }

    @Transactional
    public Optional<OrderItemCustomizationDTO> partialUpdate(OrderItemCustomizationDTO orderItemCustomizationDTO) {
        LOG.debug("Request to partially update OrderItemCustomization : {}", orderItemCustomizationDTO);
        return orderItemCustomizationRepository
            .findById(orderItemCustomizationDTO.getId())
            .map(existingOrderItemCustomization -> {
                orderItemCustomizationMapper.partialUpdate(existingOrderItemCustomization, orderItemCustomizationDTO);
                return orderItemCustomizationRepository.save(existingOrderItemCustomization);
            })
            .map(orderItemCustomizationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<OrderItemCustomizationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrderItemCustomizations");
        Page<OrderItemCustomization> page = orderItemCustomizationRepository.findAll(pageable);
        return page.getContent().stream()
            .map(orderItemCustomizationMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return orderItemCustomizationRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<OrderItemCustomizationDTO> findOne(UUID id) {
        LOG.debug("Request to get OrderItemCustomization : {}", id);
        return orderItemCustomizationRepository.findById(id).map(orderItemCustomizationMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete OrderItemCustomization : {}", id);
        orderItemCustomizationRepository.deleteById(id);
    }
}
