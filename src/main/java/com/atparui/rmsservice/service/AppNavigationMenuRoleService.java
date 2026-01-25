package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.AppNavigationMenuRole;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.AppNavigationMenuRoleRepository;
import com.atparui.rmsservice.service.dto.AppNavigationMenuRoleDTO;
import com.atparui.rmsservice.service.mapper.AppNavigationMenuRoleMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.AppNavigationMenuRole}.
 */
@Service
@Transactional
public class AppNavigationMenuRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(AppNavigationMenuRoleService.class);

    private final AppNavigationMenuRoleRepository appNavigationMenuRoleRepository;

    private final AppNavigationMenuRoleMapper appNavigationMenuRoleMapper;

    public AppNavigationMenuRoleService(
        AppNavigationMenuRoleRepository appNavigationMenuRoleRepository,
        AppNavigationMenuRoleMapper appNavigationMenuRoleMapper
    ) {
        this.appNavigationMenuRoleRepository = appNavigationMenuRoleRepository;
        this.appNavigationMenuRoleMapper = appNavigationMenuRoleMapper;
    }
    @Transactional
    public Optional<AppNavigationMenuRoleDTO> save(AppNavigationMenuRoleDTO appNavigationMenuRoleDTO) {
        LOG.debug("Request to save AppNavigationMenuRole : {}", appNavigationMenuRoleDTO);
        AppNavigationMenuRole appNavigationMenuRole = appNavigationMenuRoleMapper.toEntity(appNavigationMenuRoleDTO);
        appNavigationMenuRole = appNavigationMenuRoleRepository.save(appNavigationMenuRole);
        return Optional.of(appNavigationMenuRoleMapper.toDto(appNavigationMenuRole));
    }

    @Transactional
    public Optional<AppNavigationMenuRoleDTO> update(AppNavigationMenuRoleDTO appNavigationMenuRoleDTO) {
        LOG.debug("Request to update AppNavigationMenuRole : {}", appNavigationMenuRoleDTO);
        AppNavigationMenuRole appNavigationMenuRole = appNavigationMenuRoleMapper.toEntity(appNavigationMenuRoleDTO);
        appNavigationMenuRole = appNavigationMenuRoleRepository.save(appNavigationMenuRole);
        return Optional.of(appNavigationMenuRoleMapper.toDto(appNavigationMenuRole));
    }

    @Transactional
    public Optional<AppNavigationMenuRoleDTO> partialUpdate(AppNavigationMenuRoleDTO appNavigationMenuRoleDTO) {
        LOG.debug("Request to partially update AppNavigationMenuRole : {}", appNavigationMenuRoleDTO);
        return appNavigationMenuRoleRepository
            .findById(appNavigationMenuRoleDTO.getId())
            .map(existingAppNavigationMenuRole -> {
                appNavigationMenuRoleMapper.partialUpdate(existingAppNavigationMenuRole, appNavigationMenuRoleDTO);
                return appNavigationMenuRoleRepository.save(existingAppNavigationMenuRole);
            })
            .map(appNavigationMenuRoleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<AppNavigationMenuRoleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppNavigationMenuRoles");
        Page<AppNavigationMenuRole> page = appNavigationMenuRoleRepository.findAll(pageable);
        return page.getContent().stream()
            .map(appNavigationMenuRoleMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return appNavigationMenuRoleRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<AppNavigationMenuRoleDTO> findOne(UUID id) {
        LOG.debug("Request to get AppNavigationMenuRole : {}", id);
        return appNavigationMenuRoleRepository.findById(id).map(appNavigationMenuRoleMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete AppNavigationMenuRole : {}", id);
        appNavigationMenuRoleRepository.deleteById(id);
    }
}
