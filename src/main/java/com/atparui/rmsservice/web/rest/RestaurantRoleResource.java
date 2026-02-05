package com.atparui.rmsservice.web.rest;

import com.atparui.rmsservice.repository.RestaurantRoleRepository;
import com.atparui.rmsservice.service.RestaurantRoleService;
import com.atparui.rmsservice.service.dto.RestaurantRoleDTO;
import com.atparui.rmsservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.atparui.rmsservice.web.util.HeaderUtil;
import com.atparui.rmsservice.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.atparui.rmsservice.domain.RestaurantRole}.
 */
@RestController
@RequestMapping("/api/restaurant-roles")
public class RestaurantRoleResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantRoleResource.class);

    private static final String ENTITY_NAME = "restaurantRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurantRoleService restaurantRoleService;

    private final RestaurantRoleRepository restaurantRoleRepository;

    public RestaurantRoleResource(RestaurantRoleService restaurantRoleService, RestaurantRoleRepository restaurantRoleRepository) {
        this.restaurantRoleService = restaurantRoleService;
        this.restaurantRoleRepository = restaurantRoleRepository;
    }

    /**
     * {@code POST  /restaurant-roles} : Create a new restaurantRole.
     *
     * @param restaurantRoleDTO the restaurantRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurantRoleDTO, or with status {@code 400 (Bad Request)} if the restaurantRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RestaurantRoleDTO> createRestaurantRole(@Valid @RequestBody RestaurantRoleDTO restaurantRoleDTO)
        throws URISyntaxException {
        log.debug("REST request to save RestaurantRole : {}", restaurantRoleDTO);
        if (restaurantRoleDTO.getName() == null) {
            throw new BadRequestAlertException("A new restaurantRole must have a name", ENTITY_NAME, "namerequired");
        }
        if (restaurantRoleRepository.existsById(restaurantRoleDTO.getName())) {
            throw new BadRequestAlertException("restaurantRole already exists", ENTITY_NAME, "nameexists");
        }
        restaurantRoleDTO = restaurantRoleService.save(restaurantRoleDTO);
        return ResponseEntity.created(new URI("/api/restaurant-roles/" + restaurantRoleDTO.getName()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, restaurantRoleDTO.getName()))
            .body(restaurantRoleDTO);
    }

    /**
     * {@code PUT  /restaurant-roles/:name} : Updates an existing restaurantRole.
     *
     * @param name the name of the restaurantRoleDTO to save.
     * @param restaurantRoleDTO the restaurantRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantRoleDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurantRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{name}")
    public ResponseEntity<RestaurantRoleDTO> updateRestaurantRole(
        @PathVariable(value = "name", required = false) final String name,
        @Valid @RequestBody RestaurantRoleDTO restaurantRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RestaurantRole : {}, {}", name, restaurantRoleDTO);
        if (restaurantRoleDTO.getName() == null) {
            throw new BadRequestAlertException("Invalid name", ENTITY_NAME, "namenull");
        }
        if (!Objects.equals(name, restaurantRoleDTO.getName())) {
            throw new BadRequestAlertException("Invalid name", ENTITY_NAME, "nameinvalid");
        }

        if (!restaurantRoleRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "namenotfound");
        }

        restaurantRoleDTO = restaurantRoleService.update(restaurantRoleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, restaurantRoleDTO.getName()))
            .body(restaurantRoleDTO);
    }

    /**
     * {@code PATCH  /restaurant-roles/:name} : Partial updates given fields of an existing restaurantRole, field will ignore if it is null
     *
     * @param name the name of the restaurantRoleDTO to save.
     * @param restaurantRoleDTO the restaurantRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantRoleDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the restaurantRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurantRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{name}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RestaurantRoleDTO> partialUpdateRestaurantRole(
        @PathVariable(value = "name", required = false) final String name,
        @NotNull @RequestBody RestaurantRoleDTO restaurantRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RestaurantRole partially : {}, {}", name, restaurantRoleDTO);
        if (restaurantRoleDTO.getName() == null) {
            throw new BadRequestAlertException("Invalid name", ENTITY_NAME, "namenull");
        }
        if (!Objects.equals(name, restaurantRoleDTO.getName())) {
            throw new BadRequestAlertException("Invalid name", ENTITY_NAME, "nameinvalid");
        }

        if (!restaurantRoleRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "namenotfound");
        }

        Optional<RestaurantRoleDTO> result = restaurantRoleService.partialUpdate(restaurantRoleDTO);

        return result
            .map(dto -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dto.getName()))
                .body(dto))
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "namenotfound"));
    }

    /**
     * {@code GET  /restaurant-roles} : get all the restaurantRoles.
     *
     * @param activeOnly return only active roles if true.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurantRoles in body.
     */
    @GetMapping("")
    public List<RestaurantRoleDTO> getAllRestaurantRoles(@RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        log.debug("REST request to get all RestaurantRoles, activeOnly: {}", activeOnly);
        if (activeOnly) {
            return restaurantRoleService.findAllActive();
        }
        return restaurantRoleService.findAll();
    }

    /**
     * {@code GET  /restaurant-roles/:name} : get the "name" restaurantRole.
     *
     * @param name the name of the restaurantRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurantRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{name}")
    public ResponseEntity<RestaurantRoleDTO> getRestaurantRole(@PathVariable("name") String name) {
        log.debug("REST request to get RestaurantRole : {}", name);
        Optional<RestaurantRoleDTO> restaurantRoleDTO = restaurantRoleService.findOne(name);
        return ResponseUtil.wrapOrNotFound(restaurantRoleDTO);
    }

    /**
     * {@code DELETE  /restaurant-roles/:name} : delete the "name" restaurantRole.
     *
     * @param name the name of the restaurantRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteRestaurantRole(@PathVariable("name") String name) {
        log.debug("REST request to delete RestaurantRole : {}", name);
        restaurantRoleService.delete(name);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, name))
            .build();
    }
}
