package com.wailo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wailo.IntegrationTest;
import com.wailo.domain.Pad;
import com.wailo.repository.PadRepository;
import com.wailo.service.dto.PadDTO;
import com.wailo.service.mapper.PadMapper;
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
 * Integration tests for the {@link PadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PadResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PadRepository padRepository;

    @Autowired
    private PadMapper padMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPadMockMvc;

    private Pad pad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pad createEntity(EntityManager em) {
        Pad pad = new Pad().name(DEFAULT_NAME);
        return pad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pad createUpdatedEntity(EntityManager em) {
        Pad pad = new Pad().name(UPDATED_NAME);
        return pad;
    }

    @BeforeEach
    public void initTest() {
        pad = createEntity(em);
    }

    @Test
    @Transactional
    void createPad() throws Exception {
        int databaseSizeBeforeCreate = padRepository.findAll().size();
        // Create the Pad
        PadDTO padDTO = padMapper.toDto(pad);
        restPadMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeCreate + 1);
        Pad testPad = padList.get(padList.size() - 1);
        assertThat(testPad.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPadWithExistingId() throws Exception {
        // Create the Pad with an existing ID
        pad.setId(1L);
        PadDTO padDTO = padMapper.toDto(pad);

        int databaseSizeBeforeCreate = padRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPadMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = padRepository.findAll().size();
        // set the field null
        pad.setName(null);

        // Create the Pad, which fails.
        PadDTO padDTO = padMapper.toDto(pad);

        restPadMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isBadRequest());

        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPads() throws Exception {
        // Initialize the database
        padRepository.saveAndFlush(pad);

        // Get all the padList
        restPadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pad.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPad() throws Exception {
        // Initialize the database
        padRepository.saveAndFlush(pad);

        // Get the pad
        restPadMockMvc
            .perform(get(ENTITY_API_URL_ID, pad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pad.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPad() throws Exception {
        // Get the pad
        restPadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPad() throws Exception {
        // Initialize the database
        padRepository.saveAndFlush(pad);

        int databaseSizeBeforeUpdate = padRepository.findAll().size();

        // Update the pad
        Pad updatedPad = padRepository.findById(pad.getId()).get();
        // Disconnect from session so that the updates on updatedPad are not directly saved in db
        em.detach(updatedPad);
        updatedPad.name(UPDATED_NAME);
        PadDTO padDTO = padMapper.toDto(updatedPad);

        restPadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, padDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
        Pad testPad = padList.get(padList.size() - 1);
        assertThat(testPad.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPad() throws Exception {
        int databaseSizeBeforeUpdate = padRepository.findAll().size();
        pad.setId(count.incrementAndGet());

        // Create the Pad
        PadDTO padDTO = padMapper.toDto(pad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, padDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPad() throws Exception {
        int databaseSizeBeforeUpdate = padRepository.findAll().size();
        pad.setId(count.incrementAndGet());

        // Create the Pad
        PadDTO padDTO = padMapper.toDto(pad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPad() throws Exception {
        int databaseSizeBeforeUpdate = padRepository.findAll().size();
        pad.setId(count.incrementAndGet());

        // Create the Pad
        PadDTO padDTO = padMapper.toDto(pad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPadMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePadWithPatch() throws Exception {
        // Initialize the database
        padRepository.saveAndFlush(pad);

        int databaseSizeBeforeUpdate = padRepository.findAll().size();

        // Update the pad using partial update
        Pad partialUpdatedPad = new Pad();
        partialUpdatedPad.setId(pad.getId());

        partialUpdatedPad.name(UPDATED_NAME);

        restPadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPad.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPad))
            )
            .andExpect(status().isOk());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
        Pad testPad = padList.get(padList.size() - 1);
        assertThat(testPad.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePadWithPatch() throws Exception {
        // Initialize the database
        padRepository.saveAndFlush(pad);

        int databaseSizeBeforeUpdate = padRepository.findAll().size();

        // Update the pad using partial update
        Pad partialUpdatedPad = new Pad();
        partialUpdatedPad.setId(pad.getId());

        partialUpdatedPad.name(UPDATED_NAME);

        restPadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPad.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPad))
            )
            .andExpect(status().isOk());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
        Pad testPad = padList.get(padList.size() - 1);
        assertThat(testPad.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPad() throws Exception {
        int databaseSizeBeforeUpdate = padRepository.findAll().size();
        pad.setId(count.incrementAndGet());

        // Create the Pad
        PadDTO padDTO = padMapper.toDto(pad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, padDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPad() throws Exception {
        int databaseSizeBeforeUpdate = padRepository.findAll().size();
        pad.setId(count.incrementAndGet());

        // Create the Pad
        PadDTO padDTO = padMapper.toDto(pad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPad() throws Exception {
        int databaseSizeBeforeUpdate = padRepository.findAll().size();
        pad.setId(count.incrementAndGet());

        // Create the Pad
        PadDTO padDTO = padMapper.toDto(pad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPadMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(padDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pad in the database
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePad() throws Exception {
        // Initialize the database
        padRepository.saveAndFlush(pad);

        int databaseSizeBeforeDelete = padRepository.findAll().size();

        // Delete the pad
        restPadMockMvc
            .perform(delete(ENTITY_API_URL_ID, pad.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pad> padList = padRepository.findAll();
        assertThat(padList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
