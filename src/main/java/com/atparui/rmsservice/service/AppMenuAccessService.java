package com.atparui.rmsservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atparui.rmsservice.domain.AppMenu;
import com.atparui.rmsservice.domain.MenuPermission;
import com.atparui.rmsservice.domain.Permission;
import com.atparui.rmsservice.domain.RolePermission;
import com.atparui.rmsservice.repository.AppMenuRepository;
import com.atparui.rmsservice.repository.MenuPermissionRepository;
import com.atparui.rmsservice.repository.PermissionRepository;
import com.atparui.rmsservice.security.SecurityUtils;
import com.atparui.rmsservice.service.dto.AppMenuTreeDTO;

import reactor.core.publisher.Mono;

/**
 * Service to assemble the menu tree filtered by permissions.
 */
@Service
@Transactional(readOnly = true)
public class AppMenuAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(AppMenuAccessService.class);

    private final AppMenuRepository appMenuRepository;
    private final RolePermissionService rolePermissionService;
    private final PermissionRepository permissionRepository;
    private final MenuPermissionRepository menuPermissionRepository;

    public AppMenuAccessService(
        AppMenuRepository appMenuRepository,
        RolePermissionService rolePermissionService,
        PermissionRepository permissionRepository,
        MenuPermissionRepository menuPermissionRepository
    ) {
        this.appMenuRepository = appMenuRepository;
        this.rolePermissionService = rolePermissionService;
        this.permissionRepository = permissionRepository;
        this.menuPermissionRepository = menuPermissionRepository;
    }

    /**
     * Build menu tree for the current authenticated user.
     */
    public Mono<List<AppMenuTreeDTO>> getMenuTreeForCurrentUser(String appKey) {
        return SecurityUtils
            .getCurrentUserRoles()
            .collectList()
            .flatMap(roles -> {
                LOG.debug("Menu tree requested. Roles from token: {}", roles);
                return getMenuTreeForRoles(roles, appKey);
            });
    }

    /**
     * Build menu tree for provided roles.
     */
    public Mono<List<AppMenuTreeDTO>> getMenuTreeForRoles(List<String> roles, String appKey) {
        List<String> effectiveRoles = roles == null || roles.isEmpty() ? List.of("ROLE_USER", "ROLE_ANONYMOUS") : roles;
        LOG.debug("Menu tree resolution using roles: {}", effectiveRoles);

        Mono<List<AppMenu>> menusMono = appMenuRepository
            .findAll()
            .filter(menu -> Boolean.TRUE.equals(menu.getIsActive()))
            .filter(menu -> appKey == null || appKey.isBlank() || menu.getAppKey() == null || appKey.equalsIgnoreCase(menu.getAppKey()))
            .collectList()
            .doOnNext(menus -> LOG.debug("Menu query -> menus loaded: {}", menus));

        Mono<List<MenuPermission>> menuPermissionsMono = menuPermissionRepository
            .findAll()
            .collectList()
            .doOnNext(menuPermissions -> LOG.debug("Menu query -> menuPermissions loaded: {}", menuPermissions));

        Mono<Map<UUID, Permission>> permissionMapMono = permissionRepository
            .findAll()
            .collectMap(Permission::getId)
            .doOnNext(permissionMap -> LOG.debug("Menu query -> permissions loaded (by id): {}", permissionMap));

        Mono<Set<UUID>> userPermissionIdsMono = rolePermissionService
            .findByRoles(effectiveRoles)
            .doOnNext(rp -> LOG.debug("RolePermission match -> role: {}, permId: {}, active: {}", rp.getRole(), rp.getPermissionId(), rp.getIsActive()))
            .filter(rp -> rp.getPermissionId() != null)
            .filter(rp -> rp.getIsActive() == null || rp.getIsActive())
            .map(RolePermission::getPermissionId)
            .collect(Collectors.toSet())
            .doOnNext(userPermissionIds -> LOG.debug("Menu query -> userPermissionIds loaded: {}", userPermissionIds));

        return Mono
            .zip(menusMono, menuPermissionsMono, permissionMapMono, userPermissionIdsMono)
            .doOnNext(tuple -> {
                LOG.debug("Menu query inputs -> roles: {}", effectiveRoles);
                LOG.debug("Menu query inputs -> menus: {}", tuple.getT1().size());
                LOG.debug("Menu query inputs -> menuPermissions: {}", tuple.getT2().size());
                LOG.debug("Menu query inputs -> permissions: {}", tuple.getT3().size());
                LOG.debug("Menu query inputs -> userPermissionIds: {}", tuple.getT4());
            })
            .map(tuple -> buildTree(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4()));
    }

    private List<AppMenuTreeDTO> buildTree(
        List<AppMenu> menus,
        List<MenuPermission> menuPermissions,
        Map<UUID, Permission> permissionMap,
        Set<UUID> userPermissionIds
    ) {
        if (menus.isEmpty()) {
            return List.of();
        }

        Map<UUID, Set<UUID>> menuPermissionMap = menuPermissions
            .stream()
            .collect(Collectors.groupingBy(MenuPermission::getAppMenuId, Collectors.mapping(MenuPermission::getPermissionId, Collectors.toSet())));

        Set<UUID> activeUserPermissionIds = userPermissionIds
            .stream()
            .filter(id -> {
                Permission p = permissionMap.get(id);
                return p != null && (p.getIsActive() == null || p.getIsActive());
            })
            .collect(Collectors.toSet());

        LOG.debug(
            "Menu build context -> menus: {}, menuPermissions: {}, activeUserPermissionIds: {}",
            menus.size(),
            menuPermissions.size(),
            activeUserPermissionIds
        );

        Set<UUID> allowedMenuIds = new HashSet<>();
        Map<UUID, AppMenuTreeDTO> dtoMap = new HashMap<>();

        for (AppMenu menu : menus) {
            Set<String> requiredCodes = resolvePermissionCodes(menuPermissionMap.get(menu.getId()), permissionMap);
            boolean allowed = isMenuAllowed(menu, menuPermissionMap.get(menu.getId()), permissionMap, activeUserPermissionIds);
            if (allowed) {
                allowedMenuIds.add(menu.getId());
            }
            AppMenuTreeDTO dto = toTreeDto(menu, requiredCodes);
            dtoMap.put(menu.getId(), dto);
        }

        LOG.debug("Allowed menu ids after evaluation: {}", allowedMenuIds);

        List<AppMenuTreeDTO> roots = new ArrayList<>();
        for (AppMenu menu : menus) {
            if (!allowedMenuIds.contains(menu.getId())) {
                continue;
            }
            AppMenuTreeDTO dto = dtoMap.get(menu.getId());
            UUID parentId = menu.getParentId();
            if (parentId != null && allowedMenuIds.contains(parentId)) {
                AppMenuTreeDTO parentDto = dtoMap.get(parentId);
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                } else {
                    roots.add(dto);
                }
            } else {
                roots.add(dto);
            }
        }

        roots.forEach(this::sortChildren);
        roots.sort(this::compareMenu);
        return roots;
    }

    private Set<String> resolvePermissionCodes(Set<UUID> permissionIds, Map<UUID, Permission> permissionMap) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return Set.of();
        }
        return permissionIds
            .stream()
            .map(permissionMap::get)
            .filter(Objects::nonNull)
            .filter(p -> p.getIsActive() == null || p.getIsActive())
            .map(Permission::getCode)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean isMenuAllowed(
        AppMenu menu,
        Set<UUID> permissionIds,
        Map<UUID, Permission> permissionMap,
        Set<UUID> userPermissionIds
    ) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return true;
        }
        Set<UUID> activeRequired = permissionIds
            .stream()
            .filter(pid -> {
                Permission p = permissionMap.get(pid);
                return p != null && (p.getIsActive() == null || p.getIsActive());
            })
            .collect(Collectors.toSet());

        if (activeRequired.isEmpty()) {
            return true;
        }

        String logic = Optional.ofNullable(menu.getPermissionLogic()).orElse("ANY").toUpperCase(Locale.ROOT);
        if ("ALL".equals(logic)) {
            return userPermissionIds.containsAll(activeRequired);
        }
        return activeRequired.stream().anyMatch(userPermissionIds::contains);
    }

    private AppMenuTreeDTO toTreeDto(AppMenu menu, Set<String> requiredCodes) {
        AppMenuTreeDTO dto = new AppMenuTreeDTO();
        dto.setId(menu.getId());
        dto.setMenuKey(menu.getMenuKey());
        dto.setLabel(menu.getLabel());
        dto.setType(menu.getType());
        dto.setRoutePath(menu.getRoutePath());
        dto.setIcon(menu.getIcon());
        dto.setSortOrder(menu.getSortOrder());
        dto.setRequiredPermissions(new ArrayList<>(requiredCodes));
        return dto;
    }

    private void sortChildren(AppMenuTreeDTO dto) {
        if (dto.getChildren() == null || dto.getChildren().isEmpty()) {
            return;
        }
        dto.getChildren().forEach(this::sortChildren);
        dto.getChildren().sort(this::compareMenu);
    }

    private int compareMenu(AppMenuTreeDTO a, AppMenuTreeDTO b) {
        Integer orderA = a.getSortOrder();
        Integer orderB = b.getSortOrder();
        if (orderA == null && orderB == null) {
            return a.getLabel().compareToIgnoreCase(b.getLabel());
        }
        if (orderA == null) {
            return 1;
        }
        if (orderB == null) {
            return -1;
        }
        int cmp = orderA.compareTo(orderB);
        if (cmp != 0) {
            return cmp;
        }
        return a.getLabel().compareToIgnoreCase(b.getLabel());
    }
}
