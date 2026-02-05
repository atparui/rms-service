package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Bill;
import com.atparui.rmsservice.domain.BillTax;
import com.atparui.rmsservice.domain.TaxConfig;
import com.atparui.rmsservice.service.dto.BillDTO;
import com.atparui.rmsservice.service.dto.BillTaxDTO;
import com.atparui.rmsservice.service.dto.TaxConfigDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillTax} and its DTO {@link BillTaxDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillTaxMapper extends EntityMapper<BillTaxDTO, BillTax> {
    @Mapping(target = "bill", source = "bill", qualifiedByName = "billId")
    @Mapping(target = "taxConfig", source = "taxConfig", qualifiedByName = "taxConfigId")
    BillTaxDTO toDto(BillTax s);

    @Named("billId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillDTO toDtoBillId(Bill bill);

    @Named("taxConfigId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaxConfigDTO toDtoTaxConfigId(TaxConfig taxConfig);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "taxConfigId", ignore = true)
    BillTax toEntity(BillTaxDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "taxConfigId", ignore = true)
    void partialUpdate(@MappingTarget BillTax entity, BillTaxDTO dto);
}
