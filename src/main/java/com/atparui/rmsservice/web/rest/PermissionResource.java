package com.atparui.rmsservice.web.rest;

import com.atparui.rmsservice.repository.PermissionRepository;
import com.atparui.rmsservice.service.PermissionService;
import com.atparui.rmsservice.service.dto.PermissionDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.atparui.rmsservice.domain.Permission}.
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionResource.class);

    private static final String ENTITY_NAME = "rmsservicePermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;

    public PermissionResource(PermissionService permissionService, PermissionRepository permissionRepository) {
        this.permissionService = permissionService;
        this.permissionRepository = permissionRepository;
    }

    @PostMapping("")
    public Mono<ResponseEntity<PermissionDTO>> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Permission : {}", permissionDTO);
        if (permissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new permission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        permissionDTO.setId(UUID.randomUUID());
        return permissionService
            .save(permissionDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/permissions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PermissionDTO>> updatePermission(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody PermissionDTO permissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Permission : {}, {}", id, permissionDTO);
        if (permissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return permissionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return permissionService
                    .update(permissionDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PermissionDTO>> partialUpdatePermission(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody PermissionDTO permissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Permission partially : {}, {}", id, permissionDTO);
        if (permissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return permissionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return permissionService
                    .partialUpdate(permissionDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PermissionDTO>>> getAllPermissions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Permissions");
        return permissionService
            .countAll()
            .zipWith(permissionService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PermissionDTO>> getPermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Permission : {}", id);
        Mono<PermissionDTO> permissionDTO = permissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Permission : {}", id);
        return permissionService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
