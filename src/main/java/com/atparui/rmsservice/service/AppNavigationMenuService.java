package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.domain.AppNavigationMenu;
import com.atparui.rmsservice.domain.AppNavigationMenuItem;
import com.atparui.rmsservice.domain.AppNavigationMenuRole;
import com.atparui.rmsservice.repository.AppNavigationMenuItemRepository;
import com.atparui.rmsservice.repository.AppNavigationMenuRepository;
import com.atparui.rmsservice.repository.AppNavigationMenuRoleRepository;
import com.atparui.rmsservice.service.dto.AppNavigationMenuItemDTO;
import com.atparui.rmsservice.service.dto.AppNavigationMenuDTO;
import com.atparui.rmsservice.service.dto.AppNavigationMenuResponseDTO;
import com.atparui.rmsservice.service.mapper.AppNavigationMenuItemMapper;
import com.atparui.rmsservice.service.mapper.AppNavigationMenuMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service for managing role-based application navigation menus (CMS)
 * This is different from restaurant menu - this is the app navigation menu
 */
@Service
@Transactional
public class AppNavigationMenuService {

    private static final Logger LOG = LoggerFactory.getLogger(AppNavigationMenuService.class);

    private final AppNavigationMenuRepository appNavigationMenuRepository;
    private final AppNavigationMenuItemRepository appNavigationMenuItemRepository;
    private final AppNavigationMenuRoleRepository appNavigationMenuRoleRepository;
    private final AppNavigationMenuItemMapper appNavigationMenuItemMapper;
    private final AppNavigationMenuMapper appNavigationMenuMapper;

    public AppNavigationMenuService(
        AppNavigationMenuRepository appNavigationMenuRepository,
        AppNavigationMenuItemRepository appNavigationMenuItemRepository,
        AppNavigationMenuRoleRepository appNavigationMenuRoleRepository,
        AppNavigationMenuItemMapper appNavigationMenuItemMapper,
        AppNavigationMenuMapper appNavigationMenuMapper
    ) {
        this.appNavigationMenuRepository = appNavigationMenuRepository;
        this.appNavigationMenuItemRepository = appNavigationMenuItemRepository;
        this.appNavigationMenuRoleRepository = appNavigationMenuRoleRepository;
        this.appNavigationMenuItemMapper = appNavigationMenuItemMapper;
        this.appNavigationMenuMapper = appNavigationMenuMapper;
    }
    @Transactional
    public Optional<AppNavigationMenuDTO> save(AppNavigationMenuDTO appNavigationMenuDTO) {
        LOG.debug("Request to save AppNavigationMenu : {}", appNavigationMenuDTO);
        AppNavigationMenu appNavigationMenu = appNavigationMenuMapper.toEntity(appNavigationMenuDTO);
        appNavigationMenu = appNavigationMenuRepository.save(appNavigationMenu);
        return Optional.of(appNavigationMenuMapper.toDto(appNavigationMenu));
    }

    @Transactional
    public Optional<AppNavigationMenuDTO> update(AppNavigationMenuDTO appNavigationMenuDTO) {
        LOG.debug("Request to update AppNavigationMenu : {}", appNavigationMenuDTO);
        AppNavigationMenu appNavigationMenu = appNavigationMenuMapper.toEntity(appNavigationMenuDTO);
        appNavigationMenu = appNavigationMenuRepository.save(appNavigationMenu);
        return Optional.of(appNavigationMenuMapper.toDto(appNavigationMenu));
    }

    @Transactional
    public Optional<AppNavigationMenuDTO> partialUpdate(AppNavigationMenuDTO appNavigationMenuDTO) {
        LOG.debug("Request to partially update AppNavigationMenu : {}", appNavigationMenuDTO);
        return appNavigationMenuRepository
            .findById(appNavigationMenuDTO.getId())
            .map(existingAppNavigationMenu -> {
                appNavigationMenuMapper.partialUpdate(existingAppNavigationMenu, appNavigationMenuDTO);
                return appNavigationMenuRepository.save(existingAppNavigationMenu);
            })
            .map(appNavigationMenuMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<AppNavigationMenuDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppNavigationMenus");
        Page<AppNavigationMenu> page = appNavigationMenuRepository.findAll(pageable);
        return page.getContent().stream()
            .map(appNavigationMenuMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return appNavigationMenuRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<AppNavigationMenuDTO> findOne(UUID id) {
        LOG.debug("Request to get AppNavigationMenu : {}", id);
        return appNavigationMenuRepository.findById(id).map(appNavigationMenuMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete AppNavigationMenu : {}", id);
        appNavigationMenuRepository.deleteById(id);
    }

    /**
     * Get navigation menu with items filtered by roles
     *
     * @param roles the list of roles to filter by
     * @return the navigation menu response DTO with menus and items
     */
    @Transactional(readOnly = true)
    public Optional<AppNavigationMenuResponseDTO> getNavigationMenuByRoles(List<String> roles) {
        LOG.debug("Request to get application navigation menu with roles {}", roles);

        AppNavigationMenuResponseDTO response = new AppNavigationMenuResponseDTO();

        // Get all active menus
        List<AppNavigationMenu> allMenus = appNavigationMenuRepository.findAll();
        List<AppNavigationMenu> activeMenus = allMenus.stream()
            .filter(menu -> Boolean.TRUE.equals(menu.getIsActive()))
            .collect(Collectors.toList());
        
        // Filter menus by role
        List<AppNavigationMenu> filteredMenus = filterMenusByRoles(activeMenus, roles);
        
        // For each menu, get items filtered by role
        List<AppNavigationMenuResponseDTO.AppNavigationMenuWithItemsDTO> menusWithItems = getItemsForMenus(filteredMenus, roles);
        
        response.setMenus(menusWithItems);
        return Optional.of(response);
    }

    /**
     * Filter menus by roles
     */
    private List<AppNavigationMenu> filterMenusByRoles(List<AppNavigationMenu> menus, List<String> roles) {
        if (menus.isEmpty()) {
            return Collections.emptyList();
        }

        // Get all menu role mappings for the given roles
        List<AppNavigationMenuRole> roleMappings = roles.stream()
            .flatMap(role -> appNavigationMenuRoleRepository.findByRole(role).stream())
            .collect(Collectors.toList());
        
        Set<UUID> allowedMenuIds = roleMappings
            .stream()
            .filter(roleMapping -> roleMapping.getAppNavigationMenuId() != null)
            .map(AppNavigationMenuRole::getAppNavigationMenuId)
            .collect(Collectors.toSet());

        // If no role mappings exist, return all menus (backward compatibility)
        if (allowedMenuIds.isEmpty()) {
            return menus;
        }

        return menus.stream()
            .filter(menu -> allowedMenuIds.contains(menu.getId()))
            .collect(Collectors.toList());
    }

    /**
     * Get items for menus filtered by roles
     */
    private List<AppNavigationMenuResponseDTO.AppNavigationMenuWithItemsDTO> getItemsForMenus(
        List<AppNavigationMenu> menus,
        List<String> roles
    ) {
        if (menus.isEmpty()) {
            return Collections.emptyList();
        }

        // Get all item role mappings for the given roles
        List<AppNavigationMenuRole> roleMappings = roles.stream()
            .flatMap(role -> appNavigationMenuRoleRepository.findByRole(role).stream())
            .collect(Collectors.toList());
            
        Set<UUID> allowedItemIds = roleMappings
            .stream()
            .filter(roleMapping -> roleMapping.getAppNavigationMenuItemId() != null)
            .map(AppNavigationMenuRole::getAppNavigationMenuItemId)
            .collect(Collectors.toSet());

        // Get all active items
        List<AppNavigationMenuItem> items = appNavigationMenuItemRepository.findAll()
            .stream()
            .filter(item -> Boolean.TRUE.equals(item.getIsActive()))
            .collect(Collectors.toList());
        
        // Filter items by role if role mappings exist
        List<AppNavigationMenuItem> filteredItems = allowedItemIds.isEmpty()
            ? items
            : items.stream().filter(item -> allowedItemIds.contains(item.getId())).collect(Collectors.toList());

        // Group items by parent menu
        Map<UUID, List<AppNavigationMenuItem>> itemsByMenu = filteredItems
            .stream()
            .filter(item -> item.getParentMenuId() != null)
            .collect(Collectors.groupingBy(AppNavigationMenuItem::getParentMenuId));

        // Build menu DTOs with items
        List<AppNavigationMenuResponseDTO.AppNavigationMenuWithItemsDTO> menuList = menus.stream()
            .map(menu -> {
                AppNavigationMenuResponseDTO.AppNavigationMenuWithItemsDTO menuDTO =
                    new AppNavigationMenuResponseDTO.AppNavigationMenuWithItemsDTO();
                menuDTO.setId(menu.getId());
                menuDTO.setMenuCode(menu.getMenuCode());
                menuDTO.setMenuName(menu.getMenuName());
                menuDTO.setDescription(menu.getDescription());
                menuDTO.setMenuType(menu.getMenuType());
                menuDTO.setIcon(menu.getIcon());
                menuDTO.setRoutePath(menu.getRoutePath());
                menuDTO.setDisplayOrder(menu.getDisplayOrder());
                menuDTO.setIsActive(menu.getIsActive());

                // Get items for this menu
                List<AppNavigationMenuItem> menuItems = itemsByMenu.getOrDefault(menu.getId(), Collections.emptyList());
                List<AppNavigationMenuItemDTO> itemDTOs = menuItems
                    .stream()
                    .sorted(
                        Comparator.comparing(
                            AppNavigationMenuItem::getDisplayOrder,
                            Comparator.nullsLast(Comparator.naturalOrder())
                        )
                    )
                    .map(appNavigationMenuItemMapper::toDto)
                    .collect(Collectors.toList());
                menuDTO.setItems(itemDTOs);

                return menuDTO;
            })
            .filter(menuDTO -> !menuDTO.getItems().isEmpty()) // Only return menus with items
            .collect(Collectors.toList());
        
        // Sort by display order
        menuList.sort(
            Comparator.comparing(
                AppNavigationMenuResponseDTO.AppNavigationMenuWithItemsDTO::getDisplayOrder,
                Comparator.nullsLast(Comparator.naturalOrder())
            )
        );
        
        return menuList;
    }
}
