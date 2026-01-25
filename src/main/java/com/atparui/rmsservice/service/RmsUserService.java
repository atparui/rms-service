package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.RmsUser;
import java.util.stream.Collectors;
import java.util.Optional;

import com.atparui.rmsservice.repository.RmsUserRepository;
import com.atparui.rmsservice.service.UserSyncLogService;
import com.atparui.rmsservice.service.dto.RmsUserDTO;
import com.atparui.rmsservice.service.dto.UserSyncLogDTO;
import com.atparui.rmsservice.service.mapper.RmsUserMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.RmsUser}.
 */
@Service
@Transactional
public class RmsUserService {

    private static final Logger LOG = LoggerFactory.getLogger(RmsUserService.class);

    private final RmsUserRepository rmsUserRepository;

    private final RmsUserMapper rmsUserMapper;
    private final UserSyncLogService userSyncLogService;

    public RmsUserService(
        RmsUserRepository rmsUserRepository,
        RmsUserMapper rmsUserMapper,
        UserSyncLogService userSyncLogService
    ) {
        this.rmsUserRepository = rmsUserRepository;
        this.rmsUserMapper = rmsUserMapper;
        this.userSyncLogService = userSyncLogService;
    }
    @Transactional
    public Optional<RmsUserDTO> save(RmsUserDTO rmsUserDTO) {
        LOG.debug("Request to save RmsUser : {}", rmsUserDTO);
        RmsUser rmsUser = rmsUserMapper.toEntity(rmsUserDTO);
        rmsUser = rmsUserRepository.save(rmsUser);
        return Optional.of(rmsUserMapper.toDto(rmsUser));
    }

    @Transactional
    public Optional<RmsUserDTO> update(RmsUserDTO rmsUserDTO) {
        LOG.debug("Request to update RmsUser : {}", rmsUserDTO);
        RmsUser rmsUser = rmsUserMapper.toEntity(rmsUserDTO);
        rmsUser = rmsUserRepository.save(rmsUser);
        return Optional.of(rmsUserMapper.toDto(rmsUser));
    }

    @Transactional
    public Optional<RmsUserDTO> partialUpdate(RmsUserDTO rmsUserDTO) {
        LOG.debug("Request to partially update RmsUser : {}", rmsUserDTO);
        return rmsUserRepository
            .findById(rmsUserDTO.getId())
            .map(existingRmsUser -> {
                rmsUserMapper.partialUpdate(existingRmsUser, rmsUserDTO);
                return rmsUserRepository.save(existingRmsUser);
            })
            .map(rmsUserMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<RmsUserDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RmsUsers");
        Page<RmsUser> page = rmsUserRepository.findAll(pageable);
        return page.getContent().stream()
            .map(rmsUserMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return rmsUserRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<RmsUserDTO> findOne(UUID id) {
        LOG.debug("Request to get RmsUser : {}", id);
        return rmsUserRepository.findById(id).map(rmsUserMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete RmsUser : {}", id);
        rmsUserRepository.deleteById(id);
    }
}
