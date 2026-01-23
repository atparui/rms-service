package com.atparui.rmsservice.web.rest;

import com.atparui.rmsservice.repository.MenuPermissionRepository;
import com.atparui.rmsservice.service.MenuPermissionService;
import com.atparui.rmsservice.service.dto.MenuPermissionDTO;
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
 * REST controller for managing {@link com.atparui.rmsservice.domain.MenuPermission}.
 */
@RestController
@RequestMapping("/api/menu-permissions")
public class MenuPermissionResource {

    private static final Logger LOG = LoggerFactory.getLogger(MenuPermissionResource.class);

    private static final String ENTITY_NAME = "rmsserviceMenuPermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuPermissionService menuPermissionService;
    private final MenuPermissionRepository menuPermissionRepository;

    public MenuPermissionResource(MenuPermissionService menuPermissionService, MenuPermissionRepository menuPermissionRepository) {
        this.menuPermissionService = menuPermissionService;
        this.menuPermissionRepository = menuPermissionRepository;
    }

    @PostMapping("")
    public Mono<ResponseEntity<MenuPermissionDTO>> createMenuPermission(@Valid @RequestBody MenuPermissionDTO menuPermissionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MenuPermission : {}", menuPermissionDTO);
        if (menuPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuPermissionDTO.setId(UUID.randomUUID());
        return menuPermissionService
            .save(menuPermissionDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/menu-permissions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MenuPermissionDTO>> updateMenuPermission(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody MenuPermissionDTO menuPermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MenuPermission : {}, {}", id, menuPermissionDTO);
        if (menuPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return menuPermissionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return menuPermissionService
                    .update(menuPermissionDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MenuPermissionDTO>> partialUpdateMenuPermission(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MenuPermissionDTO menuPermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MenuPermission partially : {}, {}", id, menuPermissionDTO);
        if (menuPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return menuPermissionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return menuPermissionService
                    .partialUpdate(menuPermissionDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<MenuPermissionDTO>>> getAllMenuPermissions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of MenuPermissions");
        return menuPermissionService
            .countAll()
            .zipWith(menuPermissionService.findAll(pageable).collectList())
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
    public Mono<ResponseEntity<MenuPermissionDTO>> getMenuPermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get MenuPermission : {}", id);
        Mono<MenuPermissionDTO> menuPermissionDTO = menuPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuPermissionDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMenuPermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete MenuPermission : {}", id);
        return menuPermissionService
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
