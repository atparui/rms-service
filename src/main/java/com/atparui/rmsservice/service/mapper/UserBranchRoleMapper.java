package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Branch;
import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.domain.UserBranchRole;
import com.atparui.rmsservice.service.dto.BranchDTO;
import com.atparui.rmsservice.service.dto.RmsUserDTO;
import com.atparui.rmsservice.service.dto.UserBranchRoleDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserBranchRole} and its DTO {@link UserBranchRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserBranchRoleMapper extends EntityMapper<UserBranchRoleDTO, UserBranchRole> {
    @Mapping(target = "user", source = "user", qualifiedByName = "rmsUserId")
    @Mapping(target = "branch", source = "branch", qualifiedByName = "branchId")
    UserBranchRoleDTO toDto(UserBranchRole s);

    @Named("rmsUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RmsUserDTO toDtoRmsUserId(RmsUser rmsUser);

    @Named("branchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoBranchId(Branch branch);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    UserBranchRole toEntity(UserBranchRoleDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    void partialUpdate(@MappingTarget UserBranchRole entity, UserBranchRoleDTO dto);
}
