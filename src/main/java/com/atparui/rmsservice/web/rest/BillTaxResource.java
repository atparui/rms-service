package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.BillTaxRepository;
import com.atparui.rmsservice.service.BillTaxService;
import com.atparui.rmsservice.service.dto.BillTaxDTO;
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
 * REST controller for managing {@link com.atparui.rmsservice.domain.BillTax}.
 */
@RestController
@RequestMapping("/api/bill-taxes")
public class BillTaxResource {

    private static final Logger LOG = LoggerFactory.getLogger(BillTaxResource.class);
    private static final String ENTITY_NAME = "rmsserviceBillTax";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillTaxService billTaxService;
    private final BillTaxRepository billTaxRepository;

    public BillTaxResource(BillTaxService billTaxService, BillTaxRepository billTaxRepository) {
        this.billTaxService = billTaxService;
        this.billTaxRepository = billTaxRepository;
    }

    @PostMapping("")
    public ResponseEntity<BillTaxDTO> createBillTax(@Valid @RequestBody BillTaxDTO billTaxDTO) throws URISyntaxException {
        LOG.debug("REST request to save BillTax : {}", billTaxDTO);
        if (billTaxDTO.getId() != null) {
            throw new BadRequestAlertException("A new billTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billTaxDTO.setId(UUID.randomUUID());
        BillTaxDTO result = billTaxService
            .save(billTaxDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create bill tax"));

        return ResponseEntity.created(new URI("/api/bill-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillTaxDTO> updateBillTax(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody BillTaxDTO billTaxDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BillTax : {}, {}", id, billTaxDTO);
        if (billTaxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billTaxDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billTaxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillTaxDTO result = billTaxService
            .update(billTaxDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillTaxDTO> partialUpdateBillTax(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody BillTaxDTO billTaxDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BillTax partially : {}, {}", id, billTaxDTO);
        if (billTaxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billTaxDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billTaxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillTaxDTO result = billTaxService
            .partialUpdate(billTaxDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<BillTaxDTO>> getAllBillTaxes(
        @org.springdoc.core.annotations.ParameterObject org.springframework.data.domain.Pageable pageable
    ) {
        LOG.debug("REST request to get all BillTaxes");
        List<BillTaxDTO> billTaxes = billTaxService.findAll(pageable);
        return ResponseEntity.ok().body(billTaxes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillTaxDTO> getBillTax(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get BillTax : {}", id);
        Optional<BillTaxDTO> billTaxDTO = billTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billTaxDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBillTax(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete BillTax : {}", id);
        billTaxService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
