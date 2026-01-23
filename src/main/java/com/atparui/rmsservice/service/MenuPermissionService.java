package com.atparui.rmsservice.service;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<MenuPermissionDTO> save(MenuPermissionDTO menuPermissionDTO) {
        LOG.debug("Request to save MenuPermission : {}", menuPermissionDTO);
        return menuPermissionRepository.save(menuPermissionMapper.toEntity(menuPermissionDTO)).map(menuPermissionMapper::toDto);
    }

    public Mono<MenuPermissionDTO> update(MenuPermissionDTO menuPermissionDTO) {
        LOG.debug("Request to update MenuPermission : {}", menuPermissionDTO);
        return menuPermissionRepository.save(menuPermissionMapper.toEntity(menuPermissionDTO).setIsPersisted()).map(menuPermissionMapper::toDto);
    }

    public Mono<MenuPermissionDTO> partialUpdate(MenuPermissionDTO menuPermissionDTO) {
        LOG.debug("Request to partially update MenuPermission : {}", menuPermissionDTO);
        return menuPermissionRepository
            .findById(menuPermissionDTO.getId())
            .map(existing -> {
                menuPermissionMapper.partialUpdate(existing, menuPermissionDTO);
                return existing;
            })
            .flatMap(menuPermissionRepository::save)
            .map(menuPermissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<MenuPermissionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MenuPermissions");
        return menuPermissionRepository.findAllBy(pageable).map(menuPermissionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return menuPermissionRepository.count();
    }

    @Transactional(readOnly = true)
    public Mono<MenuPermissionDTO> findOne(UUID id) {
        LOG.debug("Request to get MenuPermission : {}", id);
        return menuPermissionRepository.findById(id).map(menuPermissionMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete MenuPermission : {}", id);
        return menuPermissionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Flux<com.atparui.rmsservice.domain.MenuPermission> findByMenuIds(Collection<UUID> menuIds) {
        return menuPermissionRepository.findByAppMenuIdIn(menuIds);
    }
}
