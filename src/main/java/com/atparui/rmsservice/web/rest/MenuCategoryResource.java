package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuCategoryRepository;
import com.atparui.rmsservice.service.MenuCategoryService;
import com.atparui.rmsservice.service.dto.MenuCategoryDTO;
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
@RequestMapping("/api/menu-categories")
public class MenuCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(MenuCategoryResource.class);
    private static final String ENTITY_NAME = "rmsserviceMenuCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuCategoryService menuCategoryService;
    private final MenuCategoryRepository menuCategoryRepository;

    public MenuCategoryResource(MenuCategoryService menuCategoryService, MenuCategoryRepository menuCategoryRepository) {
        this.menuCategoryService = menuCategoryService;
        this.menuCategoryRepository = menuCategoryRepository;
    }

    @PostMapping("")
    public ResponseEntity<MenuCategoryDTO> createMenuCategory(@Valid @RequestBody MenuCategoryDTO menuCategoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save MenuCategory : {}", menuCategoryDTO);
        if (menuCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuCategoryDTO.setId(UUID.randomUUID());
        MenuCategoryDTO result = menuCategoryService
            .save(menuCategoryDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create menu category"));

        return ResponseEntity.created(new URI("/api/menu-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuCategoryDTO> updateMenuCategory(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody MenuCategoryDTO menuCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MenuCategory : {}, {}", id, menuCategoryDTO);
        if (menuCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuCategoryDTO result = menuCategoryService
            .update(menuCategoryDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuCategoryDTO> partialUpdateMenuCategory(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MenuCategoryDTO menuCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MenuCategory partially : {}, {}", id, menuCategoryDTO);
        if (menuCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuCategoryDTO result = menuCategoryService
            .partialUpdate(menuCategoryDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuCategoryDTO>> getAllMenuCategories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of MenuCategories");
        long count = menuCategoryService.countAll();
        List<MenuCategoryDTO> entities = menuCategoryService.findAll(pageable);

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
    public ResponseEntity<MenuCategoryDTO> getMenuCategory(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get MenuCategory : {}", id);
        Optional<MenuCategoryDTO> menuCategoryDTO = menuCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuCategoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMenuCategory(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete MenuCategory : {}", id);
        menuCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
