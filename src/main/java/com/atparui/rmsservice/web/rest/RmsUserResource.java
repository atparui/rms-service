package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.RmsUserRepository;
import com.atparui.rmsservice.service.RmsUserService;
import com.atparui.rmsservice.service.dto.RmsUserDTO;
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
@RequestMapping("/api/rms-users")
public class RmsUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(RmsUserResource.class);
    private static final String ENTITY_NAME = "rmsserviceRmsUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RmsUserService rmsUserService;
    private final RmsUserRepository rmsUserRepository;

    public RmsUserResource(RmsUserService rmsUserService, RmsUserRepository rmsUserRepository) {
        this.rmsUserService = rmsUserService;
        this.rmsUserRepository = rmsUserRepository;
    }

    @PostMapping("")
    public ResponseEntity<RmsUserDTO> createRmsUser(@Valid @RequestBody RmsUserDTO rmsUserDTO) throws URISyntaxException {
        LOG.debug("REST request to save RmsUser : {}", rmsUserDTO);
        if (rmsUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new rmsUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rmsUserDTO.setId(UUID.randomUUID());
        RmsUserDTO result = rmsUserService
            .save(rmsUserDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create rms user"));

        return ResponseEntity.created(new URI("/api/rms-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RmsUserDTO> updateRmsUser(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody RmsUserDTO rmsUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RmsUser : {}, {}", id, rmsUserDTO);
        if (rmsUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rmsUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rmsUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RmsUserDTO result = rmsUserService
            .update(rmsUserDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RmsUserDTO> partialUpdateRmsUser(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RmsUserDTO rmsUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RmsUser partially : {}, {}", id, rmsUserDTO);
        if (rmsUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rmsUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rmsUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RmsUserDTO result = rmsUserService
            .partialUpdate(rmsUserDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RmsUserDTO>> getAllRmsUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of RmsUsers");
        long count = rmsUserService.countAll();
        List<RmsUserDTO> entities = rmsUserService.findAll(pageable);

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
    public ResponseEntity<RmsUserDTO> getRmsUser(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get RmsUser : {}", id);
        Optional<RmsUserDTO> rmsUserDTO = rmsUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rmsUserDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRmsUser(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete RmsUser : {}", id);
        rmsUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
