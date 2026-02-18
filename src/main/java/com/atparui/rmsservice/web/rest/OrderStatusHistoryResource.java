package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.OrderStatusHistoryRepository;
import com.atparui.rmsservice.service.OrderStatusHistoryService;
import com.atparui.rmsservice.service.dto.OrderStatusHistoryDTO;
import com.atparui.rmsservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.PaginationUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/order-status-histories")
public class OrderStatusHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderStatusHistoryResource.class);
    private static final String ENTITY_NAME = "rmsserviceOrderStatusHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderStatusHistoryService orderStatusHistoryService;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderStatusHistoryResource(OrderStatusHistoryService orderStatusHistoryService, OrderStatusHistoryRepository orderStatusHistoryRepository) {
        this.orderStatusHistoryService = orderStatusHistoryService;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    @PostMapping("")
    public ResponseEntity<OrderStatusHistoryDTO> createOrderStatusHistory(@Valid @RequestBody OrderStatusHistoryDTO orderStatusHistoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderStatusHistory : {}", orderStatusHistoryDTO);
        if (orderStatusHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderStatusHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderStatusHistoryDTO.setId(UUID.randomUUID());
        OrderStatusHistoryDTO result = orderStatusHistoryService
            .save(orderStatusHistoryDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create order status history"));

        return ResponseEntity.created(new URI("/api/order-status-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderStatusHistoryDTO> updateOrderStatusHistory(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody OrderStatusHistoryDTO orderStatusHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderStatusHistory : {}, {}", id, orderStatusHistoryDTO);
        if (orderStatusHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderStatusHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderStatusHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderStatusHistoryDTO result = orderStatusHistoryService
            .update(orderStatusHistoryDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderStatusHistoryDTO> partialUpdateOrderStatusHistory(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody OrderStatusHistoryDTO orderStatusHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderStatusHistory partially : {}, {}", id, orderStatusHistoryDTO);
        if (orderStatusHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderStatusHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderStatusHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderStatusHistoryDTO result = orderStatusHistoryService
            .partialUpdate(orderStatusHistoryDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderStatusHistoryDTO>> getAllOrderStatusHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        HttpServletRequest request
    ) {
        LOG.debug("REST request to get a page of OrderStatusHistories");
        long count = orderStatusHistoryService.countAll();
        List<OrderStatusHistoryDTO> entities = orderStatusHistoryService.findAll(pageable);

        return ResponseEntity.ok()
            .headers(
                PaginationUtil.generatePaginationHttpHeaders(
                    ServletUriComponentsBuilder.fromRequest(request),
                    new PageImpl<>(entities, pageable, count)
                )
            )
            .body(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusHistoryDTO> getOrderStatusHistory(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get OrderStatusHistory : {}", id);
        Optional<OrderStatusHistoryDTO> orderStatusHistoryDTO = orderStatusHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderStatusHistoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrderStatusHistory(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete OrderStatusHistory : {}", id);
        orderStatusHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
