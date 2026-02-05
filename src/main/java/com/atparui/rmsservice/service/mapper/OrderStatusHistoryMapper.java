package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Order;
import com.atparui.rmsservice.domain.OrderStatusHistory;
import com.atparui.rmsservice.service.dto.OrderDTO;
import com.atparui.rmsservice.service.dto.OrderStatusHistoryDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderStatusHistory} and its DTO {@link OrderStatusHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderStatusHistoryMapper extends EntityMapper<OrderStatusHistoryDTO, OrderStatusHistory> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderStatusHistoryDTO toDto(OrderStatusHistory s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "orderId", ignore = true)
    OrderStatusHistory toEntity(OrderStatusHistoryDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "orderId", ignore = true)
    void partialUpdate(@MappingTarget OrderStatusHistory entity, OrderStatusHistoryDTO dto);
}
