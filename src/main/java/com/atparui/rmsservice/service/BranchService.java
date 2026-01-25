package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.Branch;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.BranchRepository;
import com.atparui.rmsservice.service.dto.BranchDTO;
import com.atparui.rmsservice.service.mapper.BranchMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Branch}.
 */
@Service
@Transactional
public class BranchService {

    private static final Logger LOG = LoggerFactory.getLogger(BranchService.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;
    
    public BranchService(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }
    @Transactional
    public Optional<BranchDTO> save(BranchDTO branchDTO) {
        LOG.debug("Request to save Branch : {}", branchDTO);
        Branch branch = branchMapper.toEntity(branchDTO);
        branch = branchRepository.save(branch);
        return Optional.of(branchMapper.toDto(branch));
    }

    @Transactional
    public Optional<BranchDTO> update(BranchDTO branchDTO) {
        LOG.debug("Request to update Branch : {}", branchDTO);
        Branch branch = branchMapper.toEntity(branchDTO);
        branch = branchRepository.save(branch);
        return Optional.of(branchMapper.toDto(branch));
    }

    @Transactional
    public Optional<BranchDTO> partialUpdate(BranchDTO branchDTO) {
        LOG.debug("Request to partially update Branch : {}", branchDTO);
        return branchRepository
            .findById(branchDTO.getId())
            .map(existingBranch -> {
                branchMapper.partialUpdate(existingBranch, branchDTO);
                return branchRepository.save(existingBranch);
            })
            .map(branchMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BranchDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Branchs");
        Page<Branch> page = branchRepository.findAll(pageable);
        return page.getContent().stream()
            .map(branchMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return branchRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<BranchDTO> findOne(UUID id) {
        LOG.debug("Request to get Branch : {}", id);
        return branchRepository.findById(id).map(branchMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Branch : {}", id);
        branchRepository.deleteById(id);
    }
}
