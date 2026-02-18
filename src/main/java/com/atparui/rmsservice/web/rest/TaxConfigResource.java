package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.repository.TaxConfigRepository;
import com.atparui.rmsservice.service.TaxConfigService;
import com.atparui.rmsservice.service.dto.TaxConfigDTO;
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
@RequestMapping("/api/tax-configs")
public class TaxConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaxConfigResource.class);
    private static final String ENTITY_NAME = "rmsserviceTaxConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxConfigService taxConfigService;
    private final TaxConfigRepository taxConfigRepository;

    public TaxConfigResource(TaxConfigService taxConfigService, TaxConfigRepository taxConfigRepository) {
        this.taxConfigService = taxConfigService;
        this.taxConfigRepository = taxConfigRepository;
    }

    @PostMapping("")
    public ResponseEntity<TaxConfigDTO> createTaxConfig(@Valid @RequestBody TaxConfigDTO taxConfigDTO) throws URISyntaxException {
        LOG.debug("REST request to save TaxConfig : {}", taxConfigDTO);
        if (taxConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taxConfigDTO.setId(UUID.randomUUID());
        TaxConfigDTO result = taxConfigService
            .save(taxConfigDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create tax config"));

        return ResponseEntity.created(new URI("/api/tax-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxConfigDTO> updateTaxConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody TaxConfigDTO taxConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TaxConfig : {}, {}", id, taxConfigDTO);
        if (taxConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaxConfigDTO result = taxConfigService
            .update(taxConfigDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxConfigDTO> partialUpdateTaxConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TaxConfigDTO taxConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TaxConfig partially : {}, {}", id, taxConfigDTO);
        if (taxConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaxConfigDTO result = taxConfigService
            .partialUpdate(taxConfigDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaxConfigDTO>> getAllTaxConfigs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        HttpServletRequest request
    ) {
        LOG.debug("REST request to get a page of TaxConfigs");
        long count = taxConfigService.countAll();
        List<TaxConfigDTO> entities = taxConfigService.findAll(pageable);

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
    public ResponseEntity<TaxConfigDTO> getTaxConfig(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get TaxConfig : {}", id);
        Optional<TaxConfigDTO> taxConfigDTO = taxConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxConfigDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTaxConfig(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete TaxConfig : {}", id);
        taxConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
