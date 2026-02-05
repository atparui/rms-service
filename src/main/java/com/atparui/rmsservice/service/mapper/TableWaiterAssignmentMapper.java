package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.domain.TableAssignment;
import com.atparui.rmsservice.domain.TableWaiterAssignment;
import com.atparui.rmsservice.service.dto.RmsUserDTO;
import com.atparui.rmsservice.service.dto.TableAssignmentDTO;
import com.atparui.rmsservice.service.dto.TableWaiterAssignmentDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TableWaiterAssignment} and its DTO {@link TableWaiterAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TableWaiterAssignmentMapper extends EntityMapper<TableWaiterAssignmentDTO, TableWaiterAssignment> {
    @Mapping(target = "tableAssignment", source = "tableAssignment", qualifiedByName = "tableAssignmentId")
    @Mapping(target = "waiter", source = "waiter", qualifiedByName = "rmsUserId")
    TableWaiterAssignmentDTO toDto(TableWaiterAssignment s);

    @Named("tableAssignmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TableAssignmentDTO toDtoTableAssignmentId(TableAssignment tableAssignment);

    @Named("rmsUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RmsUserDTO toDtoRmsUserId(RmsUser rmsUser);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "tableAssignmentId", ignore = true)
    @Mapping(target = "waiterId", ignore = true)
    TableWaiterAssignment toEntity(TableWaiterAssignmentDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tableAssignmentId", ignore = true)
    @Mapping(target = "waiterId", ignore = true)
    void partialUpdate(@MappingTarget TableWaiterAssignment entity, TableWaiterAssignmentDTO dto);
}
