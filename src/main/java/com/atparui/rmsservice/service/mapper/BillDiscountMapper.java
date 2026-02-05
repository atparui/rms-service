package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Bill;
import com.atparui.rmsservice.domain.BillDiscount;
import com.atparui.rmsservice.domain.Discount;
import com.atparui.rmsservice.service.dto.BillDTO;
import com.atparui.rmsservice.service.dto.BillDiscountDTO;
import com.atparui.rmsservice.service.dto.DiscountDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillDiscount} and its DTO {@link BillDiscountDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillDiscountMapper extends EntityMapper<BillDiscountDTO, BillDiscount> {
    @Mapping(target = "bill", source = "bill", qualifiedByName = "billId")
    @Mapping(target = "discount", source = "discount", qualifiedByName = "discountId")
    BillDiscountDTO toDto(BillDiscount s);

    @Named("billId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillDTO toDtoBillId(Bill bill);

    @Named("discountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DiscountDTO toDtoDiscountId(Discount discount);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "discountId", ignore = true)
    BillDiscount toEntity(BillDiscountDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "discountId", ignore = true)
    void partialUpdate(@MappingTarget BillDiscount entity, BillDiscountDTO dto);
}
