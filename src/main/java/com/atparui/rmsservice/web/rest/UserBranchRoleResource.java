package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.UserBranchRoleRepository;
import com.atparui.rmsservice.service.UserBranchRoleService;
import com.atparui.rmsservice.service.dto.UserBranchRoleDTO;
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
@RequestMapping("/api/user-branch-roles")
public class UserBranchRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserBranchRoleResource.class);
    private static final String ENTITY_NAME = "rmsserviceUserBranchRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserBranchRoleService userBranchRoleService;
    private final UserBranchRoleRepository userBranchRoleRepository;

    public UserBranchRoleResource(UserBranchRoleService userBranchRoleService, UserBranchRoleRepository userBranchRoleRepository) {
        this.userBranchRoleService = userBranchRoleService;
        this.userBranchRoleRepository = userBranchRoleRepository;
    }

    @PostMapping("")
    public ResponseEntity<UserBranchRoleDTO> createUserBranchRole(@Valid @RequestBody UserBranchRoleDTO userBranchRoleDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserBranchRole : {}", userBranchRoleDTO);
        if (userBranchRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new userBranchRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userBranchRoleDTO.setId(UUID.randomUUID());
        UserBranchRoleDTO result = userBranchRoleService
            .save(userBranchRoleDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create user branch role"));

        return ResponseEntity.created(new URI("/api/user-branch-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserBranchRoleDTO> updateUserBranchRole(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UserBranchRoleDTO userBranchRoleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserBranchRole : {}, {}", id, userBranchRoleDTO);
        if (userBranchRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userBranchRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userBranchRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserBranchRoleDTO result = userBranchRoleService
            .update(userBranchRoleDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserBranchRoleDTO> partialUpdateUserBranchRole(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UserBranchRoleDTO userBranchRoleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserBranchRole partially : {}, {}", id, userBranchRoleDTO);
        if (userBranchRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userBranchRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userBranchRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserBranchRoleDTO result = userBranchRoleService
            .partialUpdate(userBranchRoleDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserBranchRoleDTO>> getAllUserBranchRoles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of UserBranchRoles");
        long count = userBranchRoleService.countAll();
        List<UserBranchRoleDTO> entities = userBranchRoleService.findAll(pageable);

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
    public ResponseEntity<UserBranchRoleDTO> getUserBranchRole(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get UserBranchRole : {}", id);
        Optional<UserBranchRoleDTO> userBranchRoleDTO = userBranchRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userBranchRoleDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserBranchRole(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete UserBranchRole : {}", id);
        userBranchRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
