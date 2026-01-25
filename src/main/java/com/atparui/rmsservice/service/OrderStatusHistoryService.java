package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.OrderStatusHistory;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.OrderStatusHistoryRepository;
import com.atparui.rmsservice.service.dto.OrderStatusHistoryDTO;
import com.atparui.rmsservice.service.mapper.OrderStatusHistoryMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.OrderStatusHistory}.
 */
@Service
@Transactional
public class OrderStatusHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderStatusHistoryService.class);

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    private final OrderStatusHistoryMapper orderStatusHistoryMapper;

    public OrderStatusHistoryService(
        OrderStatusHistoryRepository orderStatusHistoryRepository,
        OrderStatusHistoryMapper orderStatusHistoryMapper
    ) {
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
        this.orderStatusHistoryMapper = orderStatusHistoryMapper;
    }
    @Transactional
    public Optional<OrderStatusHistoryDTO> save(OrderStatusHistoryDTO orderStatusHistoryDTO) {
        LOG.debug("Request to save OrderStatusHistory : {}", orderStatusHistoryDTO);
        OrderStatusHistory orderStatusHistory = orderStatusHistoryMapper.toEntity(orderStatusHistoryDTO);
        orderStatusHistory = orderStatusHistoryRepository.save(orderStatusHistory);
        return Optional.of(orderStatusHistoryMapper.toDto(orderStatusHistory));
    }

    @Transactional
    public Optional<OrderStatusHistoryDTO> update(OrderStatusHistoryDTO orderStatusHistoryDTO) {
        LOG.debug("Request to update OrderStatusHistory : {}", orderStatusHistoryDTO);
        OrderStatusHistory orderStatusHistory = orderStatusHistoryMapper.toEntity(orderStatusHistoryDTO);
        orderStatusHistory = orderStatusHistoryRepository.save(orderStatusHistory);
        return Optional.of(orderStatusHistoryMapper.toDto(orderStatusHistory));
    }

    @Transactional
    public Optional<OrderStatusHistoryDTO> partialUpdate(OrderStatusHistoryDTO orderStatusHistoryDTO) {
        LOG.debug("Request to partially update OrderStatusHistory : {}", orderStatusHistoryDTO);
        return orderStatusHistoryRepository
            .findById(orderStatusHistoryDTO.getId())
            .map(existingOrderStatusHistory -> {
                orderStatusHistoryMapper.partialUpdate(existingOrderStatusHistory, orderStatusHistoryDTO);
                return orderStatusHistoryRepository.save(existingOrderStatusHistory);
            })
            .map(orderStatusHistoryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<OrderStatusHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrderStatusHistorys");
        Page<OrderStatusHistory> page = orderStatusHistoryRepository.findAll(pageable);
        return page.getContent().stream()
            .map(orderStatusHistoryMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return orderStatusHistoryRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<OrderStatusHistoryDTO> findOne(UUID id) {
        LOG.debug("Request to get OrderStatusHistory : {}", id);
        return orderStatusHistoryRepository.findById(id).map(orderStatusHistoryMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete OrderStatusHistory : {}", id);
        orderStatusHistoryRepository.deleteById(id);
    }
}
