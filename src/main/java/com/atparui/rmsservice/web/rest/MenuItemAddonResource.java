package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuItemAddonRepository;
import com.atparui.rmsservice.service.MenuItemAddonService;
import com.atparui.rmsservice.service.dto.MenuItemAddonDTO;
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
@RequestMapping("/api/menu-item-addons")
public class MenuItemAddonResource {

    private static final Logger LOG = LoggerFactory.getLogger(MenuItemAddonResource.class);
    private static final String ENTITY_NAME = "rmsserviceMenuItemAddon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuItemAddonService menuItemAddonService;
    private final MenuItemAddonRepository menuItemAddonRepository;

    public MenuItemAddonResource(MenuItemAddonService menuItemAddonService, MenuItemAddonRepository menuItemAddonRepository) {
        this.menuItemAddonService = menuItemAddonService;
        this.menuItemAddonRepository = menuItemAddonRepository;
    }

    @PostMapping("")
    public ResponseEntity<MenuItemAddonDTO> createMenuItemAddon(@Valid @RequestBody MenuItemAddonDTO menuItemAddonDTO) throws URISyntaxException {
        LOG.debug("REST request to save MenuItemAddon : {}", menuItemAddonDTO);
        if (menuItemAddonDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuItemAddon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuItemAddonDTO.setId(UUID.randomUUID());
        MenuItemAddonDTO result = menuItemAddonService
            .save(menuItemAddonDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create menu item addon"));

        return ResponseEntity.created(new URI("/api/menu-item-addons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemAddonDTO> updateMenuItemAddon(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody MenuItemAddonDTO menuItemAddonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MenuItemAddon : {}, {}", id, menuItemAddonDTO);
        if (menuItemAddonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItemAddonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemAddonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItemAddonDTO result = menuItemAddonService
            .update(menuItemAddonDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuItemAddonDTO> partialUpdateMenuItemAddon(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MenuItemAddonDTO menuItemAddonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MenuItemAddon partially : {}, {}", id, menuItemAddonDTO);
        if (menuItemAddonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItemAddonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemAddonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItemAddonDTO result = menuItemAddonService
            .partialUpdate(menuItemAddonDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItemAddonDTO>> getAllMenuItemAddons(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of MenuItemAddons");
        long count = menuItemAddonService.countAll();
        List<MenuItemAddonDTO> entities = menuItemAddonService.findAll(pageable);

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
    public ResponseEntity<MenuItemAddonDTO> getMenuItemAddon(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get MenuItemAddon : {}", id);
        Optional<MenuItemAddonDTO> menuItemAddonDTO = menuItemAddonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuItemAddonDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMenuItemAddon(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete MenuItemAddon : {}", id);
        menuItemAddonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
