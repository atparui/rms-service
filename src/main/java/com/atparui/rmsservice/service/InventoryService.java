package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.domain.Inventory;
import com.atparui.rmsservice.repository.InventoryRepository;
import com.atparui.rmsservice.service.dto.InventoryDTO;
import com.atparui.rmsservice.service.dto.StockAdjustmentRequestDTO;
import com.atparui.rmsservice.service.dto.StockUpdateRequestDTO;
import com.atparui.rmsservice.service.mapper.InventoryMapper;
import java.math.BigDecimal;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Inventory}.
 */
@Service
@Transactional
public class InventoryService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }
    @Transactional
    public Optional<InventoryDTO> save(InventoryDTO inventoryDTO) {
        LOG.debug("Request to save Inventory : {}", inventoryDTO);
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        inventory = inventoryRepository.save(inventory);
        return Optional.of(inventoryMapper.toDto(inventory));
    }

    @Transactional
    public Optional<InventoryDTO> update(InventoryDTO inventoryDTO) {
        LOG.debug("Request to update Inventory : {}", inventoryDTO);
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        inventory = inventoryRepository.save(inventory);
        return Optional.of(inventoryMapper.toDto(inventory));
    }

    @Transactional
    public Optional<InventoryDTO> partialUpdate(InventoryDTO inventoryDTO) {
        LOG.debug("Request to partially update Inventory : {}", inventoryDTO);
        return inventoryRepository
            .findById(inventoryDTO.getId())
            .map(existingInventory -> {
                inventoryMapper.partialUpdate(existingInventory, inventoryDTO);
                return inventoryRepository.save(existingInventory);
            })
            .map(inventoryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<InventoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Inventorys");
        Page<Inventory> page = inventoryRepository.findAll(pageable);
        return page.getContent().stream()
            .map(inventoryMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return inventoryRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<InventoryDTO> findOne(UUID id) {
        LOG.debug("Request to get Inventory : {}", id);
        return inventoryRepository.findById(id).map(inventoryMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Inventory : {}", id);
        inventoryRepository.deleteById(id);
    }
}
