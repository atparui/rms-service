package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.domain.Bill;
import com.atparui.rmsservice.repository.BillDiscountRepository;
import com.atparui.rmsservice.repository.BillItemRepository;
import com.atparui.rmsservice.repository.BillRepository;
import com.atparui.rmsservice.repository.BillTaxRepository;
import com.atparui.rmsservice.repository.OrderRepository;
import com.atparui.rmsservice.service.dto.BillBreakdownDTO;
import com.atparui.rmsservice.service.dto.BillDTO;
import com.atparui.rmsservice.service.dto.BillGenerationRequestDTO;
import com.atparui.rmsservice.service.dto.DiscountApplicationRequestDTO;
import com.atparui.rmsservice.service.mapper.BillItemMapper;
import com.atparui.rmsservice.service.mapper.BillMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Bill}.
 */
@Service
@Transactional
public class BillService {

    private static final Logger LOG = LoggerFactory.getLogger(BillService.class);

    private final BillRepository billRepository;

    private final BillMapper billMapper;
    private final OrderRepository orderRepository;

    private final BillItemRepository billItemRepository;

    private final BillTaxRepository billTaxRepository;

    private final BillDiscountRepository billDiscountRepository;

    private final BillItemMapper billItemMapper;

    private final com.atparui.rmsservice.service.mapper.BillTaxMapper billTaxMapper;

    private final com.atparui.rmsservice.service.mapper.BillDiscountMapper billDiscountMapper;

    private final com.atparui.rmsservice.service.DiscountService discountService;

    public BillService(
        BillRepository billRepository,
        BillMapper billMapper,
        OrderRepository orderRepository,
        BillItemRepository billItemRepository,
        BillTaxRepository billTaxRepository,
        BillDiscountRepository billDiscountRepository,
        BillItemMapper billItemMapper,
        com.atparui.rmsservice.service.mapper.BillTaxMapper billTaxMapper,
        com.atparui.rmsservice.service.mapper.BillDiscountMapper billDiscountMapper,
        com.atparui.rmsservice.service.DiscountService discountService
    ) {
        this.billRepository = billRepository;
        this.billMapper = billMapper;
        this.orderRepository = orderRepository;
        this.billItemRepository = billItemRepository;
        this.billTaxRepository = billTaxRepository;
        this.billDiscountRepository = billDiscountRepository;
        this.billItemMapper = billItemMapper;
        this.billTaxMapper = billTaxMapper;
        this.billDiscountMapper = billDiscountMapper;
        this.discountService = discountService;
    }
    @Transactional
    public Optional<BillDTO> save(BillDTO billDTO) {
        LOG.debug("Request to save Bill : {}", billDTO);
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        return Optional.of(billMapper.toDto(bill));
    }

    @Transactional
    public Optional<BillDTO> update(BillDTO billDTO) {
        LOG.debug("Request to update Bill : {}", billDTO);
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        return Optional.of(billMapper.toDto(bill));
    }

    @Transactional
    public Optional<BillDTO> partialUpdate(BillDTO billDTO) {
        LOG.debug("Request to partially update Bill : {}", billDTO);
        return billRepository
            .findById(billDTO.getId())
            .map(existingBill -> {
                billMapper.partialUpdate(existingBill, billDTO);
                return billRepository.save(existingBill);
            })
            .map(billMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BillDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Bills");
        Page<Bill> page = billRepository.findAll(pageable);
        return page.getContent().stream()
            .map(billMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return billRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<BillDTO> findOne(UUID id) {
        LOG.debug("Request to get Bill : {}", id);
        return billRepository.findById(id).map(billMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Bill : {}", id);
        billRepository.deleteById(id);
    }
}
