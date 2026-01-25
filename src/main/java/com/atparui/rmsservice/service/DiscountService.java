package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.domain.Discount;
import com.atparui.rmsservice.repository.DiscountRepository;
import com.atparui.rmsservice.service.dto.DiscountDTO;
import com.atparui.rmsservice.service.dto.DiscountValidationDTO;
import com.atparui.rmsservice.service.dto.DiscountValidationRequestDTO;
import com.atparui.rmsservice.service.mapper.DiscountMapper;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Discount}.
 */
@Service
@Transactional
public class DiscountService {

    private static final Logger LOG = LoggerFactory.getLogger(DiscountService.class);

    private final DiscountRepository discountRepository;

    private final DiscountMapper discountMapper;

    public DiscountService(DiscountRepository discountRepository, DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
    }
    @Transactional
    public Optional<DiscountDTO> save(DiscountDTO discountDTO) {
        LOG.debug("Request to save Discount : {}", discountDTO);
        Discount discount = discountMapper.toEntity(discountDTO);
        discount = discountRepository.save(discount);
        return Optional.of(discountMapper.toDto(discount));
    }

    @Transactional
    public Optional<DiscountDTO> update(DiscountDTO discountDTO) {
        LOG.debug("Request to update Discount : {}", discountDTO);
        Discount discount = discountMapper.toEntity(discountDTO);
        discount = discountRepository.save(discount);
        return Optional.of(discountMapper.toDto(discount));
    }

    @Transactional
    public Optional<DiscountDTO> partialUpdate(DiscountDTO discountDTO) {
        LOG.debug("Request to partially update Discount : {}", discountDTO);
        return discountRepository
            .findById(discountDTO.getId())
            .map(existingDiscount -> {
                discountMapper.partialUpdate(existingDiscount, discountDTO);
                return discountRepository.save(existingDiscount);
            })
            .map(discountMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<DiscountDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Discounts");
        Page<Discount> page = discountRepository.findAll(pageable);
        return page.getContent().stream()
            .map(discountMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return discountRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<DiscountDTO> findOne(UUID id) {
        LOG.debug("Request to get Discount : {}", id);
        return discountRepository.findById(id).map(discountMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Discount : {}", id);
        discountRepository.deleteById(id);
    }
}
