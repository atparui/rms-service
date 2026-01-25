package com.atparui.rmsservice.service;
import java.util.stream.Collectors;

import com.atparui.rmsservice.domain.RolePermission;
import com.atparui.rmsservice.repository.RolePermissionRepository;
import com.atparui.rmsservice.service.dto.RolePermissionDTO;
import com.atparui.rmsservice.service.mapper.RolePermissionMapper;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Optional<RolePermissionDTO> save(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to save RolePermission : {}", rolePermissionDTO);
        RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionDTO);
        rolePermission = rolePermissionRepository.save(rolePermission);
        return Optional.of(rolePermissionMapper.toDto(rolePermission));
    }

    @Transactional
    public Optional<RolePermissionDTO> update(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to update RolePermission : {}", rolePermissionDTO);
        RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionDTO);
        rolePermission = rolePermissionRepository.save(rolePermission);
        return Optional.of(rolePermissionMapper.toDto(rolePermission));
    }

    @Transactional
    public Optional<RolePermissionDTO> partialUpdate(RolePermissionDTO rolePermissionDTO) {
        LOG.debug("Request to partially update RolePermission : {}", rolePermissionDTO);
        return rolePermissionRepository
            .findById(rolePermissionDTO.getId())
            .map(existingRolePermission -> {
                rolePermissionMapper.partialUpdate(existingRolePermission, rolePermissionDTO);
                return rolePermissionRepository.save(existingRolePermission);
            })
            .map(rolePermissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<RolePermissionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RolePermissions");
        Page<RolePermission> page = rolePermissionRepository.findAll(pageable);
        return page.getContent().stream()
            .map(rolePermissionMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return rolePermissionRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<RolePermissionDTO> findOne(UUID id) {
        LOG.debug("Request to get RolePermission : {}", id);
        return rolePermissionRepository.findById(id).map(rolePermissionMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete RolePermission : {}", id);
        rolePermissionRepository.deleteById(id);
    }

    /**
     * Find role permissions by roles.
     * Used for menu access control.
     *
     * @param roles the list of roles
     * @return the list of RolePermission entities
     */
    @Transactional(readOnly = true)
    public List<RolePermission> findByRoles(Collection<String> roles) {
        LOG.debug("Request to get RolePermissions by roles : {}", roles);
        return rolePermissionRepository.findByRoleIn(roles);
    }
}
