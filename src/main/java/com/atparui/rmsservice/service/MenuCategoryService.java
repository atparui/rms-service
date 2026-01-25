package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.MenuCategory;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.MenuCategoryRepository;
import com.atparui.rmsservice.service.dto.MenuCategoryDTO;
import com.atparui.rmsservice.service.mapper.MenuCategoryMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.MenuCategory}.
 */
@Service
@Transactional
public class MenuCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuCategoryService.class);

    private final MenuCategoryRepository menuCategoryRepository;

    private final MenuCategoryMapper menuCategoryMapper;
    public MenuCategoryService(
        MenuCategoryRepository menuCategoryRepository,
        MenuCategoryMapper menuCategoryMapper
    ) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.menuCategoryMapper = menuCategoryMapper;    }
    @Transactional
    public Optional<MenuCategoryDTO> save(MenuCategoryDTO menuCategoryDTO) {
        LOG.debug("Request to save MenuCategory : {}", menuCategoryDTO);
        MenuCategory menuCategory = menuCategoryMapper.toEntity(menuCategoryDTO);
        menuCategory = menuCategoryRepository.save(menuCategory);
        return Optional.of(menuCategoryMapper.toDto(menuCategory));
    }

    @Transactional
    public Optional<MenuCategoryDTO> update(MenuCategoryDTO menuCategoryDTO) {
        LOG.debug("Request to update MenuCategory : {}", menuCategoryDTO);
        MenuCategory menuCategory = menuCategoryMapper.toEntity(menuCategoryDTO);
        menuCategory = menuCategoryRepository.save(menuCategory);
        return Optional.of(menuCategoryMapper.toDto(menuCategory));
    }

    @Transactional
    public Optional<MenuCategoryDTO> partialUpdate(MenuCategoryDTO menuCategoryDTO) {
        LOG.debug("Request to partially update MenuCategory : {}", menuCategoryDTO);
        return menuCategoryRepository
            .findById(menuCategoryDTO.getId())
            .map(existingMenuCategory -> {
                menuCategoryMapper.partialUpdate(existingMenuCategory, menuCategoryDTO);
                return menuCategoryRepository.save(existingMenuCategory);
            })
            .map(menuCategoryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MenuCategoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MenuCategorys");
        Page<MenuCategory> page = menuCategoryRepository.findAll(pageable);
        return page.getContent().stream()
            .map(menuCategoryMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return menuCategoryRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<MenuCategoryDTO> findOne(UUID id) {
        LOG.debug("Request to get MenuCategory : {}", id);
        return menuCategoryRepository.findById(id).map(menuCategoryMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete MenuCategory : {}", id);
        menuCategoryRepository.deleteById(id);
    }
}
