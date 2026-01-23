package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.AppMenu;
import com.atparui.rmsservice.service.dto.AppMenuDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link AppMenu} and {@link AppMenuDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppMenuMapper extends EntityMapper<AppMenuDTO, AppMenu> {}
