package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Branch;
import com.atparui.rmsservice.domain.MenuCategory;
import com.atparui.rmsservice.domain.MenuItem;
import com.atparui.rmsservice.service.dto.BranchDTO;
import com.atparui.rmsservice.service.dto.MenuCategoryDTO;
import com.atparui.rmsservice.service.dto.MenuItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuItem} and its DTO {@link MenuItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuItemMapper extends EntityMapper<MenuItemDTO, MenuItem> {
    @Mapping(target = "branch", source = "branch", qualifiedByName = "branchId")
    @Mapping(target = "menuCategory", source = "menuCategory", qualifiedByName = "menuCategoryId")
    MenuItemDTO toDto(MenuItem s);

    @Named("branchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoBranchId(Branch branch);

    @Named("menuCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuCategoryDTO toDtoMenuCategoryId(MenuCategory menuCategory);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "menuCategoryId", ignore = true)
    MenuItem toEntity(MenuItemDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "menuCategoryId", ignore = true)
    void partialUpdate(@MappingTarget MenuItem entity, MenuItemDTO dto);
}
