package com.atparui.rmsservice.service;

import com.atparui.rmsservice.domain.Permission;
import com.atparui.rmsservice.repository.PermissionRepository;
import com.atparui.rmsservice.service.dto.PermissionDTO;
import com.atparui.rmsservice.service.mapper.PermissionMapper;
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
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Permission}.
 */
@Service
@Transactional
public class PermissionService {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionService.class);

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public Mono<PermissionDTO> save(PermissionDTO permissionDTO) {
        LOG.debug("Request to save Permission : {}", permissionDTO);
        return permissionRepository.save(permissionMapper.toEntity(permissionDTO)).map(permissionMapper::toDto);
    }

    public Mono<PermissionDTO> update(PermissionDTO permissionDTO) {
        LOG.debug("Request to update Permission : {}", permissionDTO);
        return permissionRepository.save(permissionMapper.toEntity(permissionDTO).setIsPersisted()).map(permissionMapper::toDto);
    }

    public Mono<PermissionDTO> partialUpdate(PermissionDTO permissionDTO) {
        LOG.debug("Request to partially update Permission : {}", permissionDTO);
        return permissionRepository
            .findById(permissionDTO.getId())
            .map(existing -> {
                permissionMapper.partialUpdate(existing, permissionDTO);
                return existing;
            })
            .flatMap(permissionRepository::save)
            .map(permissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<PermissionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Permissions");
        return permissionRepository.findAllBy(pageable).map(permissionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return permissionRepository.count();
    }

    @Transactional(readOnly = true)
    public Mono<PermissionDTO> findOne(UUID id) {
        LOG.debug("Request to get Permission : {}", id);
        return permissionRepository.findById(id).map(permissionMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete Permission : {}", id);
        return permissionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Flux<Permission> findByCodes(Collection<String> codes) {
        return permissionRepository.findByCodeIn(codes);
    }
}
