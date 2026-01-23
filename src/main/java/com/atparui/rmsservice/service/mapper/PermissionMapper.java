package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Permission;
import com.atparui.rmsservice.service.dto.PermissionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link Permission} and {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {}
