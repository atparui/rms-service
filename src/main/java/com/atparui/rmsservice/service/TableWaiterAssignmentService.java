package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.TableWaiterAssignment;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.TableWaiterAssignmentRepository;
import com.atparui.rmsservice.service.dto.TableWaiterAssignmentDTO;
import com.atparui.rmsservice.service.mapper.TableWaiterAssignmentMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.TableWaiterAssignment}.
 */
@Service
@Transactional
public class TableWaiterAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(TableWaiterAssignmentService.class);

    private final TableWaiterAssignmentRepository tableWaiterAssignmentRepository;

    private final TableWaiterAssignmentMapper tableWaiterAssignmentMapper;

    public TableWaiterAssignmentService(
        TableWaiterAssignmentRepository tableWaiterAssignmentRepository,
        TableWaiterAssignmentMapper tableWaiterAssignmentMapper
    ) {
        this.tableWaiterAssignmentRepository = tableWaiterAssignmentRepository;
        this.tableWaiterAssignmentMapper = tableWaiterAssignmentMapper;
    }
    @Transactional
    public Optional<TableWaiterAssignmentDTO> save(TableWaiterAssignmentDTO tableWaiterAssignmentDTO) {
        LOG.debug("Request to save TableWaiterAssignment : {}", tableWaiterAssignmentDTO);
        TableWaiterAssignment tableWaiterAssignment = tableWaiterAssignmentMapper.toEntity(tableWaiterAssignmentDTO);
        tableWaiterAssignment = tableWaiterAssignmentRepository.save(tableWaiterAssignment);
        return Optional.of(tableWaiterAssignmentMapper.toDto(tableWaiterAssignment));
    }

    @Transactional
    public Optional<TableWaiterAssignmentDTO> update(TableWaiterAssignmentDTO tableWaiterAssignmentDTO) {
        LOG.debug("Request to update TableWaiterAssignment : {}", tableWaiterAssignmentDTO);
        TableWaiterAssignment tableWaiterAssignment = tableWaiterAssignmentMapper.toEntity(tableWaiterAssignmentDTO);
        tableWaiterAssignment = tableWaiterAssignmentRepository.save(tableWaiterAssignment);
        return Optional.of(tableWaiterAssignmentMapper.toDto(tableWaiterAssignment));
    }

    @Transactional
    public Optional<TableWaiterAssignmentDTO> partialUpdate(TableWaiterAssignmentDTO tableWaiterAssignmentDTO) {
        LOG.debug("Request to partially update TableWaiterAssignment : {}", tableWaiterAssignmentDTO);
        return tableWaiterAssignmentRepository
            .findById(tableWaiterAssignmentDTO.getId())
            .map(existingTableWaiterAssignment -> {
                tableWaiterAssignmentMapper.partialUpdate(existingTableWaiterAssignment, tableWaiterAssignmentDTO);
                return tableWaiterAssignmentRepository.save(existingTableWaiterAssignment);
            })
            .map(tableWaiterAssignmentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<TableWaiterAssignmentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TableWaiterAssignments");
        Page<TableWaiterAssignment> page = tableWaiterAssignmentRepository.findAll(pageable);
        return page.getContent().stream()
            .map(tableWaiterAssignmentMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return tableWaiterAssignmentRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<TableWaiterAssignmentDTO> findOne(UUID id) {
        LOG.debug("Request to get TableWaiterAssignment : {}", id);
        return tableWaiterAssignmentRepository.findById(id).map(tableWaiterAssignmentMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete TableWaiterAssignment : {}", id);
        tableWaiterAssignmentRepository.deleteById(id);
    }
}
