package com.atparui.rmsservice.service;
import org.springframework.data.domain.Page;
import com.atparui.rmsservice.domain.UserBranchRole;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.repository.UserBranchRoleRepository;
import com.atparui.rmsservice.service.dto.UserBranchRoleAssignmentDTO;
import com.atparui.rmsservice.service.dto.UserBranchRoleDTO;
import com.atparui.rmsservice.service.mapper.UserBranchRoleMapper;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for managing {@link com.atparui.rmsservice.domain.UserBranchRole}.
 */
@Service
@Transactional
public class UserBranchRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(UserBranchRoleService.class);

    private final UserBranchRoleRepository userBranchRoleRepository;

    private final UserBranchRoleMapper userBranchRoleMapper;

    public UserBranchRoleService(UserBranchRoleRepository userBranchRoleRepository, UserBranchRoleMapper userBranchRoleMapper) {
        this.userBranchRoleRepository = userBranchRoleRepository;
        this.userBranchRoleMapper = userBranchRoleMapper;
    }
    @Transactional
    public Optional<UserBranchRoleDTO> save(UserBranchRoleDTO userBranchRoleDTO) {
        LOG.debug("Request to save UserBranchRole : {}", userBranchRoleDTO);
        UserBranchRole userBranchRole = userBranchRoleMapper.toEntity(userBranchRoleDTO);
        userBranchRole = userBranchRoleRepository.save(userBranchRole);
        return Optional.of(userBranchRoleMapper.toDto(userBranchRole));
    }

    @Transactional
    public Optional<UserBranchRoleDTO> update(UserBranchRoleDTO userBranchRoleDTO) {
        LOG.debug("Request to update UserBranchRole : {}", userBranchRoleDTO);
        UserBranchRole userBranchRole = userBranchRoleMapper.toEntity(userBranchRoleDTO);
        userBranchRole = userBranchRoleRepository.save(userBranchRole);
        return Optional.of(userBranchRoleMapper.toDto(userBranchRole));
    }

    @Transactional
    public Optional<UserBranchRoleDTO> partialUpdate(UserBranchRoleDTO userBranchRoleDTO) {
        LOG.debug("Request to partially update UserBranchRole : {}", userBranchRoleDTO);
        return userBranchRoleRepository
            .findById(userBranchRoleDTO.getId())
            .map(existingUserBranchRole -> {
                userBranchRoleMapper.partialUpdate(existingUserBranchRole, userBranchRoleDTO);
                return userBranchRoleRepository.save(existingUserBranchRole);
            })
            .map(userBranchRoleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<UserBranchRoleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserBranchRoles");
        Page<UserBranchRole> page = userBranchRoleRepository.findAll(pageable);
        return page.getContent().stream()
            .map(userBranchRoleMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return userBranchRoleRepository.count();
    }

    @Transactional(readOnly = true)
    public Optional<UserBranchRoleDTO> findOne(UUID id) {
        LOG.debug("Request to get UserBranchRole : {}", id);
        return userBranchRoleRepository.findById(id).map(userBranchRoleMapper::toDto);
    }

    @Transactional
    public void delete(UUID id) {
        LOG.debug("Request to delete UserBranchRole : {}", id);
        userBranchRoleRepository.deleteById(id);
    }
}
