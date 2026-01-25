package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.MenuItemAddon;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuItemAddonRepository;
import com.atparui.rmsservice.service.dto.MenuItemAddonDTO;
import com.atparui.rmsservice.service.mapper.MenuItemAddonMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.MenuItemAddon}.
 */
@Service
@Transactional
public class MenuItemAddonService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuItemAddonService.class);

    private final MenuItemAddonRepository menuItemAddonRepository;

    private final MenuItemAddonMapper menuItemAddonMapper;

    public MenuItemAddonService(MenuItemAddonRepository menuItemAddonRepository, MenuItemAddonMapper menuItemAddonMapper) {
        this.menuItemAddonRepository = menuItemAddonRepository;
        this.menuItemAddonMapper = menuItemAddonMapper;
    }
    @Transactional
    public Optional<MenuItemAddonDTO> save(MenuItemAddonDTO menuItemAddonDTO) {
        LOG.debug("Request to save MenuItemAddon : {}", menuItemAddonDTO);
        MenuItemAddon menuItemAddon = menuItemAddonMapper.toEntity(menuItemAddonDTO);
        menuItemAddon = menuItemAddonRepository.save(menuItemAddon);
        return Optional.of(menuItemAddonMapper.toDto(menuItemAddon));
    }

    @Transactional
    public Optional<MenuItemAddonDTO> update(MenuItemAddonDTO menuItemAddonDTO) {
        LOG.debug("Request to update MenuItemAddon : {}", menuItemAddonDTO);
        MenuItemAddon menuItemAddon = menuItemAddonMapper.toEntity(menuItemAddonDTO);
        menuItemAddon = menuItemAddonRepository.save(menuItemAddon);
        return Optional.of(menuItemAddonMapper.toDto(menuItemAddon));
    }

    @Transactional
    public Optional<MenuItemAddonDTO> partialUpdate(MenuItemAddonDTO menuItemAddonDTO) {
        LOG.debug("Request to partially update MenuItemAddon : {}", menuItemAddonDTO);
        return menuItemAddonRepository
            .findById(menuItemAddonDTO.getId())
            .map(existingMenuItemAddon -> {
                menuItemAddonMapper.partialUpdate(existingMenuItemAddon, menuItemAddonDTO);
                return menuItemAddonRepository.save(existingMenuItemAddon);
            })
            .map(menuItemAddonMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MenuItemAddonDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MenuItemAddons");
        Page<MenuItemAddon> page = menuItemAddonRepository.findAll(pageable);
        return page.getContent().stream()
            .map(menuItemAddonMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return menuItemAddonRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<MenuItemAddonDTO> findOne(UUID id) {
        LOG.debug("Request to get MenuItemAddon : {}", id);
        return menuItemAddonRepository.findById(id).map(menuItemAddonMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete MenuItemAddon : {}", id);
        menuItemAddonRepository.deleteById(id);
    }
}
