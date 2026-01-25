package com.atparui.rmsservice.web.rest;
import java.util.Optional;

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
    public ResponseEntity<AppMenuDTO> createAppMenu(@Valid @RequestBody AppMenuDTO appMenuDTO) throws URISyntaxException {
        LOG.debug("REST request to save AppMenu : {}", appMenuDTO);
        if (appMenuDTO.getId() != null) {
            throw new BadRequestAlertException("A new appMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appMenuDTO.setId(UUID.randomUUID());
        AppMenuDTO result = appMenuService
            .save(appMenuDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create app menu"));

        return ResponseEntity.created(new URI("/api/app-menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppMenuDTO> updateAppMenu(
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

        if (!appMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppMenuDTO result = appMenuService
            .update(appMenuDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppMenuDTO> partialUpdateAppMenu(
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

        if (!appMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppMenuDTO result = appMenuService
            .partialUpdate(appMenuDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppMenuDTO>> getAllAppMenus(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of AppMenus");
        long count = appMenuService.countAll();
        List<AppMenuDTO> entities = appMenuService.findAll(pageable);

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
    public ResponseEntity<AppMenuDTO> getAppMenu(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AppMenu : {}", id);
        Optional<AppMenuDTO> appMenuDTO = appMenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appMenuDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAppMenu(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AppMenu : {}", id);
        appMenuService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
