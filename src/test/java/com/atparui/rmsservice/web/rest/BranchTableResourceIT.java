package com.atparui.rmsservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.atparui.rmsservice.IntegrationTest;
import com.atparui.rmsservice.domain.BranchTable;
import com.atparui.rmsservice.repository.BranchTableRepository;
import com.atparui.rmsservice.service.dto.BranchTableDTO;
import com.atparui.rmsservice.service.mapper.BranchTableMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Integration tests for the {@link BranchTableResource} REST controller.
 */
@IntegrationTest
@WithMockUser
class BranchTableResourceIT {

    private static final String DEFAULT_TABLE_NUMBER = "T001";
    private static final String UPDATED_TABLE_NUMBER = "T002";

    private static final String DEFAULT_TABLE_NAME = "Table 1";
    private static final String UPDATED_TABLE_NAME = "Table 2";

    private static final Integer DEFAULT_CAPACITY = 4;
    private static final Integer UPDATED_CAPACITY = 6;

    private static final String DEFAULT_FLOOR = "Ground Floor";
    private static final String UPDATED_FLOOR = "First Floor";

    private static final String DEFAULT_SECTION = "Main Hall";
    private static final String UPDATED_SECTION = "Private Room";

    private static final String DEFAULT_STATUS = "AVAILABLE";
    private static final String UPDATED_STATUS = "OCCUPIED";

    private static final String ENTITY_API_URL = "/api/branch-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BranchTableRepository branchTableRepository;

    @Autowired
    private BranchTableMapper branchTableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc restBranchTableMockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BranchTable branchTable;

    /**
     * Create an entity for this test.
     */
    public static BranchTable createEntity(EntityManager em) {
        BranchTable branchTable = new BranchTable();
        branchTable.setId(UUID.randomUUID());
        branchTable.setTableNumber(DEFAULT_TABLE_NUMBER);
        branchTable.setTableName(DEFAULT_TABLE_NAME);
        branchTable.setCapacity(DEFAULT_CAPACITY);
        branchTable.setFloor(DEFAULT_FLOOR);
        branchTable.setSection(DEFAULT_SECTION);
        branchTable.setStatus(DEFAULT_STATUS);
        return branchTable;
    }

    @BeforeEach
    public void initTest() {
        restBranchTableMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        branchTable = createEntity(em);
    }

    @Test
    @Transactional
    void createBranchTable() throws Exception {
        long databaseSizeBeforeCreate = branchTableRepository.count();

        // Create the BranchTable
        BranchTableDTO branchTableDTO = branchTableMapper.toDto(branchTable);
        restBranchTableMockMvc
            .perform(post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(branchTableDTO)))
            .andExpect(status().isCreated());

        // Validate the BranchTable in the database
        List<BranchTable> branchTableList = branchTableRepository.findAll();
        assertThat(branchTableList).hasSize((int) databaseSizeBeforeCreate + 1);
        BranchTable testBranchTable = branchTableList.get(branchTableList.size() - 1);
        assertThat(testBranchTable.getTableNumber()).isEqualTo(DEFAULT_TABLE_NUMBER);
        assertThat(testBranchTable.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testBranchTable.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testBranchTable.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testBranchTable.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testBranchTable.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createBranchTableWithExistingId() throws Exception {
        // Create the BranchTable with an existing ID
        branchTable.setId(UUID.randomUUID());
        BranchTableDTO branchTableDTO = branchTableMapper.toDto(branchTable);

        long databaseSizeBeforeCreate = branchTableRepository.count();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchTableMockMvc
            .perform(post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(branchTableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchTable in the database
        List<BranchTable> branchTableList = branchTableRepository.findAll();
        assertThat(branchTableList).hasSize((int) databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTableNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = branchTableRepository.count();
        // set the field null
        branchTable.setTableNumber(null);

        // Create the BranchTable, which fails.
        BranchTableDTO branchTableDTO = branchTableMapper.toDto(branchTable);

        restBranchTableMockMvc
            .perform(post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(branchTableDTO)))
            .andExpect(status().isBadRequest());

        List<BranchTable> branchTableList = branchTableRepository.findAll();
        assertThat(branchTableList).hasSize((int) databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBranchTables() throws Exception {
        // Initialize the database
        branchTableRepository.saveAndFlush(branchTable);

        // Get all the branchTableList
        restBranchTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchTable.getId().toString())))
            .andExpect(jsonPath("$.[*].tableNumber").value(hasItem(DEFAULT_TABLE_NUMBER)))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getBranchTable() throws Exception {
        // Initialize the database
        branchTableRepository.saveAndFlush(branchTable);

        // Get the branchTable
        restBranchTableMockMvc
            .perform(get(ENTITY_API_URL_ID, branchTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branchTable.getId().toString()))
            .andExpect(jsonPath("$.tableNumber").value(DEFAULT_TABLE_NUMBER))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.floor").value(DEFAULT_FLOOR))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingBranchTable() throws Exception {
        // Get the branchTable
        restBranchTableMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBranchTable() throws Exception {
        // Initialize the database
        branchTableRepository.saveAndFlush(branchTable);

        long databaseSizeBeforeUpdate = branchTableRepository.count();

        // Update the branchTable
        BranchTable updatedBranchTable = branchTableRepository.findById(branchTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBranchTable are not directly saved in db
        em.detach(updatedBranchTable);
        updatedBranchTable
            .tableNumber(UPDATED_TABLE_NUMBER)
            .tableName(UPDATED_TABLE_NAME)
            .capacity(UPDATED_CAPACITY)
            .floor(UPDATED_FLOOR)
            .section(UPDATED_SECTION)
            .status(UPDATED_STATUS);
        BranchTableDTO branchTableDTO = branchTableMapper.toDto(updatedBranchTable);

        restBranchTableMockMvc
            .perform(put(ENTITY_API_URL_ID, branchTableDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(branchTableDTO)))
            .andExpect(status().isOk());

        // Validate the BranchTable in the database
        List<BranchTable> branchTableList = branchTableRepository.findAll();
        assertThat(branchTableList).hasSize((int) databaseSizeBeforeUpdate);
        BranchTable testBranchTable = branchTableList.get(branchTableList.size() - 1);
        assertThat(testBranchTable.getTableNumber()).isEqualTo(UPDATED_TABLE_NUMBER);
        assertThat(testBranchTable.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testBranchTable.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testBranchTable.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testBranchTable.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testBranchTable.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingBranchTable() throws Exception {
        long databaseSizeBeforeUpdate = branchTableRepository.count();
        branchTable.setId(UUID.randomUUID());

        // Create the BranchTable
        BranchTableDTO branchTableDTO = branchTableMapper.toDto(branchTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchTableMockMvc
            .perform(put(ENTITY_API_URL_ID, branchTableDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(branchTableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchTable in the database
        List<BranchTable> branchTableList = branchTableRepository.findAll();
        assertThat(branchTableList).hasSize((int) databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranchTable() throws Exception {
        // Initialize the database
        branchTableRepository.saveAndFlush(branchTable);

        long databaseSizeBeforeDelete = branchTableRepository.count();

        // Delete the branchTable
        restBranchTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, branchTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchTable> branchTableList = branchTableRepository.findAll();
        assertThat(branchTableList).hasSize((int) databaseSizeBeforeDelete - 1);
    }
}
