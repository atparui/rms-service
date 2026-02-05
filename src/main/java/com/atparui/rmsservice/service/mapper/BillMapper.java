package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Bill;
import com.atparui.rmsservice.domain.Branch;
import com.atparui.rmsservice.domain.Customer;
import com.atparui.rmsservice.domain.Order;
import com.atparui.rmsservice.service.dto.BillDTO;
import com.atparui.rmsservice.service.dto.BranchDTO;
import com.atparui.rmsservice.service.dto.CustomerDTO;
import com.atparui.rmsservice.service.dto.OrderDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bill} and its DTO {@link BillDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillMapper extends EntityMapper<BillDTO, Bill> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    @Mapping(target = "branch", source = "branch", qualifiedByName = "branchId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    BillDTO toDto(Bill s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("branchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoBranchId(Branch branch);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    Bill toEntity(BillDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    void partialUpdate(@MappingTarget Bill entity, BillDTO dto);
}
