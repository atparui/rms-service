package com.atparui.rmsservice.service.mapper;

import com.atparui.rmsservice.domain.Bill;
import com.atparui.rmsservice.domain.Payment;
import com.atparui.rmsservice.domain.PaymentMethod;
import com.atparui.rmsservice.service.dto.BillDTO;
import com.atparui.rmsservice.service.dto.PaymentDTO;
import com.atparui.rmsservice.service.dto.PaymentMethodDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "bill", source = "bill", qualifiedByName = "billId")
    @Mapping(target = "paymentMethod", source = "paymentMethod", qualifiedByName = "paymentMethodId")
    PaymentDTO toDto(Payment s);

    @Named("billId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillDTO toDtoBillId(Bill bill);

    @Named("paymentMethodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentMethodDTO toDtoPaymentMethodId(PaymentMethod paymentMethod);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

    // Ignore FK ID fields - they're auto-managed by entity setters
    @Override
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "paymentMethodId", ignore = true)
    Payment toEntity(PaymentDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "billId", ignore = true)
    @Mapping(target = "paymentMethodId", ignore = true)
    void partialUpdate(@MappingTarget Payment entity, PaymentDTO dto);
}
