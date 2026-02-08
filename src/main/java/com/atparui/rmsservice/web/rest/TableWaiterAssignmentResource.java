package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.TableWaiterAssignmentRepository;
import com.atparui.rmsservice.service.TableWaiterAssignmentService;
import com.atparui.rmsservice.service.dto.TableWaiterAssignmentDTO;
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
@RequestMapping("/api/table-waiter-assignments")
public class TableWaiterAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(TableWaiterAssignmentResource.class);
    private static final String ENTITY_NAME = "rmsserviceTableWaiterAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TableWaiterAssignmentService tableWaiterAssignmentService;
    private final TableWaiterAssignmentRepository tableWaiterAssignmentRepository;

    public TableWaiterAssignmentResource(TableWaiterAssignmentService tableWaiterAssignmentService, TableWaiterAssignmentRepository tableWaiterAssignmentRepository) {
        this.tableWaiterAssignmentService = tableWaiterAssignmentService;
        this.tableWaiterAssignmentRepository = tableWaiterAssignmentRepository;
    }

    @PostMapping("")
    public ResponseEntity<TableWaiterAssignmentDTO> createTableWaiterAssignment(@Valid @RequestBody TableWaiterAssignmentDTO tableWaiterAssignmentDTO) throws URISyntaxException {
        LOG.debug("REST request to save TableWaiterAssignment : {}", tableWaiterAssignmentDTO);
        if (tableWaiterAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new tableWaiterAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tableWaiterAssignmentDTO.setId(UUID.randomUUID());
        TableWaiterAssignmentDTO result = tableWaiterAssignmentService
            .save(tableWaiterAssignmentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create table waiter assignment"));

        return ResponseEntity.created(new URI("/api/table-waiter-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableWaiterAssignmentDTO> updateTableWaiterAssignment(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody TableWaiterAssignmentDTO tableWaiterAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TableWaiterAssignment : {}, {}", id, tableWaiterAssignmentDTO);
        if (tableWaiterAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tableWaiterAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableWaiterAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TableWaiterAssignmentDTO result = tableWaiterAssignmentService
            .update(tableWaiterAssignmentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TableWaiterAssignmentDTO> partialUpdateTableWaiterAssignment(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TableWaiterAssignmentDTO tableWaiterAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TableWaiterAssignment partially : {}, {}", id, tableWaiterAssignmentDTO);
        if (tableWaiterAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tableWaiterAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableWaiterAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TableWaiterAssignmentDTO result = tableWaiterAssignmentService
            .partialUpdate(tableWaiterAssignmentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TableWaiterAssignmentDTO>> getAllTableWaiterAssignments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of TableWaiterAssignments");
        long count = tableWaiterAssignmentService.countAll();
        List<TableWaiterAssignmentDTO> entities = tableWaiterAssignmentService.findAll(pageable);

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
    public ResponseEntity<TableWaiterAssignmentDTO> getTableWaiterAssignment(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get TableWaiterAssignment : {}", id);
        Optional<TableWaiterAssignmentDTO> tableWaiterAssignmentDTO = tableWaiterAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tableWaiterAssignmentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTableWaiterAssignment(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete TableWaiterAssignment : {}", id);
        tableWaiterAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
