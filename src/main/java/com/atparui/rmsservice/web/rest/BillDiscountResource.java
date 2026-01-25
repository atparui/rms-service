package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.BillDiscountRepository;
import com.atparui.rmsservice.service.BillDiscountService;
import com.atparui.rmsservice.service.dto.BillDiscountDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.atparui.rmsservice.domain.BillDiscount}.
 */
@RestController
@RequestMapping("/api/bill-discounts")
public class BillDiscountResource {

    private static final Logger LOG = LoggerFactory.getLogger(BillDiscountResource.class);

    private static final String ENTITY_NAME = "rmsserviceBillDiscount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillDiscountService billDiscountService;

    private final BillDiscountRepository billDiscountRepository;

    public BillDiscountResource(BillDiscountService billDiscountService, BillDiscountRepository billDiscountRepository) {
        this.billDiscountService = billDiscountService;
        this.billDiscountRepository = billDiscountRepository;
    }

    /**
     * {@code POST  /bill-discounts} : Create a new billDiscount.
     *
     * @param billDiscountDTO the billDiscountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billDiscountDTO, or with status {@code 400 (Bad Request)} if the billDiscount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillDiscountDTO> createBillDiscount(@Valid @RequestBody BillDiscountDTO billDiscountDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BillDiscount : {}", billDiscountDTO);
        if (billDiscountDTO.getId() != null) {
            throw new BadRequestAlertException("A new billDiscount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billDiscountDTO.setId(UUID.randomUUID());
        BillDiscountDTO result = billDiscountService
            .save(billDiscountDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create bill discount"));

        return ResponseEntity.created(new URI("/api/bill-discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillDiscountDTO> updateBillDiscount(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody BillDiscountDTO billDiscountDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BillDiscount : {}, {}", id, billDiscountDTO);
        if (billDiscountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billDiscountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billDiscountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillDiscountDTO result = billDiscountService
            .update(billDiscountDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillDiscountDTO> partialUpdateBillDiscount(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody BillDiscountDTO billDiscountDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BillDiscount partially : {}, {}", id, billDiscountDTO);
        if (billDiscountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billDiscountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billDiscountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillDiscountDTO result = billDiscountService
            .partialUpdate(billDiscountDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<BillDiscountDTO>> getAllBillDiscounts(
        @org.springdoc.core.annotations.ParameterObject org.springframework.data.domain.Pageable pageable
    ) {
        LOG.debug("REST request to get all BillDiscounts");
        List<BillDiscountDTO> billDiscounts = billDiscountService.findAll(pageable);
        return ResponseEntity.ok().body(billDiscounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDiscountDTO> getBillDiscount(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get BillDiscount : {}", id);
        Optional<BillDiscountDTO> billDiscountDTO = billDiscountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billDiscountDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBillDiscount(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete BillDiscount : {}", id);
        billDiscountService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
