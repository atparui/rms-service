package com.atparui.rmsservice.service;

import com.atparui.rmsservice.repository.AppMenuRepository;
import com.atparui.rmsservice.service.dto.AppMenuDTO;
import com.atparui.rmsservice.service.mapper.AppMenuMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<AppMenuDTO> save(AppMenuDTO appMenuDTO) {
        LOG.debug("Request to save AppMenu : {}", appMenuDTO);
        return appMenuRepository.save(appMenuMapper.toEntity(appMenuDTO)).map(appMenuMapper::toDto);
    }

    public Mono<AppMenuDTO> update(AppMenuDTO appMenuDTO) {
        LOG.debug("Request to update AppMenu : {}", appMenuDTO);
        return appMenuRepository.save(appMenuMapper.toEntity(appMenuDTO).setIsPersisted()).map(appMenuMapper::toDto);
    }

    public Mono<AppMenuDTO> partialUpdate(AppMenuDTO appMenuDTO) {
        LOG.debug("Request to partially update AppMenu : {}", appMenuDTO);
        return appMenuRepository
            .findById(appMenuDTO.getId())
            .map(existing -> {
                appMenuMapper.partialUpdate(existing, appMenuDTO);
                return existing;
            })
            .flatMap(appMenuRepository::save)
            .map(appMenuMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<AppMenuDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppMenus");
        return appMenuRepository.findAllBy(pageable).map(appMenuMapper::toDto);
    }

    public Mono<Long> countAll() {
        return appMenuRepository.count();
    }

    @Transactional(readOnly = true)
    public Mono<AppMenuDTO> findOne(UUID id) {
        LOG.debug("Request to get AppMenu : {}", id);
        return appMenuRepository.findById(id).map(appMenuMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete AppMenu : {}", id);
        return appMenuRepository.deleteById(id);
    }
}
