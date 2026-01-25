package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.BillDiscount;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.BillDiscountRepository;
import com.atparui.rmsservice.service.dto.BillDiscountDTO;
import com.atparui.rmsservice.service.mapper.BillDiscountMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.BillDiscount}.
 */
@Service
@Transactional
public class BillDiscountService {

    private static final Logger LOG = LoggerFactory.getLogger(BillDiscountService.class);

    private final BillDiscountRepository billDiscountRepository;

    private final BillDiscountMapper billDiscountMapper;

    public BillDiscountService(BillDiscountRepository billDiscountRepository, BillDiscountMapper billDiscountMapper) {
        this.billDiscountRepository = billDiscountRepository;
        this.billDiscountMapper = billDiscountMapper;
    }
    @Transactional
    public Optional<BillDiscountDTO> save(BillDiscountDTO billDiscountDTO) {
        LOG.debug("Request to save BillDiscount : {}", billDiscountDTO);
        BillDiscount billDiscount = billDiscountMapper.toEntity(billDiscountDTO);
        billDiscount = billDiscountRepository.save(billDiscount);
        return Optional.of(billDiscountMapper.toDto(billDiscount));
    }

    @Transactional
    public Optional<BillDiscountDTO> update(BillDiscountDTO billDiscountDTO) {
        LOG.debug("Request to update BillDiscount : {}", billDiscountDTO);
        BillDiscount billDiscount = billDiscountMapper.toEntity(billDiscountDTO);
        billDiscount = billDiscountRepository.save(billDiscount);
        return Optional.of(billDiscountMapper.toDto(billDiscount));
    }

    @Transactional
    public Optional<BillDiscountDTO> partialUpdate(BillDiscountDTO billDiscountDTO) {
        LOG.debug("Request to partially update BillDiscount : {}", billDiscountDTO);
        return billDiscountRepository
            .findById(billDiscountDTO.getId())
            .map(existingBillDiscount -> {
                billDiscountMapper.partialUpdate(existingBillDiscount, billDiscountDTO);
                return billDiscountRepository.save(existingBillDiscount);
            })
            .map(billDiscountMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BillDiscountDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BillDiscounts");
        Page<BillDiscount> page = billDiscountRepository.findAll(pageable);
        return page.getContent().stream()
            .map(billDiscountMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return billDiscountRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<BillDiscountDTO> findOne(UUID id) {
        LOG.debug("Request to get BillDiscount : {}", id);
        return billDiscountRepository.findById(id).map(billDiscountMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete BillDiscount : {}", id);
        billDiscountRepository.deleteById(id);
    }
}
