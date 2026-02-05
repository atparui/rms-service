package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.domain.UserSyncLog;
import com.atparui.rmsservice.service.dto.RmsUserDTO;
import com.atparui.rmsservice.service.dto.UserSyncLogDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSyncLog} and its DTO {@link UserSyncLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSyncLogMapper extends EntityMapper<UserSyncLogDTO, UserSyncLog> {
    @Mapping(target = "user", source = "user", qualifiedByName = "rmsUserId")
    UserSyncLogDTO toDto(UserSyncLog s);

    @Named("rmsUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RmsUserDTO toDtoRmsUserId(RmsUser rmsUser);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "userId", ignore = true)
    UserSyncLog toEntity(UserSyncLogDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userId", ignore = true)
    void partialUpdate(@MappingTarget UserSyncLog entity, UserSyncLogDTO dto);
}
