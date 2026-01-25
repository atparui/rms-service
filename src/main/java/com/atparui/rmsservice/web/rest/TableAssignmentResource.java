package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.TableAssignmentRepository;
import com.atparui.rmsservice.service.TableAssignmentService;
import com.atparui.rmsservice.service.dto.TableAssignmentDTO;
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
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.PaginationUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/table-assignments")
public class TableAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(TableAssignmentResource.class);
    private static final String ENTITY_NAME = "rmsserviceTableAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TableAssignmentService tableAssignmentService;
    private final TableAssignmentRepository tableAssignmentRepository;

    public TableAssignmentResource(TableAssignmentService tableAssignmentService, TableAssignmentRepository tableAssignmentRepository) {
        this.tableAssignmentService = tableAssignmentService;
        this.tableAssignmentRepository = tableAssignmentRepository;
    }

    @PostMapping("")
    public ResponseEntity<TableAssignmentDTO> createTableAssignment(@Valid @RequestBody TableAssignmentDTO tableAssignmentDTO) throws URISyntaxException {
        LOG.debug("REST request to save TableAssignment : {}", tableAssignmentDTO);
        if (tableAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new tableAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tableAssignmentDTO.setId(UUID.randomUUID());
        TableAssignmentDTO result = tableAssignmentService
            .save(tableAssignmentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create table assignment"));

        return ResponseEntity.created(new URI("/api/table-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableAssignmentDTO> updateTableAssignment(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody TableAssignmentDTO tableAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TableAssignment : {}, {}", id, tableAssignmentDTO);
        if (tableAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tableAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TableAssignmentDTO result = tableAssignmentService
            .update(tableAssignmentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TableAssignmentDTO> partialUpdateTableAssignment(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TableAssignmentDTO tableAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TableAssignment partially : {}, {}", id, tableAssignmentDTO);
        if (tableAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tableAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TableAssignmentDTO result = tableAssignmentService
            .partialUpdate(tableAssignmentDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TableAssignmentDTO>> getAllTableAssignments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of TableAssignments");
        long count = tableAssignmentService.countAll();
        List<TableAssignmentDTO> entities = tableAssignmentService.findAll(pageable);

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
    public ResponseEntity<TableAssignmentDTO> getTableAssignment(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get TableAssignment : {}", id);
        Optional<TableAssignmentDTO> tableAssignmentDTO = tableAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tableAssignmentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTableAssignment(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete TableAssignment : {}", id);
        tableAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
