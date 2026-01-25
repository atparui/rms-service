package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.TaxConfig;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.TaxConfigRepository;
import com.atparui.rmsservice.service.dto.TaxConfigDTO;
import com.atparui.rmsservice.service.mapper.TaxConfigMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.TaxConfig}.
 */
@Service
@Transactional
public class TaxConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(TaxConfigService.class);

    private final TaxConfigRepository taxConfigRepository;

    private final TaxConfigMapper taxConfigMapper;

    public TaxConfigService(TaxConfigRepository taxConfigRepository, TaxConfigMapper taxConfigMapper) {
        this.taxConfigRepository = taxConfigRepository;
        this.taxConfigMapper = taxConfigMapper;
    }
    @Transactional
    public Optional<TaxConfigDTO> save(TaxConfigDTO taxConfigDTO) {
        LOG.debug("Request to save TaxConfig : {}", taxConfigDTO);
        TaxConfig taxConfig = taxConfigMapper.toEntity(taxConfigDTO);
        taxConfig = taxConfigRepository.save(taxConfig);
        return Optional.of(taxConfigMapper.toDto(taxConfig));
    }

    @Transactional
    public Optional<TaxConfigDTO> update(TaxConfigDTO taxConfigDTO) {
        LOG.debug("Request to update TaxConfig : {}", taxConfigDTO);
        TaxConfig taxConfig = taxConfigMapper.toEntity(taxConfigDTO);
        taxConfig = taxConfigRepository.save(taxConfig);
        return Optional.of(taxConfigMapper.toDto(taxConfig));
    }

    @Transactional
    public Optional<TaxConfigDTO> partialUpdate(TaxConfigDTO taxConfigDTO) {
        LOG.debug("Request to partially update TaxConfig : {}", taxConfigDTO);
        return taxConfigRepository
            .findById(taxConfigDTO.getId())
            .map(existingTaxConfig -> {
                taxConfigMapper.partialUpdate(existingTaxConfig, taxConfigDTO);
                return taxConfigRepository.save(existingTaxConfig);
            })
            .map(taxConfigMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<TaxConfigDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TaxConfigs");
        Page<TaxConfig> page = taxConfigRepository.findAll(pageable);
        return page.getContent().stream()
            .map(taxConfigMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return taxConfigRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<TaxConfigDTO> findOne(UUID id) {
        LOG.debug("Request to get TaxConfig : {}", id);
        return taxConfigRepository.findById(id).map(taxConfigMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete TaxConfig : {}", id);
        taxConfigRepository.deleteById(id);
    }
}
