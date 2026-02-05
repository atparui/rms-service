package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.service.dto.RmsUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RmsUser} and its DTO {@link RmsUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface RmsUserMapper extends EntityMapper<RmsUserDTO, RmsUser> {
    // NOTE: RmsUser doesn't use authorities collection - RMS uses user_branch_role system instead
}
