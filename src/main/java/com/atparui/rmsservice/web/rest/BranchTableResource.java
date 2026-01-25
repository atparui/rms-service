package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.BranchTableRepository;
import com.atparui.rmsservice.service.BranchTableService;
import com.atparui.rmsservice.service.dto.BranchTableDTO;
import com.atparui.rmsservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
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
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.PaginationUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.atparui.rmsservice.domain.BranchTable}.
 */
@RestController
@RequestMapping("/api/branch-tables")
public class BranchTableResource {

    private static final Logger LOG = LoggerFactory.getLogger(BranchTableResource.class);

    private static final String ENTITY_NAME = "rmsserviceBranchTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchTableService branchTableService;

    private final BranchTableRepository branchTableRepository;

    public BranchTableResource(BranchTableService branchTableService, BranchTableRepository branchTableRepository) {
        this.branchTableService = branchTableService;
        this.branchTableRepository = branchTableRepository;
    }

    /**
     * {@code POST  /branch-tables} : Create a new branchTable.
     *
     * @param branchTableDTO the branchTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchTableDTO, or with status {@code 400 (Bad Request)} if the branchTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BranchTableDTO> createBranchTable(@Valid @RequestBody BranchTableDTO branchTableDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BranchTable : {}", branchTableDTO);
        if (branchTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        branchTableDTO.setId(UUID.randomUUID());
        BranchTableDTO result = branchTableService
            .save(branchTableDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create branch table"));

        return ResponseEntity.created(new URI("/api/branch-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-tables/:id} : Updates an existing branchTable.
     *
     * @param id             the id of the branchTableDTO to save.
     * @param branchTableDTO the branchTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchTableDTO,
     * or with status {@code 400 (Bad Request)} if the branchTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BranchTableDTO> updateBranchTable(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody BranchTableDTO branchTableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BranchTable : {}, {}", id, branchTableDTO);
        if (branchTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BranchTableDTO result = branchTableService
            .update(branchTableDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /branch-tables/:id} : Partial updates given fields of an existing branchTable, field will ignore if it is null
     *
     * @param id             the id of the branchTableDTO to save.
     * @param branchTableDTO the branchTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchTableDTO,
     * or with status {@code 400 (Bad Request)} if the branchTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the branchTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the branchTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<BranchTableDTO> partialUpdateBranchTable(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody BranchTableDTO branchTableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BranchTable partially : {}, {}", id, branchTableDTO);
        if (branchTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BranchTableDTO result = branchTableService
            .partialUpdate(branchTableDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /branch-tables} : get all the branchTables.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchTables in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BranchTableDTO>> getAllBranchTables(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of BranchTables");
        long count = branchTableService.countAll();
        List<BranchTableDTO> entities = branchTableService.findAll(pageable);

        return ResponseEntity.ok()
            .headers(
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    new PageImpl<>(entities, pageable, count)
                )
            )
            .body(entities);
    }

    /**
     * {@code GET  /branch-tables/:id} : get the "id" branchTable.
     *
     * @param id the id of the branchTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BranchTableDTO> getBranchTable(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get BranchTable : {}", id);
        Optional<BranchTableDTO> branchTableDTO = branchTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchTableDTO);
    }

    /**
     * {@code DELETE  /branch-tables/:id} : delete the "id" branchTable.
     *
     * @param id the id of the branchTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBranchTable(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete BranchTable : {}", id);
        branchTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /branch-tables/_search?query=:query} : search for the branchTable corresponding
     * to the query.
     *
     * @param query    the query of the branchTable search.
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<BranchTableDTO>> searchBranchTables(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of BranchTables for query {}", query);
        // Search functionality removed (Elasticsearch was removed)
        // Return empty list with proper pagination headers
        List<BranchTableDTO> results = new ArrayList<>();
        PageImpl<BranchTableDTO> page = new PageImpl<>(results, pageable, 0);

        return ResponseEntity.ok()
            .headers(
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .body(results);
    }
}
