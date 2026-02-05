package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.BranchTable;
import com.atparui.rmsservice.domain.RmsUser;
import com.atparui.rmsservice.domain.Shift;
import com.atparui.rmsservice.domain.TableAssignment;
import com.atparui.rmsservice.service.dto.BranchTableDTO;
import com.atparui.rmsservice.service.dto.RmsUserDTO;
import com.atparui.rmsservice.service.dto.ShiftDTO;
import com.atparui.rmsservice.service.dto.TableAssignmentDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TableAssignment} and its DTO {@link TableAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TableAssignmentMapper extends EntityMapper<TableAssignmentDTO, TableAssignment> {
    @Mapping(target = "branchTable", source = "branchTable", qualifiedByName = "branchTableId")
    @Mapping(target = "shift", source = "shift", qualifiedByName = "shiftId")
    @Mapping(target = "supervisor", source = "supervisor", qualifiedByName = "rmsUserId")
    TableAssignmentDTO toDto(TableAssignment s);

    @Named("branchTableId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchTableDTO toDtoBranchTableId(BranchTable branchTable);

    @Named("shiftId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShiftDTO toDtoShiftId(Shift shift);

    @Named("rmsUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RmsUserDTO toDtoRmsUserId(RmsUser rmsUser);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "branchTableId", ignore = true)
    @Mapping(target = "shiftId", ignore = true)
    @Mapping(target = "supervisorId", ignore = true)
    TableAssignment toEntity(TableAssignmentDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "branchTableId", ignore = true)
    @Mapping(target = "shiftId", ignore = true)
    @Mapping(target = "supervisorId", ignore = true)
    void partialUpdate(@MappingTarget TableAssignment entity, TableAssignmentDTO dto);
}
