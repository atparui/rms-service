package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.Customer;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.CustomerRepository;
import com.atparui.rmsservice.repository.OrderRepository;
import com.atparui.rmsservice.service.dto.CustomerDTO;
import com.atparui.rmsservice.service.dto.CustomerLoyaltyDTO;
import com.atparui.rmsservice.service.dto.LoyaltyPointsRequestDTO;
import com.atparui.rmsservice.service.dto.OrderDTO;
import com.atparui.rmsservice.service.mapper.CustomerMapper;
import com.atparui.rmsservice.service.mapper.OrderMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Customer}.
 */
@Service
@Transactional
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final CustomerLoyaltyService customerLoyaltyService;

    public CustomerService(
        CustomerRepository customerRepository,
        CustomerMapper customerMapper,
        OrderRepository orderRepository,
        OrderMapper orderMapper,
        CustomerLoyaltyService customerLoyaltyService
    ) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerLoyaltyService = customerLoyaltyService;
    }
    @Transactional
    public Optional<CustomerDTO> save(CustomerDTO customerDTO) {
        LOG.debug("Request to save Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return Optional.of(customerMapper.toDto(customer));
    }

    @Transactional
    public Optional<CustomerDTO> update(CustomerDTO customerDTO) {
        LOG.debug("Request to update Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return Optional.of(customerMapper.toDto(customer));
    }

    @Transactional
    public Optional<CustomerDTO> partialUpdate(CustomerDTO customerDTO) {
        LOG.debug("Request to partially update Customer : {}", customerDTO);
        return customerRepository
            .findById(customerDTO.getId())
            .map(existingCustomer -> {
                customerMapper.partialUpdate(existingCustomer, customerDTO);
                return customerRepository.save(existingCustomer);
            })
            .map(customerMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Customers");
        Page<Customer> page = customerRepository.findAll(pageable);
        return page.getContent().stream()
            .map(customerMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return customerRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findOne(UUID id) {
        LOG.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
    }
}
