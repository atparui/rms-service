package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.MenuItem;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuItemRepository;
import com.atparui.rmsservice.service.dto.MenuItemDTO;
import com.atparui.rmsservice.service.mapper.MenuItemMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.MenuItem}.
 */
@Service
@Transactional
public class MenuItemService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuItemService.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;
    public MenuItemService(
        MenuItemRepository menuItemRepository,
        MenuItemMapper menuItemMapper
    ) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;    }
    @Transactional
    public Optional<MenuItemDTO> save(MenuItemDTO menuItemDTO) {
        LOG.debug("Request to save MenuItem : {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        menuItem = menuItemRepository.save(menuItem);
        return Optional.of(menuItemMapper.toDto(menuItem));
    }

    @Transactional
    public Optional<MenuItemDTO> update(MenuItemDTO menuItemDTO) {
        LOG.debug("Request to update MenuItem : {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        menuItem = menuItemRepository.save(menuItem);
        return Optional.of(menuItemMapper.toDto(menuItem));
    }

    @Transactional
    public Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO) {
        LOG.debug("Request to partially update MenuItem : {}", menuItemDTO);
        return menuItemRepository
            .findById(menuItemDTO.getId())
            .map(existingMenuItem -> {
                menuItemMapper.partialUpdate(existingMenuItem, menuItemDTO);
                return menuItemRepository.save(existingMenuItem);
            })
            .map(menuItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MenuItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MenuItems");
        Page<MenuItem> page = menuItemRepository.findAll(pageable);
        return page.getContent().stream()
            .map(menuItemMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return menuItemRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> findOne(UUID id) {
        LOG.debug("Request to get MenuItem : {}", id);
        return menuItemRepository.findById(id).map(menuItemMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
    }
}
