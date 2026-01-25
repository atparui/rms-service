package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.ShiftRepository;
import com.atparui.rmsservice.service.ShiftService;
import com.atparui.rmsservice.service.dto.ShiftDTO;
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
@RequestMapping("/api/shifts")
public class ShiftResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShiftResource.class);
    private static final String ENTITY_NAME = "rmsserviceShift";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShiftService shiftService;
    private final ShiftRepository shiftRepository;

    public ShiftResource(ShiftService shiftService, ShiftRepository shiftRepository) {
        this.shiftService = shiftService;
        this.shiftRepository = shiftRepository;
    }

    @PostMapping("")
    public ResponseEntity<ShiftDTO> createShift(@Valid @RequestBody ShiftDTO shiftDTO) throws URISyntaxException {
        LOG.debug("REST request to save Shift : {}", shiftDTO);
        if (shiftDTO.getId() != null) {
            throw new BadRequestAlertException("A new shift cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shiftDTO.setId(UUID.randomUUID());
        ShiftDTO result = shiftService
            .save(shiftDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create shift"));

        return ResponseEntity.created(new URI("/api/shifts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShiftDTO> updateShift(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ShiftDTO shiftDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Shift : {}, {}", id, shiftDTO);
        if (shiftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shiftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shiftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShiftDTO result = shiftService
            .update(shiftDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShiftDTO> partialUpdateShift(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ShiftDTO shiftDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Shift partially : {}, {}", id, shiftDTO);
        if (shiftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shiftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shiftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShiftDTO result = shiftService
            .partialUpdate(shiftDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShiftDTO>> getAllShifts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Shifts");
        long count = shiftService.countAll();
        List<ShiftDTO> entities = shiftService.findAll(pageable);

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
    public ResponseEntity<ShiftDTO> getShift(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Shift : {}", id);
        Optional<ShiftDTO> shiftDTO = shiftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shiftDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteShift(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Shift : {}", id);
        shiftService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
