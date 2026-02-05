package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.MenuItemAddon;
import com.atparui.rmsservice.domain.OrderItem;
import com.atparui.rmsservice.domain.OrderItemCustomization;
import com.atparui.rmsservice.service.dto.MenuItemAddonDTO;
import com.atparui.rmsservice.service.dto.OrderItemCustomizationDTO;
import com.atparui.rmsservice.service.dto.OrderItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItemCustomization} and its DTO {@link OrderItemCustomizationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemCustomizationMapper extends EntityMapper<OrderItemCustomizationDTO, OrderItemCustomization> {
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "orderItemId")
    @Mapping(target = "menuItemAddon", source = "menuItemAddon", qualifiedByName = "menuItemAddonId")
    OrderItemCustomizationDTO toDto(OrderItemCustomization s);

    @Named("orderItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderItemDTO toDtoOrderItemId(OrderItem orderItem);

    @Named("menuItemAddonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuItemAddonDTO toDtoMenuItemAddonId(MenuItemAddon menuItemAddon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "orderItemId", ignore = true)
    @Mapping(target = "menuItemAddonId", ignore = true)
    OrderItemCustomization toEntity(OrderItemCustomizationDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "orderItemId", ignore = true)
    @Mapping(target = "menuItemAddonId", ignore = true)
    void partialUpdate(@MappingTarget OrderItemCustomization entity, OrderItemCustomizationDTO dto);
}
