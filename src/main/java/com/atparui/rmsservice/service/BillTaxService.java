package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.BillTax;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.BillTaxRepository;
import com.atparui.rmsservice.service.dto.BillTaxDTO;
import com.atparui.rmsservice.service.mapper.BillTaxMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.BillTax}.
 */
@Service
@Transactional
public class BillTaxService {

    private static final Logger LOG = LoggerFactory.getLogger(BillTaxService.class);

    private final BillTaxRepository billTaxRepository;

    private final BillTaxMapper billTaxMapper;

    public BillTaxService(BillTaxRepository billTaxRepository, BillTaxMapper billTaxMapper) {
        this.billTaxRepository = billTaxRepository;
        this.billTaxMapper = billTaxMapper;
    }
    @Transactional
    public Optional<BillTaxDTO> save(BillTaxDTO billTaxDTO) {
        LOG.debug("Request to save BillTax : {}", billTaxDTO);
        BillTax billTax = billTaxMapper.toEntity(billTaxDTO);
        billTax = billTaxRepository.save(billTax);
        return Optional.of(billTaxMapper.toDto(billTax));
    }

    @Transactional
    public Optional<BillTaxDTO> update(BillTaxDTO billTaxDTO) {
        LOG.debug("Request to update BillTax : {}", billTaxDTO);
        BillTax billTax = billTaxMapper.toEntity(billTaxDTO);
        billTax = billTaxRepository.save(billTax);
        return Optional.of(billTaxMapper.toDto(billTax));
    }

    @Transactional
    public Optional<BillTaxDTO> partialUpdate(BillTaxDTO billTaxDTO) {
        LOG.debug("Request to partially update BillTax : {}", billTaxDTO);
        return billTaxRepository
            .findById(billTaxDTO.getId())
            .map(existingBillTax -> {
                billTaxMapper.partialUpdate(existingBillTax, billTaxDTO);
                return billTaxRepository.save(existingBillTax);
            })
            .map(billTaxMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BillTaxDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BillTaxs");
        Page<BillTax> page = billTaxRepository.findAll(pageable);
        return page.getContent().stream()
            .map(billTaxMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return billTaxRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<BillTaxDTO> findOne(UUID id) {
        LOG.debug("Request to get BillTax : {}", id);
        return billTaxRepository.findById(id).map(billTaxMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete BillTax : {}", id);
        billTaxRepository.deleteById(id);
    }
}
