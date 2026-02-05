package com.atparui.rmsservice.service;

import com.atparui.rmsservice.domain.RestaurantRole;
import com.atparui.rmsservice.repository.RestaurantRoleRepository;
import com.atparui.rmsservice.service.dto.RestaurantRoleDTO;
import com.atparui.rmsservice.service.mapper.RestaurantRoleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.RestaurantRole}.
 */
@Service
@Transactional
public class RestaurantRoleService {

    private final Logger log = LoggerFactory.getLogger(RestaurantRoleService.class);

    private final RestaurantRoleRepository restaurantRoleRepository;

    private final RestaurantRoleMapper restaurantRoleMapper;

    public RestaurantRoleService(RestaurantRoleRepository restaurantRoleRepository, RestaurantRoleMapper restaurantRoleMapper) {
        this.restaurantRoleRepository = restaurantRoleRepository;
        this.restaurantRoleMapper = restaurantRoleMapper;
    }

    /**
     * Save a restaurantRole.
     *
     * @param restaurantRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantRoleDTO save(RestaurantRoleDTO restaurantRoleDTO) {
        log.debug("Request to save RestaurantRole : {}", restaurantRoleDTO);
        RestaurantRole restaurantRole = restaurantRoleMapper.toEntity(restaurantRoleDTO);
        restaurantRole = restaurantRoleRepository.save(restaurantRole);
        return restaurantRoleMapper.toDto(restaurantRole);
    }

    /**
     * Update a restaurantRole.
     *
     * @param restaurantRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantRoleDTO update(RestaurantRoleDTO restaurantRoleDTO) {
        log.debug("Request to update RestaurantRole : {}", restaurantRoleDTO);
        RestaurantRole restaurantRole = restaurantRoleMapper.toEntity(restaurantRoleDTO);
        restaurantRole = restaurantRoleRepository.save(restaurantRole);
        return restaurantRoleMapper.toDto(restaurantRole);
    }

    /**
     * Partially update a restaurantRole.
     *
     * @param restaurantRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RestaurantRoleDTO> partialUpdate(RestaurantRoleDTO restaurantRoleDTO) {
        log.debug("Request to partially update RestaurantRole : {}", restaurantRoleDTO);

        return restaurantRoleRepository
            .findById(restaurantRoleDTO.getName())
            .map(existingRestaurantRole -> {
                restaurantRoleMapper.partialUpdate(existingRestaurantRole, restaurantRoleDTO);

                return existingRestaurantRole;
            })
            .map(restaurantRoleRepository::save)
            .map(restaurantRoleMapper::toDto);
    }

    /**
     * Get all the restaurantRoles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantRoleDTO> findAll() {
        log.debug("Request to get all RestaurantRoles");
        return restaurantRoleRepository
            .findAllByOrderBySortOrder()
            .stream()
            .map(restaurantRoleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all active restaurant roles.
     *
     * @return the list of active entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantRoleDTO> findAllActive() {
        log.debug("Request to get all active RestaurantRoles");
        return restaurantRoleRepository
            .findAllByIsActiveTrueOrderBySortOrder()
            .stream()
            .map(restaurantRoleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one restaurantRole by id.
     *
     * @param name the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RestaurantRoleDTO> findOne(String name) {
        log.debug("Request to get RestaurantRole : {}", name);
        return restaurantRoleRepository.findById(name).map(restaurantRoleMapper::toDto);
    }

    /**
     * Delete the restaurantRole by id.
     *
     * @param name the id of the entity.
     */
    public void delete(String name) {
        log.debug("Request to delete RestaurantRole : {}", name);
        restaurantRoleRepository.deleteById(name);
    }
}
