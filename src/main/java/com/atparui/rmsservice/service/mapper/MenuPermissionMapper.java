package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.MenuPermission;
import com.atparui.rmsservice.service.dto.MenuPermissionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link MenuPermission} and {@link MenuPermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuPermissionMapper extends EntityMapper<MenuPermissionDTO, MenuPermission> {}
