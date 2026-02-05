package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.service.dto.RmsUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RmsUser} and its DTO {@link RmsUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface RmsUserMapper extends EntityMapper<RmsUserDTO, RmsUser> {
    // Ignore authorities - managed separately via user_authority join table
    @Override
    @Mapping(target = "authorities", ignore = true)
    RmsUser toEntity(RmsUserDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    void partialUpdate(@MappingTarget RmsUser entity, RmsUserDTO dto);
}
