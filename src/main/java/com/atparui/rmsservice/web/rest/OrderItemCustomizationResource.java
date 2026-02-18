package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.OrderItemCustomizationRepository;
import com.atparui.rmsservice.service.OrderItemCustomizationService;
import com.atparui.rmsservice.service.dto.OrderItemCustomizationDTO;
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
@RequestMapping("/api/order-item-customizations")
public class OrderItemCustomizationResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemCustomizationResource.class);
    private static final String ENTITY_NAME = "rmsserviceOrderItemCustomization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderItemCustomizationService orderItemCustomizationService;
    private final OrderItemCustomizationRepository orderItemCustomizationRepository;

    public OrderItemCustomizationResource(OrderItemCustomizationService orderItemCustomizationService, OrderItemCustomizationRepository orderItemCustomizationRepository) {
        this.orderItemCustomizationService = orderItemCustomizationService;
        this.orderItemCustomizationRepository = orderItemCustomizationRepository;
    }

    @PostMapping("")
    public ResponseEntity<OrderItemCustomizationDTO> createOrderItemCustomization(@Valid @RequestBody OrderItemCustomizationDTO orderItemCustomizationDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderItemCustomization : {}", orderItemCustomizationDTO);
        if (orderItemCustomizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderItemCustomization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderItemCustomizationDTO.setId(UUID.randomUUID());
        OrderItemCustomizationDTO result = orderItemCustomizationService
            .save(orderItemCustomizationDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create order item customization"));

        return ResponseEntity.created(new URI("/api/order-item-customizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemCustomizationDTO> updateOrderItemCustomization(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody OrderItemCustomizationDTO orderItemCustomizationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderItemCustomization : {}, {}", id, orderItemCustomizationDTO);
        if (orderItemCustomizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemCustomizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemCustomizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderItemCustomizationDTO result = orderItemCustomizationService
            .update(orderItemCustomizationDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderItemCustomizationDTO> partialUpdateOrderItemCustomization(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody OrderItemCustomizationDTO orderItemCustomizationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderItemCustomization partially : {}, {}", id, orderItemCustomizationDTO);
        if (orderItemCustomizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemCustomizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemCustomizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderItemCustomizationDTO result = orderItemCustomizationService
            .partialUpdate(orderItemCustomizationDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderItemCustomizationDTO>> getAllOrderItemCustomizations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        HttpServletRequest request
    ) {
        LOG.debug("REST request to get a page of OrderItemCustomizations");
        long count = orderItemCustomizationService.countAll();
        List<OrderItemCustomizationDTO> entities = orderItemCustomizationService.findAll(pageable);

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
    public ResponseEntity<OrderItemCustomizationDTO> getOrderItemCustomization(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get OrderItemCustomization : {}", id);
        Optional<OrderItemCustomizationDTO> orderItemCustomizationDTO = orderItemCustomizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItemCustomizationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrderItemCustomization(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete OrderItemCustomization : {}", id);
        orderItemCustomizationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
