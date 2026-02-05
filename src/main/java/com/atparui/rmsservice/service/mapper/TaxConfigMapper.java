package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Restaurant;
import com.atparui.rmsservice.domain.TaxConfig;
import com.atparui.rmsservice.service.dto.RestaurantDTO;
import com.atparui.rmsservice.service.dto.TaxConfigDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxConfig} and its DTO {@link TaxConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaxConfigMapper extends EntityMapper<TaxConfigDTO, TaxConfig> {
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    TaxConfigDTO toDto(TaxConfig s);

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
    TaxConfig toEntity(TaxConfigDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "restaurantId", ignore = true)
    void partialUpdate(@MappingTarget TaxConfig entity, TaxConfigDTO dto);
}
