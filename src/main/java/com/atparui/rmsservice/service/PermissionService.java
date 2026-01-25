package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

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
    @Transactional
    public Optional<PermissionDTO> save(PermissionDTO permissionDTO) {
        LOG.debug("Request to save Permission : {}", permissionDTO);
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        return Optional.of(permissionMapper.toDto(permission));
    }

    @Transactional
    public Optional<PermissionDTO> update(PermissionDTO permissionDTO) {
        LOG.debug("Request to update Permission : {}", permissionDTO);
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        return Optional.of(permissionMapper.toDto(permission));
    }

    @Transactional
    public Optional<PermissionDTO> partialUpdate(PermissionDTO permissionDTO) {
        LOG.debug("Request to partially update Permission : {}", permissionDTO);
        return permissionRepository
            .findById(permissionDTO.getId())
            .map(existingPermission -> {
                permissionMapper.partialUpdate(existingPermission, permissionDTO);
                return permissionRepository.save(existingPermission);
            })
            .map(permissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<PermissionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Permissions");
        Page<Permission> page = permissionRepository.findAll(pageable);
        return page.getContent().stream()
            .map(permissionMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return permissionRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<PermissionDTO> findOne(UUID id) {
        LOG.debug("Request to get Permission : {}", id);
        return permissionRepository.findById(id).map(permissionMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Permission : {}", id);
        permissionRepository.deleteById(id);
    }
}
