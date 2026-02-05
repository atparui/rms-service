package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Branch;
import com.atparui.rmsservice.domain.BranchTable;
import com.atparui.rmsservice.service.dto.BranchDTO;
import com.atparui.rmsservice.service.dto.BranchTableDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchTable} and its DTO {@link BranchTableDTO}.
 */
@Mapper(componentModel = "spring")
public interface BranchTableMapper extends EntityMapper<BranchTableDTO, BranchTable> {
    @Mapping(target = "branch", source = "branch", qualifiedByName = "branchId")
    BranchTableDTO toDto(BranchTable s);

    @Named("branchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoBranchId(Branch branch);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "branchId", ignore = true)
    BranchTable toEntity(BranchTableDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "branchId", ignore = true)
    void partialUpdate(@MappingTarget BranchTable entity, BranchTableDTO dto);
}
