package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.AppNavigationMenuRoleRepository;
import com.atparui.rmsservice.service.AppNavigationMenuRoleService;
import com.atparui.rmsservice.service.dto.AppNavigationMenuRoleDTO;
import com.atparui.rmsservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.PaginationUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/app-navigation-menu-roles")
public class AppNavigationMenuRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppNavigationMenuRoleResource.class);
    private static final String ENTITY_NAME = "rmsserviceAppNavigationMenuRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppNavigationMenuRoleService appNavigationMenuRoleService;
    private final AppNavigationMenuRoleRepository appNavigationMenuRoleRepository;

    public AppNavigationMenuRoleResource(
        AppNavigationMenuRoleService appNavigationMenuRoleService,
        AppNavigationMenuRoleRepository appNavigationMenuRoleRepository
    ) {
        this.appNavigationMenuRoleService = appNavigationMenuRoleService;
        this.appNavigationMenuRoleRepository = appNavigationMenuRoleRepository;
    }

    @PostMapping("")
    public ResponseEntity<AppNavigationMenuRoleDTO> createAppNavigationMenuRole(
        @Valid @RequestBody AppNavigationMenuRoleDTO appNavigationMenuRoleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AppNavigationMenuRole : {}", appNavigationMenuRoleDTO);
        if (appNavigationMenuRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new appNavigationMenuRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appNavigationMenuRoleDTO.setId(UUID.randomUUID());
        AppNavigationMenuRoleDTO result = appNavigationMenuRoleService
            .save(appNavigationMenuRoleDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create app navigation menu role"));

        return ResponseEntity.created(new URI("/api/app-navigation-menu-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppNavigationMenuRoleDTO> updateAppNavigationMenuRole(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AppNavigationMenuRoleDTO appNavigationMenuRoleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AppNavigationMenuRole : {}, {}", id, appNavigationMenuRoleDTO);
        if (appNavigationMenuRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appNavigationMenuRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appNavigationMenuRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppNavigationMenuRoleDTO result = appNavigationMenuRoleService
            .update(appNavigationMenuRoleDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppNavigationMenuRoleDTO> partialUpdateAppNavigationMenuRole(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AppNavigationMenuRoleDTO appNavigationMenuRoleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AppNavigationMenuRole partially : {}, {}", id, appNavigationMenuRoleDTO);
        if (appNavigationMenuRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appNavigationMenuRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appNavigationMenuRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppNavigationMenuRoleDTO result = appNavigationMenuRoleService
            .partialUpdate(appNavigationMenuRoleDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppNavigationMenuRoleDTO>> getAllAppNavigationMenuRoles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        HttpServletRequest request
    ) {
        LOG.debug("REST request to get a page of AppNavigationMenuRoles");
        long count = appNavigationMenuRoleService.countAll();
        List<AppNavigationMenuRoleDTO> entities = appNavigationMenuRoleService.findAll(pageable);

        return ResponseEntity.ok()
            .headers(
                PaginationUtil.generatePaginationHttpHeaders(
                    ServletUriComponentsBuilder.fromRequest(request),
                    new PageImpl<>(entities, pageable, count)
                )
            )
            .body(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppNavigationMenuRoleDTO> getAppNavigationMenuRole(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AppNavigationMenuRole : {}", id);
        Optional<AppNavigationMenuRoleDTO> appNavigationMenuRoleDTO = appNavigationMenuRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appNavigationMenuRoleDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAppNavigationMenuRole(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AppNavigationMenuRole : {}", id);
        appNavigationMenuRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
