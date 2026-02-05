package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.RestaurantRole;
import com.atparui.rmsservice.service.dto.RestaurantRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RestaurantRole} and its DTO {@link RestaurantRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RestaurantRoleMapper extends EntityMapper<RestaurantRoleDTO, RestaurantRole> {}
