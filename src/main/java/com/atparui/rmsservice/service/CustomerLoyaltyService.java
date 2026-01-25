package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.CustomerLoyalty;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.CustomerLoyaltyRepository;
import com.atparui.rmsservice.service.dto.CustomerLoyaltyDTO;
import com.atparui.rmsservice.service.mapper.CustomerLoyaltyMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.CustomerLoyalty}.
 */
@Service
@Transactional
public class CustomerLoyaltyService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerLoyaltyService.class);

    private final CustomerLoyaltyRepository customerLoyaltyRepository;

    private final CustomerLoyaltyMapper customerLoyaltyMapper;

    public CustomerLoyaltyService(CustomerLoyaltyRepository customerLoyaltyRepository, CustomerLoyaltyMapper customerLoyaltyMapper) {
        this.customerLoyaltyRepository = customerLoyaltyRepository;
        this.customerLoyaltyMapper = customerLoyaltyMapper;
    }
    @Transactional
    public Optional<CustomerLoyaltyDTO> save(CustomerLoyaltyDTO customerLoyaltyDTO) {
        LOG.debug("Request to save CustomerLoyalty : {}", customerLoyaltyDTO);
        CustomerLoyalty customerLoyalty = customerLoyaltyMapper.toEntity(customerLoyaltyDTO);
        customerLoyalty = customerLoyaltyRepository.save(customerLoyalty);
        return Optional.of(customerLoyaltyMapper.toDto(customerLoyalty));
    }

    @Transactional
    public Optional<CustomerLoyaltyDTO> update(CustomerLoyaltyDTO customerLoyaltyDTO) {
        LOG.debug("Request to update CustomerLoyalty : {}", customerLoyaltyDTO);
        CustomerLoyalty customerLoyalty = customerLoyaltyMapper.toEntity(customerLoyaltyDTO);
        customerLoyalty = customerLoyaltyRepository.save(customerLoyalty);
        return Optional.of(customerLoyaltyMapper.toDto(customerLoyalty));
    }

    @Transactional
    public Optional<CustomerLoyaltyDTO> partialUpdate(CustomerLoyaltyDTO customerLoyaltyDTO) {
        LOG.debug("Request to partially update CustomerLoyalty : {}", customerLoyaltyDTO);
        return customerLoyaltyRepository
            .findById(customerLoyaltyDTO.getId())
            .map(existingCustomerLoyalty -> {
                customerLoyaltyMapper.partialUpdate(existingCustomerLoyalty, customerLoyaltyDTO);
                return customerLoyaltyRepository.save(existingCustomerLoyalty);
            })
            .map(customerLoyaltyMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<CustomerLoyaltyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CustomerLoyaltys");
        Page<CustomerLoyalty> page = customerLoyaltyRepository.findAll(pageable);
        return page.getContent().stream()
            .map(customerLoyaltyMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return customerLoyaltyRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<CustomerLoyaltyDTO> findOne(UUID id) {
        LOG.debug("Request to get CustomerLoyalty : {}", id);
        return customerLoyaltyRepository.findById(id).map(customerLoyaltyMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete CustomerLoyalty : {}", id);
        customerLoyaltyRepository.deleteById(id);
    }
}
