package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.AppMenu;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.AppMenuRepository;
import com.atparui.rmsservice.service.dto.AppMenuDTO;
import com.atparui.rmsservice.service.mapper.AppMenuMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.AppMenu}.
 */
@Service
@Transactional
public class AppMenuService {

    private static final Logger LOG = LoggerFactory.getLogger(AppMenuService.class);

    private final AppMenuRepository appMenuRepository;
    private final AppMenuMapper appMenuMapper;

    public AppMenuService(AppMenuRepository appMenuRepository, AppMenuMapper appMenuMapper) {
        this.appMenuRepository = appMenuRepository;
        this.appMenuMapper = appMenuMapper;
    }
    @Transactional
    public Optional<AppMenuDTO> save(AppMenuDTO appMenuDTO) {
        LOG.debug("Request to save AppMenu : {}", appMenuDTO);
        AppMenu appMenu = appMenuMapper.toEntity(appMenuDTO);
        appMenu = appMenuRepository.save(appMenu);
        return Optional.of(appMenuMapper.toDto(appMenu));
    }

    @Transactional
    public Optional<AppMenuDTO> update(AppMenuDTO appMenuDTO) {
        LOG.debug("Request to update AppMenu : {}", appMenuDTO);
        AppMenu appMenu = appMenuMapper.toEntity(appMenuDTO);
        appMenu = appMenuRepository.save(appMenu);
        return Optional.of(appMenuMapper.toDto(appMenu));
    }

    @Transactional
    public Optional<AppMenuDTO> partialUpdate(AppMenuDTO appMenuDTO) {
        LOG.debug("Request to partially update AppMenu : {}", appMenuDTO);
        return appMenuRepository
            .findById(appMenuDTO.getId())
            .map(existingAppMenu -> {
                appMenuMapper.partialUpdate(existingAppMenu, appMenuDTO);
                return appMenuRepository.save(existingAppMenu);
            })
            .map(appMenuMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<AppMenuDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppMenus");
        Page<AppMenu> page = appMenuRepository.findAll(pageable);
        return page.getContent().stream()
            .map(appMenuMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return appMenuRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<AppMenuDTO> findOne(UUID id) {
        LOG.debug("Request to get AppMenu : {}", id);
        return appMenuRepository.findById(id).map(appMenuMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete AppMenu : {}", id);
        appMenuRepository.deleteById(id);
    }
}
