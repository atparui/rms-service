package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.MenuPermission;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuPermissionRepository;
import com.atparui.rmsservice.service.dto.MenuPermissionDTO;
import com.atparui.rmsservice.service.mapper.MenuPermissionMapper;
import java.util.Collection;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.MenuPermission}.
 */
@Service
@Transactional
public class MenuPermissionService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuPermissionService.class);

    private final MenuPermissionRepository menuPermissionRepository;
    private final MenuPermissionMapper menuPermissionMapper;

    public MenuPermissionService(MenuPermissionRepository menuPermissionRepository, MenuPermissionMapper menuPermissionMapper) {
        this.menuPermissionRepository = menuPermissionRepository;
        this.menuPermissionMapper = menuPermissionMapper;
    }
    @Transactional
    public Optional<MenuPermissionDTO> save(MenuPermissionDTO menuPermissionDTO) {
        LOG.debug("Request to save MenuPermission : {}", menuPermissionDTO);
        MenuPermission menuPermission = menuPermissionMapper.toEntity(menuPermissionDTO);
        menuPermission = menuPermissionRepository.save(menuPermission);
        return Optional.of(menuPermissionMapper.toDto(menuPermission));
    }

    @Transactional
    public Optional<MenuPermissionDTO> update(MenuPermissionDTO menuPermissionDTO) {
        LOG.debug("Request to update MenuPermission : {}", menuPermissionDTO);
        MenuPermission menuPermission = menuPermissionMapper.toEntity(menuPermissionDTO);
        menuPermission = menuPermissionRepository.save(menuPermission);
        return Optional.of(menuPermissionMapper.toDto(menuPermission));
    }

    @Transactional
    public Optional<MenuPermissionDTO> partialUpdate(MenuPermissionDTO menuPermissionDTO) {
        LOG.debug("Request to partially update MenuPermission : {}", menuPermissionDTO);
        return menuPermissionRepository
            .findById(menuPermissionDTO.getId())
            .map(existingMenuPermission -> {
                menuPermissionMapper.partialUpdate(existingMenuPermission, menuPermissionDTO);
                return menuPermissionRepository.save(existingMenuPermission);
            })
            .map(menuPermissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MenuPermissionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MenuPermissions");
        Page<MenuPermission> page = menuPermissionRepository.findAll(pageable);
        return page.getContent().stream()
            .map(menuPermissionMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return menuPermissionRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<MenuPermissionDTO> findOne(UUID id) {
        LOG.debug("Request to get MenuPermission : {}", id);
        return menuPermissionRepository.findById(id).map(menuPermissionMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete MenuPermission : {}", id);
        menuPermissionRepository.deleteById(id);
    }
}
