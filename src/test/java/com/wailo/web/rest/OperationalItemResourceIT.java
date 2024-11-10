package com.wailo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wailo.IntegrationTest;
import com.wailo.domain.Location;
import com.wailo.domain.OperationalItem;
import com.wailo.domain.enumeration.OperationalItemTypes;
import com.wailo.repository.OperationalItemRepository;
import com.wailo.service.dto.OperationalItemDTO;
import com.wailo.service.mapper.OperationalItemMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OperationalItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperationalItemResourceIT {

    private static final OperationalItemTypes DEFAULT_TYPE = OperationalItemTypes.OPERATIONAL_ALARM;
    private static final OperationalItemTypes UPDATED_TYPE = OperationalItemTypes.OPERATIONAL_ACTION;

    private static final Double DEFAULT_PRIORITY_SCORE = 1D;
    private static final Double UPDATED_PRIORITY_SCORE = 2D;

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/operational-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperationalItemRepository operationalItemRepository;

    @Autowired
    private OperationalItemMapper operationalItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperationalItemMockMvc;

    private OperationalItem operationalItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperationalItem createEntity(EntityManager em) {
        OperationalItem operationalItem = new OperationalItem()
            .type(DEFAULT_TYPE)
            .priorityScore(DEFAULT_PRIORITY_SCORE)
            .dueDate(DEFAULT_DUE_DATE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        operationalItem.setLocation(location);
        return operationalItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperationalItem createUpdatedEntity(EntityManager em) {
        OperationalItem operationalItem = new OperationalItem()
            .type(UPDATED_TYPE)
            .priorityScore(UPDATED_PRIORITY_SCORE)
            .dueDate(UPDATED_DUE_DATE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        operationalItem.setLocation(location);
        return operationalItem;
    }

    @BeforeEach
    public void initTest() {
        operationalItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOperationalItem() throws Exception {
        int databaseSizeBeforeCreate = operationalItemRepository.findAll().size();
        // Create the OperationalItem
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);
        restOperationalItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeCreate + 1);
        OperationalItem testOperationalItem = operationalItemList.get(operationalItemList.size() - 1);
        assertThat(testOperationalItem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOperationalItem.getPriorityScore()).isEqualTo(DEFAULT_PRIORITY_SCORE);
        assertThat(testOperationalItem.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    void createOperationalItemWithExistingId() throws Exception {
        // Create the OperationalItem with an existing ID
        operationalItem.setId(1L);
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        int databaseSizeBeforeCreate = operationalItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationalItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationalItemRepository.findAll().size();
        // set the field null
        operationalItem.setType(null);

        // Create the OperationalItem, which fails.
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        restOperationalItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationalItemRepository.findAll().size();
        // set the field null
        operationalItem.setPriorityScore(null);

        // Create the OperationalItem, which fails.
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        restOperationalItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperationalItems() throws Exception {
        // Initialize the database
        operationalItemRepository.saveAndFlush(operationalItem);

        // Get all the operationalItemList
        restOperationalItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operationalItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priorityScore").value(hasItem(DEFAULT_PRIORITY_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())));
    }

    @Test
    @Transactional
    void getOperationalItem() throws Exception {
        // Initialize the database
        operationalItemRepository.saveAndFlush(operationalItem);

        // Get the operationalItem
        restOperationalItemMockMvc
            .perform(get(ENTITY_API_URL_ID, operationalItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operationalItem.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.priorityScore").value(DEFAULT_PRIORITY_SCORE.doubleValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOperationalItem() throws Exception {
        // Get the operationalItem
        restOperationalItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperationalItem() throws Exception {
        // Initialize the database
        operationalItemRepository.saveAndFlush(operationalItem);

        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();

        // Update the operationalItem
        OperationalItem updatedOperationalItem = operationalItemRepository.findById(operationalItem.getId()).get();
        // Disconnect from session so that the updates on updatedOperationalItem are not directly saved in db
        em.detach(updatedOperationalItem);
        updatedOperationalItem.type(UPDATED_TYPE).priorityScore(UPDATED_PRIORITY_SCORE).dueDate(UPDATED_DUE_DATE);
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(updatedOperationalItem);

        restOperationalItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationalItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
        OperationalItem testOperationalItem = operationalItemList.get(operationalItemList.size() - 1);
        assertThat(testOperationalItem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOperationalItem.getPriorityScore()).isEqualTo(UPDATED_PRIORITY_SCORE);
        assertThat(testOperationalItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOperationalItem() throws Exception {
        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();
        operationalItem.setId(count.incrementAndGet());

        // Create the OperationalItem
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationalItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationalItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperationalItem() throws Exception {
        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();
        operationalItem.setId(count.incrementAndGet());

        // Create the OperationalItem
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationalItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperationalItem() throws Exception {
        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();
        operationalItem.setId(count.incrementAndGet());

        // Create the OperationalItem
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationalItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperationalItemWithPatch() throws Exception {
        // Initialize the database
        operationalItemRepository.saveAndFlush(operationalItem);

        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();

        // Update the operationalItem using partial update
        OperationalItem partialUpdatedOperationalItem = new OperationalItem();
        partialUpdatedOperationalItem.setId(operationalItem.getId());

        partialUpdatedOperationalItem.priorityScore(UPDATED_PRIORITY_SCORE).dueDate(UPDATED_DUE_DATE);

        restOperationalItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperationalItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperationalItem))
            )
            .andExpect(status().isOk());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
        OperationalItem testOperationalItem = operationalItemList.get(operationalItemList.size() - 1);
        assertThat(testOperationalItem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOperationalItem.getPriorityScore()).isEqualTo(UPDATED_PRIORITY_SCORE);
        assertThat(testOperationalItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOperationalItemWithPatch() throws Exception {
        // Initialize the database
        operationalItemRepository.saveAndFlush(operationalItem);

        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();

        // Update the operationalItem using partial update
        OperationalItem partialUpdatedOperationalItem = new OperationalItem();
        partialUpdatedOperationalItem.setId(operationalItem.getId());

        partialUpdatedOperationalItem.type(UPDATED_TYPE).priorityScore(UPDATED_PRIORITY_SCORE).dueDate(UPDATED_DUE_DATE);

        restOperationalItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperationalItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperationalItem))
            )
            .andExpect(status().isOk());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
        OperationalItem testOperationalItem = operationalItemList.get(operationalItemList.size() - 1);
        assertThat(testOperationalItem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOperationalItem.getPriorityScore()).isEqualTo(UPDATED_PRIORITY_SCORE);
        assertThat(testOperationalItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOperationalItem() throws Exception {
        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();
        operationalItem.setId(count.incrementAndGet());

        // Create the OperationalItem
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationalItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operationalItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperationalItem() throws Exception {
        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();
        operationalItem.setId(count.incrementAndGet());

        // Create the OperationalItem
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationalItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperationalItem() throws Exception {
        int databaseSizeBeforeUpdate = operationalItemRepository.findAll().size();
        operationalItem.setId(count.incrementAndGet());

        // Create the OperationalItem
        OperationalItemDTO operationalItemDTO = operationalItemMapper.toDto(operationalItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationalItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationalItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OperationalItem in the database
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperationalItem() throws Exception {
        // Initialize the database
        operationalItemRepository.saveAndFlush(operationalItem);

        int databaseSizeBeforeDelete = operationalItemRepository.findAll().size();

        // Delete the operationalItem
        restOperationalItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, operationalItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OperationalItem> operationalItemList = operationalItemRepository.findAll();
        assertThat(operationalItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
