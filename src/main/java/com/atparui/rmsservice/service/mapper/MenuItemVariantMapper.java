package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.MenuItem;
import com.atparui.rmsservice.domain.MenuItemVariant;
import com.atparui.rmsservice.service.dto.MenuItemDTO;
import com.atparui.rmsservice.service.dto.MenuItemVariantDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuItemVariant} and its DTO {@link MenuItemVariantDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuItemVariantMapper extends EntityMapper<MenuItemVariantDTO, MenuItemVariant> {
    @Mapping(target = "menuItem", source = "menuItem", qualifiedByName = "menuItemId")
    MenuItemVariantDTO toDto(MenuItemVariant s);

    @Named("menuItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuItemDTO toDtoMenuItemId(MenuItem menuItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "menuItemId", ignore = true)
    MenuItemVariant toEntity(MenuItemVariantDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "menuItemId", ignore = true)
    void partialUpdate(@MappingTarget MenuItemVariant entity, MenuItemVariantDTO dto);
}
