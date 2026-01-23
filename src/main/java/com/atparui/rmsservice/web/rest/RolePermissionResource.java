package com.atparui.rmsservice.web.rest;

import com.atparui.rmsservice.repository.RolePermissionRepository;
import com.atparui.rmsservice.service.RolePermissionService;
import com.atparui.rmsservice.service.dto.RolePermissionDTO;
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
 * REST controller for managing {@link com.atparui.rmsservice.domain.RolePermission}.
 */
@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RolePermissionResource.class);

    private static final String ENTITY_NAME = "rmsserviceRolePermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RolePermissionService rolePermissionService;
    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionResource(RolePermissionService rolePermissionService, RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionService = rolePermissionService;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @PostMapping("")
    public Mono<ResponseEntity<RolePermissionDTO>> createRolePermission(@Valid @RequestBody RolePermissionDTO rolePermissionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RolePermission : {}", rolePermissionDTO);
        if (rolePermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new rolePermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rolePermissionDTO.setId(UUID.randomUUID());
        return rolePermissionService
            .save(rolePermissionDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/role-permissions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<RolePermissionDTO>> updateRolePermission(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody RolePermissionDTO rolePermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RolePermission : {}, {}", id, rolePermissionDTO);
        if (rolePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolePermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rolePermissionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return rolePermissionService
                    .update(rolePermissionDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RolePermissionDTO>> partialUpdateRolePermission(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RolePermissionDTO rolePermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RolePermission partially : {}, {}", id, rolePermissionDTO);
        if (rolePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolePermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return rolePermissionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return rolePermissionService
                    .partialUpdate(rolePermissionDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<RolePermissionDTO>>> getAllRolePermissions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of RolePermissions");
        return rolePermissionService
            .countAll()
            .zipWith(rolePermissionService.findAll(pageable).collectList())
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
    public Mono<ResponseEntity<RolePermissionDTO>> getRolePermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get RolePermission : {}", id);
        Mono<RolePermissionDTO> rolePermissionDTO = rolePermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rolePermissionDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRolePermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete RolePermission : {}", id);
        return rolePermissionService
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
