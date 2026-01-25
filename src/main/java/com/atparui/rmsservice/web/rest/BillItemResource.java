package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.BillItemRepository;
import com.atparui.rmsservice.service.BillItemService;
import com.atparui.rmsservice.service.dto.BillItemDTO;
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
 * REST controller for managing {@link com.atparui.rmsservice.domain.BillItem}.
 */
@RestController
@RequestMapping("/api/bill-items")
public class BillItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(BillItemResource.class);

    private static final String ENTITY_NAME = "rmsserviceBillItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillItemService billItemService;

    private final BillItemRepository billItemRepository;

    public BillItemResource(BillItemService billItemService, BillItemRepository billItemRepository) {
        this.billItemService = billItemService;
        this.billItemRepository = billItemRepository;
    }

    /**
     * {@code POST  /bill-items} : Create a new billItem.
     *
     * @param billItemDTO the billItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billItemDTO, or with status {@code 400 (Bad Request)} if the billItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillItemDTO> createBillItem(@Valid @RequestBody BillItemDTO billItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save BillItem : {}", billItemDTO);
        if (billItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new billItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billItemDTO.setId(UUID.randomUUID());
        BillItemDTO result = billItemService
            .save(billItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create bill item"));

        return ResponseEntity.created(new URI("/api/bill-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bill-items/:id} : Updates an existing billItem.
     *
     * @param id the id of the billItemDTO to save.
     * @param billItemDTO the billItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billItemDTO,
     * or with status {@code 400 (Bad Request)} if the billItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BillItemDTO> updateBillItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody BillItemDTO billItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BillItem : {}, {}", id, billItemDTO);
        if (billItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillItemDTO result = billItemService
            .update(billItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bill-items/:id} : Partial updates given fields of an existing billItem, field will ignore if it is null
     *
     * @param id the id of the billItemDTO to save.
     * @param billItemDTO the billItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billItemDTO,
     * or with status {@code 400 (Bad Request)} if the billItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillItemDTO> partialUpdateBillItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody BillItemDTO billItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BillItem partially : {}, {}", id, billItemDTO);
        if (billItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillItemDTO result = billItemService
            .partialUpdate(billItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bill-items} : get all the billItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billItems in body.
     */
    @GetMapping(value = "")
    public ResponseEntity<List<BillItemDTO>> getAllBillItems(
        @org.springdoc.core.annotations.ParameterObject org.springframework.data.domain.Pageable pageable
    ) {
        LOG.debug("REST request to get all BillItems");
        List<BillItemDTO> billItems = billItemService.findAll(pageable);
        return ResponseEntity.ok().body(billItems);
    }

    /**
     * {@code GET  /bill-items/:id} : get the "id" billItem.
     *
     * @param id the id of the billItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillItemDTO> getBillItem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get BillItem : {}", id);
        Optional<BillItemDTO> billItemDTO = billItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billItemDTO);
    }

    /**
     * {@code DELETE  /bill-items/:id} : delete the "id" billItem.
     *
     * @param id the id of the billItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBillItem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete BillItem : {}", id);
        billItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
