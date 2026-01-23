package com.atparui.rmsservice.web.rest;

import com.atparui.rmsservice.repository.AppMenuRepository;
import com.atparui.rmsservice.service.AppMenuService;
import com.atparui.rmsservice.service.dto.AppMenuDTO;
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
 * REST controller for managing {@link com.atparui.rmsservice.domain.AppMenu}.
 */
@RestController
@RequestMapping("/api/app-menus")
public class AppMenuResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppMenuResource.class);

    private static final String ENTITY_NAME = "rmsserviceAppMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppMenuService appMenuService;
    private final AppMenuRepository appMenuRepository;

    public AppMenuResource(AppMenuService appMenuService, AppMenuRepository appMenuRepository) {
        this.appMenuService = appMenuService;
        this.appMenuRepository = appMenuRepository;
    }

    @PostMapping("")
    public Mono<ResponseEntity<AppMenuDTO>> createAppMenu(@Valid @RequestBody AppMenuDTO appMenuDTO) throws URISyntaxException {
        LOG.debug("REST request to save AppMenu : {}", appMenuDTO);
        if (appMenuDTO.getId() != null) {
            throw new BadRequestAlertException("A new appMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appMenuDTO.setId(UUID.randomUUID());
        return appMenuService
            .save(appMenuDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/app-menus/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AppMenuDTO>> updateAppMenu(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AppMenuDTO appMenuDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AppMenu : {}, {}", id, appMenuDTO);
        if (appMenuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appMenuDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appMenuRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return appMenuService
                    .update(appMenuDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AppMenuDTO>> partialUpdateAppMenu(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AppMenuDTO appMenuDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AppMenu partially : {}, {}", id, appMenuDTO);
        if (appMenuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appMenuDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appMenuRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return appMenuService
                    .partialUpdate(appMenuDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AppMenuDTO>>> getAllAppMenus(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of AppMenus");
        return appMenuService
            .countAll()
            .zipWith(appMenuService.findAll(pageable).collectList())
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
    public Mono<ResponseEntity<AppMenuDTO>> getAppMenu(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AppMenu : {}", id);
        Mono<AppMenuDTO> appMenuDTO = appMenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appMenuDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAppMenu(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AppMenu : {}", id);
        return appMenuService
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
