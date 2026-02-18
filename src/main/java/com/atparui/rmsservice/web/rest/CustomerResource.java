package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.CustomerRepository;
import com.atparui.rmsservice.service.CustomerService;
import com.atparui.rmsservice.service.dto.CustomerDTO;
import com.atparui.rmsservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

/**
 * REST controller for managing {@link com.atparui.rmsservice.domain.Customer}.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "rmsserviceCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    public CustomerResource(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customerDTO the customerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerDTO, or with status {@code 400 (Bad Request)} if the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerDTO.setId(UUID.randomUUID());
        CustomerDTO result = customerService
            .save(customerDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create customer"));

        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customers/:id} : Updates an existing customer.
     *
     * @param id the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Customer : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerDTO result = customerService
            .update(customerDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customers/:id} : Partial updates given fields of an existing customer, field will ignore if it is null
     *
     * @param id the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerDTO> partialUpdateCustomer(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Customer partially : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerDTO result = customerService
            .partialUpdate(customerDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @param pageable the pagination information.
     * @param request a {@link HttpServletRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        HttpServletRequest request
    ) {
        LOG.debug("REST request to get a page of Customers");
        long count = customerService.countAll();
        List<CustomerDTO> entities = customerService.findAll(pageable);

        return ResponseEntity.ok()
            .headers(
                PaginationUtil.generatePaginationHttpHeaders(
                    ServletUriComponentsBuilder.fromRequest(request),
                    new PageImpl<>(entities, pageable, count)
                )
            )
            .body(entities);
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Customer : {}", id);
        Optional<CustomerDTO> customerDTO = customerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDTO);
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /customers/_search?query=:query} : search for the customer corresponding
     * to the query.
     *
     * @param query the query of the customer search.
     * @param pageable the pagination information.
     * @param request a {@link HttpServletRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        HttpServletRequest request
    ) {
        LOG.debug("REST request to search for a page of Customers for query {}", query);
        // Search functionality removed (Elasticsearch was removed)
        // Return empty list with proper pagination headers
        List<CustomerDTO> results = new ArrayList<>();
        PageImpl<CustomerDTO> page = new PageImpl<>(results, pageable, 0);

        return ResponseEntity.ok()
            .headers(
                PaginationUtil.generatePaginationHttpHeaders(
                    ServletUriComponentsBuilder.fromRequest(request),
                    page
                )
            )
            .body(results);
    }

    // jhipster-needle-rest-add-get-method - JHipster will add get methods here

    /**
     * {@code GET /api/customers/{id}/orders} : Get customer orders
     *
     * @param id the id of the customer
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of orders
     */
    // TODO: Implement getCustomerOrders method in CustomerService first
    // @GetMapping("/{id}/orders")
    // public ResponseEntity<List<com.atparui.rmsservice.service.dto.OrderDTO>> getCustomerOrders(@PathVariable UUID id) {
    //     LOG.debug("REST request to get customer orders : {}", id);
    //     List<com.atparui.rmsservice.service.dto.OrderDTO> result = customerService.getCustomerOrders(id);
    //     return ResponseEntity.ok().body(result);
    // }

    /**
     * {@code GET /api/customers/{id}/loyalty} : Get customer loyalty
     *
     * @param id the id of the customer
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and loyalty DTO
     */
    // TODO: Implement getCustomerLoyalty method in CustomerService first
    // @GetMapping("/{id}/loyalty")
    // public ResponseEntity<com.atparui.rmsservice.service.dto.CustomerLoyaltyDTO> getCustomerLoyalty(@PathVariable UUID id) {
    //     LOG.debug("REST request to get customer loyalty : {}", id);
    //     Optional<com.atparui.rmsservice.service.dto.CustomerLoyaltyDTO> result = customerService.getCustomerLoyalty(id);
    //     return result
    //         .map(dto -> ResponseEntity.ok().body(dto))
    //         .orElse(ResponseEntity.notFound().build());
    // }

    // jhipster-needle-rest-add-post-method - JHipster will add post methods here

    /**
     * {@code POST /api/customers/{id}/loyalty/add-points} : Add loyalty points
     *
     * @param id the id of the customer
     * @param request the points addition request
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and updated loyalty DTO
     */
    // TODO: Implement addLoyaltyPoints method in CustomerService first
    // @PostMapping("/{id}/loyalty/add-points")
    // public ResponseEntity<com.atparui.rmsservice.service.dto.CustomerLoyaltyDTO> addLoyaltyPoints(
    //     @PathVariable UUID id,
    //     @Valid @RequestBody com.atparui.rmsservice.service.dto.LoyaltyPointsRequestDTO request
    // ) {
    //     LOG.debug("REST request to add loyalty points : {} - {}", id, request);
    //     com.atparui.rmsservice.service.dto.CustomerLoyaltyDTO result = customerService.addLoyaltyPoints(id, request);
    //     return ResponseEntity.ok().body(result);
    // }
}
