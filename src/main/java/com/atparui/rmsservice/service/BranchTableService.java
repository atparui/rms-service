package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.BranchTable;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.BranchTableRepository;
import com.atparui.rmsservice.service.dto.BranchTableDTO;
import com.atparui.rmsservice.service.mapper.BranchTableMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.BranchTable}.
 */
@Service
@Transactional
public class BranchTableService {

    private static final Logger LOG = LoggerFactory.getLogger(BranchTableService.class);

    private final BranchTableRepository branchTableRepository;

    private final BranchTableMapper branchTableMapper;
    public BranchTableService(
        BranchTableRepository branchTableRepository,
        BranchTableMapper branchTableMapper
    ) {
        this.branchTableRepository = branchTableRepository;
        this.branchTableMapper = branchTableMapper;    }
    @Transactional
    public Optional<BranchTableDTO> save(BranchTableDTO branchTableDTO) {
        LOG.debug("Request to save BranchTable : {}", branchTableDTO);
        BranchTable branchTable = branchTableMapper.toEntity(branchTableDTO);
        branchTable = branchTableRepository.save(branchTable);
        return Optional.of(branchTableMapper.toDto(branchTable));
    }

    @Transactional
    public Optional<BranchTableDTO> update(BranchTableDTO branchTableDTO) {
        LOG.debug("Request to update BranchTable : {}", branchTableDTO);
        BranchTable branchTable = branchTableMapper.toEntity(branchTableDTO);
        branchTable = branchTableRepository.save(branchTable);
        return Optional.of(branchTableMapper.toDto(branchTable));
    }

    @Transactional
    public Optional<BranchTableDTO> partialUpdate(BranchTableDTO branchTableDTO) {
        LOG.debug("Request to partially update BranchTable : {}", branchTableDTO);
        return branchTableRepository
            .findById(branchTableDTO.getId())
            .map(existingBranchTable -> {
                branchTableMapper.partialUpdate(existingBranchTable, branchTableDTO);
                return branchTableRepository.save(existingBranchTable);
            })
            .map(branchTableMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BranchTableDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BranchTables");
        Page<BranchTable> page = branchTableRepository.findAll(pageable);
        return page.getContent().stream()
            .map(branchTableMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return branchTableRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<BranchTableDTO> findOne(UUID id) {
        LOG.debug("Request to get BranchTable : {}", id);
        return branchTableRepository.findById(id).map(branchTableMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete BranchTable : {}", id);
        branchTableRepository.deleteById(id);
    }
}
