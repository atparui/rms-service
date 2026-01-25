package com.atparui.rmsservice.service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.UserSyncLog;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.UserSyncLogRepository;
import com.atparui.rmsservice.service.dto.UserSyncLogDTO;
import com.atparui.rmsservice.service.mapper.UserSyncLogMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.UserSyncLog}.
 */
@Service
@Transactional
public class UserSyncLogService {

    private static final Logger LOG = LoggerFactory.getLogger(UserSyncLogService.class);

    private final UserSyncLogRepository userSyncLogRepository;

    private final UserSyncLogMapper userSyncLogMapper;

    public UserSyncLogService(UserSyncLogRepository userSyncLogRepository, UserSyncLogMapper userSyncLogMapper) {
        this.userSyncLogRepository = userSyncLogRepository;
        this.userSyncLogMapper = userSyncLogMapper;
    }
    @Transactional
    public Optional<UserSyncLogDTO> save(UserSyncLogDTO userSyncLogDTO) {
        LOG.debug("Request to save UserSyncLog : {}", userSyncLogDTO);
        UserSyncLog userSyncLog = userSyncLogMapper.toEntity(userSyncLogDTO);
        userSyncLog = userSyncLogRepository.save(userSyncLog);
        return Optional.of(userSyncLogMapper.toDto(userSyncLog));
    }

    @Transactional
    public Optional<UserSyncLogDTO> update(UserSyncLogDTO userSyncLogDTO) {
        LOG.debug("Request to update UserSyncLog : {}", userSyncLogDTO);
        UserSyncLog userSyncLog = userSyncLogMapper.toEntity(userSyncLogDTO);
        userSyncLog = userSyncLogRepository.save(userSyncLog);
        return Optional.of(userSyncLogMapper.toDto(userSyncLog));
    }

    @Transactional
    public Optional<UserSyncLogDTO> partialUpdate(UserSyncLogDTO userSyncLogDTO) {
        LOG.debug("Request to partially update UserSyncLog : {}", userSyncLogDTO);
        return userSyncLogRepository
            .findById(userSyncLogDTO.getId())
            .map(existingUserSyncLog -> {
                userSyncLogMapper.partialUpdate(existingUserSyncLog, userSyncLogDTO);
                return userSyncLogRepository.save(existingUserSyncLog);
            })
            .map(userSyncLogMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<UserSyncLogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserSyncLogs");
        Page<UserSyncLog> page = userSyncLogRepository.findAll(pageable);
        return page.getContent().stream()
            .map(userSyncLogMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return userSyncLogRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<UserSyncLogDTO> findOne(UUID id) {
        LOG.debug("Request to get UserSyncLog : {}", id);
        return userSyncLogRepository.findById(id).map(userSyncLogMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete UserSyncLog : {}", id);
        userSyncLogRepository.deleteById(id);
    }
}
