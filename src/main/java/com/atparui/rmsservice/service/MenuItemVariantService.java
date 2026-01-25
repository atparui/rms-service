package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.MenuItemVariant;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuItemVariantRepository;
import com.atparui.rmsservice.service.dto.MenuItemVariantDTO;
import com.atparui.rmsservice.service.mapper.MenuItemVariantMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.MenuItemVariant}.
 */
@Service
@Transactional
public class MenuItemVariantService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuItemVariantService.class);

    private final MenuItemVariantRepository menuItemVariantRepository;

    private final MenuItemVariantMapper menuItemVariantMapper;

    public MenuItemVariantService(MenuItemVariantRepository menuItemVariantRepository, MenuItemVariantMapper menuItemVariantMapper) {
        this.menuItemVariantRepository = menuItemVariantRepository;
        this.menuItemVariantMapper = menuItemVariantMapper;
    }
    @Transactional
    public Optional<MenuItemVariantDTO> save(MenuItemVariantDTO menuItemVariantDTO) {
        LOG.debug("Request to save MenuItemVariant : {}", menuItemVariantDTO);
        MenuItemVariant menuItemVariant = menuItemVariantMapper.toEntity(menuItemVariantDTO);
        menuItemVariant = menuItemVariantRepository.save(menuItemVariant);
        return Optional.of(menuItemVariantMapper.toDto(menuItemVariant));
    }

    @Transactional
    public Optional<MenuItemVariantDTO> update(MenuItemVariantDTO menuItemVariantDTO) {
        LOG.debug("Request to update MenuItemVariant : {}", menuItemVariantDTO);
        MenuItemVariant menuItemVariant = menuItemVariantMapper.toEntity(menuItemVariantDTO);
        menuItemVariant = menuItemVariantRepository.save(menuItemVariant);
        return Optional.of(menuItemVariantMapper.toDto(menuItemVariant));
    }

    @Transactional
    public Optional<MenuItemVariantDTO> partialUpdate(MenuItemVariantDTO menuItemVariantDTO) {
        LOG.debug("Request to partially update MenuItemVariant : {}", menuItemVariantDTO);
        return menuItemVariantRepository
            .findById(menuItemVariantDTO.getId())
            .map(existingMenuItemVariant -> {
                menuItemVariantMapper.partialUpdate(existingMenuItemVariant, menuItemVariantDTO);
                return menuItemVariantRepository.save(existingMenuItemVariant);
            })
            .map(menuItemVariantMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MenuItemVariantDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MenuItemVariants");
        Page<MenuItemVariant> page = menuItemVariantRepository.findAll(pageable);
        return page.getContent().stream()
            .map(menuItemVariantMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return menuItemVariantRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<MenuItemVariantDTO> findOne(UUID id) {
        LOG.debug("Request to get MenuItemVariant : {}", id);
        return menuItemVariantRepository.findById(id).map(menuItemVariantMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete MenuItemVariant : {}", id);
        menuItemVariantRepository.deleteById(id);
    }
}
