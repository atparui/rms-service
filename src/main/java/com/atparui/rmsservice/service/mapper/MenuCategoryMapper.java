package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.MenuCategory;
import com.atparui.rmsservice.domain.Restaurant;
import com.atparui.rmsservice.service.dto.MenuCategoryDTO;
import com.atparui.rmsservice.service.dto.RestaurantDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuCategory} and its DTO {@link MenuCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuCategoryMapper extends EntityMapper<MenuCategoryDTO, MenuCategory> {
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    MenuCategoryDTO toDto(MenuCategory s);

    @Named("restaurantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantDTO toDtoRestaurantId(Restaurant restaurant);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "restaurantId", ignore = true)
    MenuCategory toEntity(MenuCategoryDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "restaurantId", ignore = true)
    void partialUpdate(@MappingTarget MenuCategory entity, MenuCategoryDTO dto);
}
