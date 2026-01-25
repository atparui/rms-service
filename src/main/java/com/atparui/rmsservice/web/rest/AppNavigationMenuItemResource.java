package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.AppNavigationMenuItemRepository;
import com.atparui.rmsservice.service.AppNavigationMenuItemService;
import com.atparui.rmsservice.service.dto.AppNavigationMenuItemDTO;
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
@RequestMapping("/api/app-navigation-menu-items")
public class AppNavigationMenuItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppNavigationMenuItemResource.class);
    private static final String ENTITY_NAME = "rmsserviceAppNavigationMenuItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppNavigationMenuItemService appNavigationMenuItemService;
    private final AppNavigationMenuItemRepository appNavigationMenuItemRepository;

    public AppNavigationMenuItemResource(
        AppNavigationMenuItemService appNavigationMenuItemService,
        AppNavigationMenuItemRepository appNavigationMenuItemRepository
    ) {
        this.appNavigationMenuItemService = appNavigationMenuItemService;
        this.appNavigationMenuItemRepository = appNavigationMenuItemRepository;
    }

    @PostMapping("")
    public ResponseEntity<AppNavigationMenuItemDTO> createAppNavigationMenuItem(
        @Valid @RequestBody AppNavigationMenuItemDTO appNavigationMenuItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AppNavigationMenuItem : {}", appNavigationMenuItemDTO);
        if (appNavigationMenuItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new appNavigationMenuItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appNavigationMenuItemDTO.setId(UUID.randomUUID());
        AppNavigationMenuItemDTO result = appNavigationMenuItemService
            .save(appNavigationMenuItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create app navigation menu item"));

        return ResponseEntity.created(new URI("/api/app-navigation-menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppNavigationMenuItemDTO> updateAppNavigationMenuItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AppNavigationMenuItemDTO appNavigationMenuItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AppNavigationMenuItem : {}, {}", id, appNavigationMenuItemDTO);
        if (appNavigationMenuItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appNavigationMenuItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appNavigationMenuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppNavigationMenuItemDTO result = appNavigationMenuItemService
            .update(appNavigationMenuItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppNavigationMenuItemDTO> partialUpdateAppNavigationMenuItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AppNavigationMenuItemDTO appNavigationMenuItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AppNavigationMenuItem partially : {}, {}", id, appNavigationMenuItemDTO);
        if (appNavigationMenuItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appNavigationMenuItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appNavigationMenuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppNavigationMenuItemDTO result = appNavigationMenuItemService
            .partialUpdate(appNavigationMenuItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppNavigationMenuItemDTO>> getAllAppNavigationMenuItems(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of AppNavigationMenuItems");
        long count = appNavigationMenuItemService.countAll();
        List<AppNavigationMenuItemDTO> entities = appNavigationMenuItemService.findAll(pageable);

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
    public ResponseEntity<AppNavigationMenuItemDTO> getAppNavigationMenuItem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AppNavigationMenuItem : {}", id);
        Optional<AppNavigationMenuItemDTO> appNavigationMenuItemDTO = appNavigationMenuItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appNavigationMenuItemDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAppNavigationMenuItem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AppNavigationMenuItem : {}", id);
        appNavigationMenuItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
