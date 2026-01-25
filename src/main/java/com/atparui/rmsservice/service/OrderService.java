package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.domain.Order;
import com.atparui.rmsservice.repository.OrderItemRepository;
import com.atparui.rmsservice.repository.OrderRepository;
import com.atparui.rmsservice.service.dto.OrderCancellationRequestDTO;
import com.atparui.rmsservice.service.dto.OrderCreationRequestDTO;
import com.atparui.rmsservice.service.dto.OrderDTO;
import com.atparui.rmsservice.service.dto.OrderItemDTO;
import com.atparui.rmsservice.service.dto.OrderStatusUpdateRequestDTO;
import com.atparui.rmsservice.service.dto.OrderWithItemsDTO;
import com.atparui.rmsservice.service.mapper.OrderItemMapper;
import com.atparui.rmsservice.service.mapper.OrderMapper;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Order}.
 */
@Service
@Transactional
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;

    public OrderService(
        OrderRepository orderRepository,
        OrderMapper orderMapper,
        OrderItemRepository orderItemRepository,
        OrderItemMapper orderItemMapper
    ) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }
    @Transactional
    public Optional<OrderDTO> save(OrderDTO orderDTO) {
        LOG.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return Optional.of(orderMapper.toDto(order));
    }

    @Transactional
    public Optional<OrderDTO> update(OrderDTO orderDTO) {
        LOG.debug("Request to update Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return Optional.of(orderMapper.toDto(order));
    }

    @Transactional
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        LOG.debug("Request to partially update Order : {}", orderDTO);
        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);
                return orderRepository.save(existingOrder);
            })
            .map(orderMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Orders");
        Page<Order> page = orderRepository.findAll(pageable);
        return page.getContent().stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return orderRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(UUID id) {
        LOG.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }
}
