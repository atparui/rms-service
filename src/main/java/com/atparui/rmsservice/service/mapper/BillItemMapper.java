package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Bill;
import com.atparui.rmsservice.domain.BillItem;
import com.atparui.rmsservice.domain.OrderItem;
import com.atparui.rmsservice.service.dto.BillDTO;
import com.atparui.rmsservice.service.dto.BillItemDTO;
import com.atparui.rmsservice.service.dto.OrderItemDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillItem} and its DTO {@link BillItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillItemMapper extends EntityMapper<BillItemDTO, BillItem> {
    @Mapping(target = "bill", source = "bill", qualifiedByName = "billId")
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "orderItemId")
    BillItemDTO toDto(BillItem s);

    @Named("billId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillDTO toDtoBillId(Bill bill);

    @Named("orderItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderItemDTO toDtoOrderItemId(OrderItem orderItem);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "orderItemId", ignore = true)
    BillItem toEntity(BillItemDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "orderItemId", ignore = true)
    void partialUpdate(@MappingTarget BillItem entity, BillItemDTO dto);
}
