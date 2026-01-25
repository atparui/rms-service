package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.Shift;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.ShiftRepository;
import com.atparui.rmsservice.service.dto.ShiftDTO;
import com.atparui.rmsservice.service.mapper.ShiftMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.Shift}.
 */
@Service
@Transactional
public class ShiftService {

    private static final Logger LOG = LoggerFactory.getLogger(ShiftService.class);

    private final ShiftRepository shiftRepository;

    private final ShiftMapper shiftMapper;

    public ShiftService(ShiftRepository shiftRepository, ShiftMapper shiftMapper) {
        this.shiftRepository = shiftRepository;
        this.shiftMapper = shiftMapper;
    }
    @Transactional
    public Optional<ShiftDTO> save(ShiftDTO shiftDTO) {
        LOG.debug("Request to save Shift : {}", shiftDTO);
        Shift shift = shiftMapper.toEntity(shiftDTO);
        shift = shiftRepository.save(shift);
        return Optional.of(shiftMapper.toDto(shift));
    }

    @Transactional
    public Optional<ShiftDTO> update(ShiftDTO shiftDTO) {
        LOG.debug("Request to update Shift : {}", shiftDTO);
        Shift shift = shiftMapper.toEntity(shiftDTO);
        shift = shiftRepository.save(shift);
        return Optional.of(shiftMapper.toDto(shift));
    }

    @Transactional
    public Optional<ShiftDTO> partialUpdate(ShiftDTO shiftDTO) {
        LOG.debug("Request to partially update Shift : {}", shiftDTO);
        return shiftRepository
            .findById(shiftDTO.getId())
            .map(existingShift -> {
                shiftMapper.partialUpdate(existingShift, shiftDTO);
                return shiftRepository.save(existingShift);
            })
            .map(shiftMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<ShiftDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Shifts");
        Page<Shift> page = shiftRepository.findAll(pageable);
        return page.getContent().stream()
            .map(shiftMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return shiftRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<ShiftDTO> findOne(UUID id) {
        LOG.debug("Request to get Shift : {}", id);
        return shiftRepository.findById(id).map(shiftMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete Shift : {}", id);
        shiftRepository.deleteById(id);
    }
}
