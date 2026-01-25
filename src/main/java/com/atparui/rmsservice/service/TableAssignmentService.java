package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.TableAssignment;
import java.util.stream.Collectors;
import java.util.Optional;

import com.atparui.rmsservice.repository.TableAssignmentRepository;
import com.atparui.rmsservice.service.dto.DailyTableAssignmentRequestDTO;
import com.atparui.rmsservice.service.dto.TableAssignmentDTO;
import com.atparui.rmsservice.service.mapper.TableAssignmentMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.TableAssignment}.
 */
@Service
@Transactional
public class TableAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(TableAssignmentService.class);

    private final TableAssignmentRepository tableAssignmentRepository;

    private final TableAssignmentMapper tableAssignmentMapper;

    private final com.atparui.rmsservice.service.TableWaiterAssignmentService tableWaiterAssignmentService;

    public TableAssignmentService(
        TableAssignmentRepository tableAssignmentRepository,
        TableAssignmentMapper tableAssignmentMapper,
        com.atparui.rmsservice.service.TableWaiterAssignmentService tableWaiterAssignmentService
    ) {
        this.tableAssignmentRepository = tableAssignmentRepository;
        this.tableAssignmentMapper = tableAssignmentMapper;
        this.tableWaiterAssignmentService = tableWaiterAssignmentService;
    }
    @Transactional
    public Optional<TableAssignmentDTO> save(TableAssignmentDTO tableAssignmentDTO) {
        LOG.debug("Request to save TableAssignment : {}", tableAssignmentDTO);
        TableAssignment tableAssignment = tableAssignmentMapper.toEntity(tableAssignmentDTO);
        tableAssignment = tableAssignmentRepository.save(tableAssignment);
        return Optional.of(tableAssignmentMapper.toDto(tableAssignment));
    }

    @Transactional
    public Optional<TableAssignmentDTO> update(TableAssignmentDTO tableAssignmentDTO) {
        LOG.debug("Request to update TableAssignment : {}", tableAssignmentDTO);
        TableAssignment tableAssignment = tableAssignmentMapper.toEntity(tableAssignmentDTO);
        tableAssignment = tableAssignmentRepository.save(tableAssignment);
        return Optional.of(tableAssignmentMapper.toDto(tableAssignment));
    }

    @Transactional
    public Optional<TableAssignmentDTO> partialUpdate(TableAssignmentDTO tableAssignmentDTO) {
        LOG.debug("Request to partially update TableAssignment : {}", tableAssignmentDTO);
        return tableAssignmentRepository
            .findById(tableAssignmentDTO.getId())
            .map(existingTableAssignment -> {
                tableAssignmentMapper.partialUpdate(existingTableAssignment, tableAssignmentDTO);
                return tableAssignmentRepository.save(existingTableAssignment);
            })
            .map(tableAssignmentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<TableAssignmentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TableAssignments");
        Page<TableAssignment> page = tableAssignmentRepository.findAll(pageable);
        return page.getContent().stream()
            .map(tableAssignmentMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return tableAssignmentRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<TableAssignmentDTO> findOne(UUID id) {
        LOG.debug("Request to get TableAssignment : {}", id);
        return tableAssignmentRepository.findById(id).map(tableAssignmentMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete TableAssignment : {}", id);
        tableAssignmentRepository.deleteById(id);
    }
}
