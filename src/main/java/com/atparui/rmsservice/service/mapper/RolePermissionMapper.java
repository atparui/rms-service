package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.RolePermission;
import com.atparui.rmsservice.service.dto.RolePermissionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link RolePermission} and {@link RolePermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RolePermissionMapper extends EntityMapper<RolePermissionDTO, RolePermission> {}
