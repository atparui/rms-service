package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.RolePermissionRepository;
import com.atparui.rmsservice.service.RolePermissionService;
import com.atparui.rmsservice.service.dto.RolePermissionDTO;
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
@RequestMapping("/api/role-permissions")
public class RolePermissionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RolePermissionResource.class);
    private static final String ENTITY_NAME = "rmsserviceRolePermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RolePermissionService rolePermissionService;
    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionResource(RolePermissionService rolePermissionService, RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionService = rolePermissionService;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @PostMapping("")
    public ResponseEntity<RolePermissionDTO> createRolePermission(@Valid @RequestBody RolePermissionDTO rolePermissionDTO) throws URISyntaxException {
        LOG.debug("REST request to save RolePermission : {}", rolePermissionDTO);
        if (rolePermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new rolePermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rolePermissionDTO.setId(UUID.randomUUID());
        RolePermissionDTO result = rolePermissionService
            .save(rolePermissionDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create role permission"));

        return ResponseEntity.created(new URI("/api/role-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolePermissionDTO> updateRolePermission(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody RolePermissionDTO rolePermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RolePermission : {}, {}", id, rolePermissionDTO);
        if (rolePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolePermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolePermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RolePermissionDTO result = rolePermissionService
            .update(rolePermissionDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RolePermissionDTO> partialUpdateRolePermission(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RolePermissionDTO rolePermissionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RolePermission partially : {}, {}", id, rolePermissionDTO);
        if (rolePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolePermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolePermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RolePermissionDTO result = rolePermissionService
            .partialUpdate(rolePermissionDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RolePermissionDTO>> getAllRolePermissions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        HttpServletRequest request
    ) {
        LOG.debug("REST request to get a page of RolePermissions");
        long count = rolePermissionService.countAll();
        List<RolePermissionDTO> entities = rolePermissionService.findAll(pageable);

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
    public ResponseEntity<RolePermissionDTO> getRolePermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get RolePermission : {}", id);
        Optional<RolePermissionDTO> rolePermissionDTO = rolePermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rolePermissionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRolePermission(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete RolePermission : {}", id);
        rolePermissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
