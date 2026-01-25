package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.Restaurant;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.RestaurantRepository;
import com.atparui.rmsservice.service.dto.RestaurantDTO;
import com.atparui.rmsservice.service.mapper.RestaurantMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Restaurant}.
 */
@Service
@Transactional
public class RestaurantService {

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;
    public RestaurantService(
        RestaurantRepository restaurantRepository,
        RestaurantMapper restaurantMapper
    ) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;    }
    @Transactional
    public Optional<RestaurantDTO> save(RestaurantDTO restaurantDTO) {
        LOG.debug("Request to save Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        return Optional.of(restaurantMapper.toDto(restaurant));
    }

    @Transactional
    public Optional<RestaurantDTO> update(RestaurantDTO restaurantDTO) {
        LOG.debug("Request to update Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        return Optional.of(restaurantMapper.toDto(restaurant));
    }

    @Transactional
    public Optional<RestaurantDTO> partialUpdate(RestaurantDTO restaurantDTO) {
        LOG.debug("Request to partially update Restaurant : {}", restaurantDTO);
        return restaurantRepository
            .findById(restaurantDTO.getId())
            .map(existingRestaurant -> {
                restaurantMapper.partialUpdate(existingRestaurant, restaurantDTO);
                return restaurantRepository.save(existingRestaurant);
            })
            .map(restaurantMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<RestaurantDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Restaurants");
        Page<Restaurant> page = restaurantRepository.findAll(pageable);
        return page.getContent().stream()
            .map(restaurantMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return restaurantRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<RestaurantDTO> findOne(UUID id) {
        LOG.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findById(id).map(restaurantMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
    }
}
