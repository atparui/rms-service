package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.CustomerLoyaltyRepository;
import com.atparui.rmsservice.service.CustomerLoyaltyService;
import com.atparui.rmsservice.service.dto.CustomerLoyaltyDTO;
import com.atparui.rmsservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.atparui.rmsservice.domain.CustomerLoyalty}.
 */
@RestController
@RequestMapping("/api/customer-loyalties")
public class CustomerLoyaltyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerLoyaltyResource.class);
    private static final String ENTITY_NAME = "rmsserviceCustomerLoyalty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerLoyaltyService customerLoyaltyService;
    private final CustomerLoyaltyRepository customerLoyaltyRepository;

    public CustomerLoyaltyResource(CustomerLoyaltyService customerLoyaltyService, CustomerLoyaltyRepository customerLoyaltyRepository) {
        this.customerLoyaltyService = customerLoyaltyService;
        this.customerLoyaltyRepository = customerLoyaltyRepository;
    }

    @PostMapping("")
    public ResponseEntity<CustomerLoyaltyDTO> createCustomerLoyalty(@Valid @RequestBody CustomerLoyaltyDTO customerLoyaltyDTO) throws URISyntaxException {
        LOG.debug("REST request to save CustomerLoyalty : {}", customerLoyaltyDTO);
        if (customerLoyaltyDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerLoyalty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerLoyaltyDTO.setId(UUID.randomUUID());
        CustomerLoyaltyDTO result = customerLoyaltyService
            .save(customerLoyaltyDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create customer loyalty"));

        return ResponseEntity.created(new URI("/api/customer-loyalties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerLoyaltyDTO> updateCustomerLoyalty(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CustomerLoyaltyDTO customerLoyaltyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerLoyalty : {}, {}", id, customerLoyaltyDTO);
        if (customerLoyaltyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerLoyaltyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerLoyaltyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerLoyaltyDTO result = customerLoyaltyService
            .update(customerLoyaltyDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerLoyaltyDTO> partialUpdateCustomerLoyalty(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CustomerLoyaltyDTO customerLoyaltyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerLoyalty partially : {}, {}", id, customerLoyaltyDTO);
        if (customerLoyaltyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerLoyaltyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerLoyaltyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerLoyaltyDTO result = customerLoyaltyService
            .partialUpdate(customerLoyaltyDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<CustomerLoyaltyDTO>> getAllCustomerLoyalties(
        @org.springdoc.core.annotations.ParameterObject org.springframework.data.domain.Pageable pageable
    ) {
        LOG.debug("REST request to get all CustomerLoyalties");
        List<CustomerLoyaltyDTO> customerLoyalties = customerLoyaltyService.findAll(pageable);
        return ResponseEntity.ok().body(customerLoyalties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerLoyaltyDTO> getCustomerLoyalty(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get CustomerLoyalty : {}", id);
        Optional<CustomerLoyaltyDTO> customerLoyaltyDTO = customerLoyaltyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerLoyaltyDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomerLoyalty(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete CustomerLoyalty : {}", id);
        customerLoyaltyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
