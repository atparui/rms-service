package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuItemRepository;
import com.atparui.rmsservice.service.MenuItemService;
import com.atparui.rmsservice.service.dto.MenuItemDTO;
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
@RequestMapping("/api/menu-items")
public class MenuItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(MenuItemResource.class);
    private static final String ENTITY_NAME = "rmsserviceMenuItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuItemService menuItemService;
    private final MenuItemRepository menuItemRepository;

    public MenuItemResource(MenuItemService menuItemService, MenuItemRepository menuItemRepository) {
        this.menuItemService = menuItemService;
        this.menuItemRepository = menuItemRepository;
    }

    @PostMapping("")
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save MenuItem : {}", menuItemDTO);
        if (menuItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuItemDTO.setId(UUID.randomUUID());
        MenuItemDTO result = menuItemService
            .save(menuItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create menu item"));

        return ResponseEntity.created(new URI("/api/menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody MenuItemDTO menuItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MenuItem : {}, {}", id, menuItemDTO);
        if (menuItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItemDTO result = menuItemService
            .update(menuItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuItemDTO> partialUpdateMenuItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MenuItemDTO menuItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MenuItem partially : {}, {}", id, menuItemDTO);
        if (menuItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItemDTO result = menuItemService
            .partialUpdate(menuItemDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of MenuItems");
        long count = menuItemService.countAll();
        List<MenuItemDTO> entities = menuItemService.findAll(pageable);

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
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get MenuItem : {}", id);
        Optional<MenuItemDTO> menuItemDTO = menuItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuItemDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMenuItem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete MenuItem : {}", id);
        menuItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
