package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.UserSyncLogRepository;
import com.atparui.rmsservice.service.UserSyncLogService;
import com.atparui.rmsservice.service.dto.UserSyncLogDTO;
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
@RequestMapping("/api/user-sync-logs")
public class UserSyncLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserSyncLogResource.class);
    private static final String ENTITY_NAME = "rmsserviceUserSyncLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSyncLogService userSyncLogService;
    private final UserSyncLogRepository userSyncLogRepository;

    public UserSyncLogResource(UserSyncLogService userSyncLogService, UserSyncLogRepository userSyncLogRepository) {
        this.userSyncLogService = userSyncLogService;
        this.userSyncLogRepository = userSyncLogRepository;
    }

    @PostMapping("")
    public ResponseEntity<UserSyncLogDTO> createUserSyncLog(@Valid @RequestBody UserSyncLogDTO userSyncLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserSyncLog : {}", userSyncLogDTO);
        if (userSyncLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSyncLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userSyncLogDTO.setId(UUID.randomUUID());
        UserSyncLogDTO result = userSyncLogService
            .save(userSyncLogDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create user sync log"));

        return ResponseEntity.created(new URI("/api/user-sync-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSyncLogDTO> updateUserSyncLog(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UserSyncLogDTO userSyncLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserSyncLog : {}, {}", id, userSyncLogDTO);
        if (userSyncLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSyncLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSyncLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserSyncLogDTO result = userSyncLogService
            .update(userSyncLogDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSyncLogDTO> partialUpdateUserSyncLog(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UserSyncLogDTO userSyncLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserSyncLog partially : {}, {}", id, userSyncLogDTO);
        if (userSyncLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSyncLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSyncLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserSyncLogDTO result = userSyncLogService
            .partialUpdate(userSyncLogDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSyncLogDTO>> getAllUserSyncLogs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        HttpServletRequest request
    ) {
        LOG.debug("REST request to get a page of UserSyncLogs");
        long count = userSyncLogService.countAll();
        List<UserSyncLogDTO> entities = userSyncLogService.findAll(pageable);

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
    public ResponseEntity<UserSyncLogDTO> getUserSyncLog(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get UserSyncLog : {}", id);
        Optional<UserSyncLogDTO> userSyncLogDTO = userSyncLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSyncLogDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserSyncLog(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete UserSyncLog : {}", id);
        userSyncLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
