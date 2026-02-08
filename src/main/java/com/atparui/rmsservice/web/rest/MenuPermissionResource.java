package com.atparui.rmsservice.web.rest;
import java.util.Optional;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.PaginationUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

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
    public ResponseEntity<MenuPermissionDTO> createMenuPermission(@Valid @RequestBody MenuPermissionDTO menuPermissionDTO) throws URISyntaxException {
        LOG.debug("REST request to save MenuPermission : {}", menuPermissionDTO);
        if (menuPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuPermissionDTO.setId(UUID.randomUUID());
        MenuPermissionDTO result = menuPermissionService
            .save(menuPermissionDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create menu permission"));

        return ResponseEntity.created(new URI("/api/menu-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuPermissionDTO> updateMenuPermission(
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

        if (!menuPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuPermissionDTO result = menuPermissionService
            .update(menuPermissionDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuPermissionDTO> partialUpdateMenuPermission(
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

        if (!menuPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuPermissionDTO result = menuPermissionService
            .partialUpdate(menuPermissionDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuPermissionDTO>> getAllMenuPermissions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of MenuPermissions");
        long count = menuPermissionService.countAll();
        List<MenuPermissionDTO> entities = menuPermissionService.findAll(pageable);

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
    public ResponseEntity<MenuPermissionDTO> getMenuPermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get MenuPermission : {}", id);
        Optional<MenuPermissionDTO> menuPermissionDTO = menuPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuPermissionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMenuPermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete MenuPermission : {}", id);
        menuPermissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
