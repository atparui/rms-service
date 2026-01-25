package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.AppNavigationMenuItem;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.AppNavigationMenuItemRepository;
import com.atparui.rmsservice.service.dto.AppNavigationMenuItemDTO;
import com.atparui.rmsservice.service.mapper.AppNavigationMenuItemMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.AppNavigationMenuItem}.
 */
@Service
@Transactional
public class AppNavigationMenuItemService {

    private static final Logger LOG = LoggerFactory.getLogger(AppNavigationMenuItemService.class);

    private final AppNavigationMenuItemRepository appNavigationMenuItemRepository;

    private final AppNavigationMenuItemMapper appNavigationMenuItemMapper;

    public AppNavigationMenuItemService(
        AppNavigationMenuItemRepository appNavigationMenuItemRepository,
        AppNavigationMenuItemMapper appNavigationMenuItemMapper
    ) {
        this.appNavigationMenuItemRepository = appNavigationMenuItemRepository;
        this.appNavigationMenuItemMapper = appNavigationMenuItemMapper;
    }
    @Transactional
    public Optional<AppNavigationMenuItemDTO> save(AppNavigationMenuItemDTO appNavigationMenuItemDTO) {
        LOG.debug("Request to save AppNavigationMenuItem : {}", appNavigationMenuItemDTO);
        AppNavigationMenuItem appNavigationMenuItem = appNavigationMenuItemMapper.toEntity(appNavigationMenuItemDTO);
        appNavigationMenuItem = appNavigationMenuItemRepository.save(appNavigationMenuItem);
        return Optional.of(appNavigationMenuItemMapper.toDto(appNavigationMenuItem));
    }

    @Transactional
    public Optional<AppNavigationMenuItemDTO> update(AppNavigationMenuItemDTO appNavigationMenuItemDTO) {
        LOG.debug("Request to update AppNavigationMenuItem : {}", appNavigationMenuItemDTO);
        AppNavigationMenuItem appNavigationMenuItem = appNavigationMenuItemMapper.toEntity(appNavigationMenuItemDTO);
        appNavigationMenuItem = appNavigationMenuItemRepository.save(appNavigationMenuItem);
        return Optional.of(appNavigationMenuItemMapper.toDto(appNavigationMenuItem));
    }

    @Transactional
    public Optional<AppNavigationMenuItemDTO> partialUpdate(AppNavigationMenuItemDTO appNavigationMenuItemDTO) {
        LOG.debug("Request to partially update AppNavigationMenuItem : {}", appNavigationMenuItemDTO);
        return appNavigationMenuItemRepository
            .findById(appNavigationMenuItemDTO.getId())
            .map(existingAppNavigationMenuItem -> {
                appNavigationMenuItemMapper.partialUpdate(existingAppNavigationMenuItem, appNavigationMenuItemDTO);
                return appNavigationMenuItemRepository.save(existingAppNavigationMenuItem);
            })
            .map(appNavigationMenuItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<AppNavigationMenuItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppNavigationMenuItems");
        Page<AppNavigationMenuItem> page = appNavigationMenuItemRepository.findAll(pageable);
        return page.getContent().stream()
            .map(appNavigationMenuItemMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return appNavigationMenuItemRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<AppNavigationMenuItemDTO> findOne(UUID id) {
        LOG.debug("Request to get AppNavigationMenuItem : {}", id);
        return appNavigationMenuItemRepository.findById(id).map(appNavigationMenuItemMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete AppNavigationMenuItem : {}", id);
        appNavigationMenuItemRepository.deleteById(id);
    }
}
