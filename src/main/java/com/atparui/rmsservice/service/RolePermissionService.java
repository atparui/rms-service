package com.atparui.rmsservice.service;

import com.atparui.rmsservice.domain.RolePermission;
import com.atparui.rmsservice.repository.RolePermissionRepository;
import com.atparui.rmsservice.service.dto.RolePermissionDTO;
import com.atparui.rmsservice.service.mapper.RolePermissionMapper;
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
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.RolePermission}.
 */
@Service
@Transactional
public class RolePermissionService {

    private static final Logger LOG = LoggerFactory.getLogger(RolePermissionService.class);

    private final RolePermissionRepository rolePermissionRepository;
    private final RolePermissionMapper rolePermissionMapper;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository, RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    public Mono<RolePermissionDTO> save(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to save RolePermission : {}", rolePermissionDTO);
        return rolePermissionRepository.save(rolePermissionMapper.toEntity(rolePermissionDTO)).map(rolePermissionMapper::toDto);
    }

    public Mono<RolePermissionDTO> update(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to update RolePermission : {}", rolePermissionDTO);
        return rolePermissionRepository.save(rolePermissionMapper.toEntity(rolePermissionDTO).setIsPersisted()).map(rolePermissionMapper::toDto);
    }

    public Mono<RolePermissionDTO> partialUpdate(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to partially update RolePermission : {}", rolePermissionDTO);
        return rolePermissionRepository
            .findById(rolePermissionDTO.getId())
            .map(existing -> {
                rolePermissionMapper.partialUpdate(existing, rolePermissionDTO);
                return existing;
            })
            .flatMap(rolePermissionRepository::save)
            .map(rolePermissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<RolePermissionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RolePermissions");
        return rolePermissionRepository.findAllBy(pageable).map(rolePermissionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return rolePermissionRepository.count();
    }

    @Transactional(readOnly = true)
    public Mono<RolePermissionDTO> findOne(UUID id) {
        LOG.debug("Request to get RolePermission : {}", id);
        return rolePermissionRepository.findById(id).map(rolePermissionMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete RolePermission : {}", id);
        return rolePermissionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Flux<RolePermission> findByRoles(Collection<String> roles) {
        return rolePermissionRepository.findByRoleIn(roles);
    }
}
