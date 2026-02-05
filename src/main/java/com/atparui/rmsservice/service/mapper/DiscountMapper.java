package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Discount;
import com.atparui.rmsservice.domain.Restaurant;
import com.atparui.rmsservice.service.dto.DiscountDTO;
import com.atparui.rmsservice.service.dto.RestaurantDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Discount} and its DTO {@link DiscountDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiscountMapper extends EntityMapper<DiscountDTO, Discount> {
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    DiscountDTO toDto(Discount s);

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
    Discount toEntity(DiscountDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "restaurantId", ignore = true)
    void partialUpdate(@MappingTarget Discount entity, DiscountDTO dto);
}
