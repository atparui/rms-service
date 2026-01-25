package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.BillItem;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.BillItemRepository;
import com.atparui.rmsservice.service.dto.BillItemDTO;
import com.atparui.rmsservice.service.mapper.BillItemMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.BillItem}.
 */
@Service
@Transactional
public class BillItemService {

    private static final Logger LOG = LoggerFactory.getLogger(BillItemService.class);

    private final BillItemRepository billItemRepository;

    private final BillItemMapper billItemMapper;

    public BillItemService(BillItemRepository billItemRepository, BillItemMapper billItemMapper) {
        this.billItemRepository = billItemRepository;
        this.billItemMapper = billItemMapper;
    }
    @Transactional
    public Optional<BillItemDTO> save(BillItemDTO billItemDTO) {
        LOG.debug("Request to save BillItem : {}", billItemDTO);
        BillItem billItem = billItemMapper.toEntity(billItemDTO);
        billItem = billItemRepository.save(billItem);
        return Optional.of(billItemMapper.toDto(billItem));
    }

    @Transactional
    public Optional<BillItemDTO> update(BillItemDTO billItemDTO) {
        LOG.debug("Request to update BillItem : {}", billItemDTO);
        BillItem billItem = billItemMapper.toEntity(billItemDTO);
        billItem = billItemRepository.save(billItem);
        return Optional.of(billItemMapper.toDto(billItem));
    }

    @Transactional
    public Optional<BillItemDTO> partialUpdate(BillItemDTO billItemDTO) {
        LOG.debug("Request to partially update BillItem : {}", billItemDTO);
        return billItemRepository
            .findById(billItemDTO.getId())
            .map(existingBillItem -> {
                billItemMapper.partialUpdate(existingBillItem, billItemDTO);
                return billItemRepository.save(existingBillItem);
            })
            .map(billItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BillItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BillItems");
        Page<BillItem> page = billItemRepository.findAll(pageable);
        return page.getContent().stream()
            .map(billItemMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return billItemRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<BillItemDTO> findOne(UUID id) {
        LOG.debug("Request to get BillItem : {}", id);
        return billItemRepository.findById(id).map(billItemMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete BillItem : {}", id);
        billItemRepository.deleteById(id);
    }
}
