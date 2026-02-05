package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Branch;
import com.atparui.rmsservice.domain.Inventory;
import com.atparui.rmsservice.domain.MenuItem;
import com.atparui.rmsservice.service.dto.BranchDTO;
import com.atparui.rmsservice.service.dto.InventoryDTO;
import com.atparui.rmsservice.service.dto.MenuItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventory} and its DTO {@link InventoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoryMapper extends EntityMapper<InventoryDTO, Inventory> {
    @Mapping(target = "branch", source = "branch", qualifiedByName = "branchId")
    @Mapping(target = "menuItem", source = "menuItem", qualifiedByName = "menuItemId")
    InventoryDTO toDto(Inventory s);

    @Named("branchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoBranchId(Branch branch);

    @Named("menuItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuItemDTO toDtoMenuItemId(MenuItem menuItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "menuItemId", ignore = true)
    Inventory toEntity(InventoryDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "menuItemId", ignore = true)
    void partialUpdate(@MappingTarget Inventory entity, InventoryDTO dto);
}
