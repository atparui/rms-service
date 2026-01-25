package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuItemVariantRepository;
import com.atparui.rmsservice.service.MenuItemVariantService;
import com.atparui.rmsservice.service.dto.MenuItemVariantDTO;
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
@RequestMapping("/api/menu-item-variants")
public class MenuItemVariantResource {

    private static final Logger LOG = LoggerFactory.getLogger(MenuItemVariantResource.class);
    private static final String ENTITY_NAME = "rmsserviceMenuItemVariant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuItemVariantService menuItemVariantService;
    private final MenuItemVariantRepository menuItemVariantRepository;

    public MenuItemVariantResource(MenuItemVariantService menuItemVariantService, MenuItemVariantRepository menuItemVariantRepository) {
        this.menuItemVariantService = menuItemVariantService;
        this.menuItemVariantRepository = menuItemVariantRepository;
    }

    @PostMapping("")
    public ResponseEntity<MenuItemVariantDTO> createMenuItemVariant(@Valid @RequestBody MenuItemVariantDTO menuItemVariantDTO) throws URISyntaxException {
        LOG.debug("REST request to save MenuItemVariant : {}", menuItemVariantDTO);
        if (menuItemVariantDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuItemVariant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuItemVariantDTO.setId(UUID.randomUUID());
        MenuItemVariantDTO result = menuItemVariantService
            .save(menuItemVariantDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create menu item variant"));

        return ResponseEntity.created(new URI("/api/menu-item-variants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemVariantDTO> updateMenuItemVariant(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody MenuItemVariantDTO menuItemVariantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MenuItemVariant : {}, {}", id, menuItemVariantDTO);
        if (menuItemVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItemVariantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItemVariantDTO result = menuItemVariantService
            .update(menuItemVariantDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuItemVariantDTO> partialUpdateMenuItemVariant(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MenuItemVariantDTO menuItemVariantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MenuItemVariant partially : {}, {}", id, menuItemVariantDTO);
        if (menuItemVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItemVariantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItemVariantDTO result = menuItemVariantService
            .partialUpdate(menuItemVariantDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItemVariantDTO>> getAllMenuItemVariants(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of MenuItemVariants");
        long count = menuItemVariantService.countAll();
        List<MenuItemVariantDTO> entities = menuItemVariantService.findAll(pageable);

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
    public ResponseEntity<MenuItemVariantDTO> getMenuItemVariant(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get MenuItemVariant : {}", id);
        Optional<MenuItemVariantDTO> menuItemVariantDTO = menuItemVariantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuItemVariantDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMenuItemVariant(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete MenuItemVariant : {}", id);
        menuItemVariantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
