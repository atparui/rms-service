package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.PaymentRepository;
import com.atparui.rmsservice.service.PaymentService;
import com.atparui.rmsservice.service.dto.PaymentDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.PaginationUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/payments")
public class PaymentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentResource.class);
    private static final String ENTITY_NAME = "rmsservicePayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentResource(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Payment : {}", paymentDTO);
        if (paymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentDTO.setId(UUID.randomUUID());
        PaymentDTO result = paymentService
            .save(paymentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create payment"));

        return ResponseEntity.created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody PaymentDTO paymentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Payment : {}, {}", id, paymentDTO);
        if (paymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentDTO result = paymentService
            .update(paymentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentDTO> partialUpdatePayment(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody PaymentDTO paymentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Payment partially : {}, {}", id, paymentDTO);
        if (paymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentDTO result = paymentService
            .partialUpdate(paymentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentDTO>> getAllPayments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Payments");
        long count = paymentService.countAll();
        List<PaymentDTO> entities = paymentService.findAll(pageable);

        return ResponseEntity.ok()
            .headers(
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    new PageImpl<>(entities, pageable, count)
                )
            )
            .body(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Payment : {}", id);
        Optional<PaymentDTO> paymentDTO = paymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePayment(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Payment : {}", id);
        paymentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
