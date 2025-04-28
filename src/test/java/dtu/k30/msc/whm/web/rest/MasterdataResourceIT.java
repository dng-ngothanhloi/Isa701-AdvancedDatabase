package dtu.k30.msc.whm.web.rest;

import static dtu.k30.msc.whm.domain.MasterdataAsserts.*;
import static dtu.k30.msc.whm.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtu.k30.msc.whm.IntegrationTest;
import dtu.k30.msc.whm.domain.Masterdata;
import dtu.k30.msc.whm.repository.MasterdataRepository;
import dtu.k30.msc.whm.service.dto.MasterdataDTO;
import dtu.k30.msc.whm.service.mapper.MasterdataMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link MasterdataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MasterdataResourceIT {

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DATA_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/masterdata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MasterdataRepository masterdataRepository;

    @Autowired
    private MasterdataMapper masterdataMapper;

    @Autowired
    private MockMvc restMasterdataMockMvc;

    private Masterdata masterdata;

    private Masterdata insertedMasterdata;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Masterdata createEntity() {
        return new Masterdata()
                .category(DEFAULT_CATEGORY)
                .dataKey(DEFAULT_DATA_KEY)
                .dataValue(DEFAULT_DATA_VALUE)
                .isDeleted(DEFAULT_IS_DELETED)
                .createdAt(DEFAULT_CREATED_AT)
                .createdBy(DEFAULT_CREATED_BY)
                .updatedAt(DEFAULT_UPDATED_AT)
                .updatedBy(DEFAULT_UPDATED_BY);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Masterdata createUpdatedEntity() {
        return new Masterdata()
                .category(UPDATED_CATEGORY)
                .dataKey(UPDATED_DATA_KEY)
                .dataValue(UPDATED_DATA_VALUE)
                .isDeleted(UPDATED_IS_DELETED)
                .createdAt(UPDATED_CREATED_AT)
                .createdBy(UPDATED_CREATED_BY)
                .updatedAt(UPDATED_UPDATED_AT)
                .updatedBy(UPDATED_UPDATED_BY);
    }

    @BeforeEach
    void initTest() {
        masterdata = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMasterdata != null) {
            masterdataRepository.delete(insertedMasterdata);
            insertedMasterdata = null;
        }
    }

    @Test
    void createMasterdata() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Masterdata
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);
        var returnedMasterdataDTO = om.readValue(
                restMasterdataMockMvc
                        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterdataDTO)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                MasterdataDTO.class
        );

        // Validate the Masterdata in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMasterdata = masterdataMapper.toEntity(returnedMasterdataDTO);
        assertMasterdataUpdatableFieldsEquals(returnedMasterdata, getPersistedMasterdata(returnedMasterdata));

        insertedMasterdata = returnedMasterdata;
    }

    @Test
    void createMasterdataWithExistingId() throws Exception {
        // Create the Masterdata with an existing ID
        masterdata.setId("existing_id");
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterdataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterdataDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Masterdata in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        masterdata.setCategory(null);

        // Create the Masterdata, which fails.
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        restMasterdataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterdataDTO)))
                .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDataKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        masterdata.setDataKey(null);

        // Create the Masterdata, which fails.
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        restMasterdataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterdataDTO)))
                .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDataValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        masterdata.setDataValue(null);

        // Create the Masterdata, which fails.
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        restMasterdataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterdataDTO)))
                .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMasterdata() throws Exception {
        // Initialize the database
        insertedMasterdata = masterdataRepository.save(masterdata);

        // Get all the masterdataList
        restMasterdataMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(masterdata.getId())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
                .andExpect(jsonPath("$.[*].dataKey").value(hasItem(DEFAULT_DATA_KEY)))
                .andExpect(jsonPath("$.[*].dataValue").value(hasItem(DEFAULT_DATA_VALUE)))
                .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
                .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    void getMasterdata() throws Exception {
        // Initialize the database
        insertedMasterdata = masterdataRepository.save(masterdata);

        // Get the masterdata
        restMasterdataMockMvc
                .perform(get(ENTITY_API_URL_ID, masterdata.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(masterdata.getId()))
                .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
                .andExpect(jsonPath("$.dataKey").value(DEFAULT_DATA_KEY))
                .andExpect(jsonPath("$.dataValue").value(DEFAULT_DATA_VALUE))
                .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED))
                .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
                .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
                .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
                .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    void getNonExistingMasterdata() throws Exception {
        // Get the masterdata
        restMasterdataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingMasterdata() throws Exception {
        // Initialize the database
        insertedMasterdata = masterdataRepository.save(masterdata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterdata
        Masterdata updatedMasterdata = masterdataRepository.findById(masterdata.getId()).orElseThrow();
        updatedMasterdata
                .category(UPDATED_CATEGORY)
                .dataKey(UPDATED_DATA_KEY)
                .dataValue(UPDATED_DATA_VALUE)
                .isDeleted(UPDATED_IS_DELETED)
                .createdAt(UPDATED_CREATED_AT)
                .createdBy(UPDATED_CREATED_BY)
                .updatedAt(UPDATED_UPDATED_AT)
                .updatedBy(UPDATED_UPDATED_BY);
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(updatedMasterdata);

        restMasterdataMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, masterdataDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsBytes(masterdataDTO))
                )
                .andExpect(status().isOk());

        // Validate the Masterdata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMasterdataToMatchAllProperties(updatedMasterdata);
    }

    @Test
    void putNonExistingMasterdata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterdata.setId(UUID.randomUUID().toString());

        // Create the Masterdata
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterdataMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, masterdataDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsBytes(masterdataDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Masterdata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMasterdata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterdata.setId(UUID.randomUUID().toString());

        // Create the Masterdata
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterdataMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsBytes(masterdataDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Masterdata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMasterdata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterdata.setId(UUID.randomUUID().toString());

        // Create the Masterdata
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterdataMockMvc
                .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterdataDTO)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Masterdata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMasterdataWithPatch() throws Exception {
        // Initialize the database
        insertedMasterdata = masterdataRepository.save(masterdata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterdata using partial update
        Masterdata partialUpdatedMasterdata = new Masterdata();
        partialUpdatedMasterdata.setId(masterdata.getId());

        partialUpdatedMasterdata
                .dataValue(UPDATED_DATA_VALUE)
                .isDeleted(UPDATED_IS_DELETED)
                .createdBy(UPDATED_CREATED_BY)
                .updatedBy(UPDATED_UPDATED_BY);

        restMasterdataMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedMasterdata.getId())
                                .contentType("application/merge-patch+json")
                                .content(om.writeValueAsBytes(partialUpdatedMasterdata))
                )
                .andExpect(status().isOk());

        // Validate the Masterdata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMasterdataUpdatableFieldsEquals(
                createUpdateProxyForBean(partialUpdatedMasterdata, masterdata),
                getPersistedMasterdata(masterdata)
        );
    }

    @Test
    void fullUpdateMasterdataWithPatch() throws Exception {
        // Initialize the database
        insertedMasterdata = masterdataRepository.save(masterdata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterdata using partial update
        Masterdata partialUpdatedMasterdata = new Masterdata();
        partialUpdatedMasterdata.setId(masterdata.getId());

        partialUpdatedMasterdata
                .category(UPDATED_CATEGORY)
                .dataKey(UPDATED_DATA_KEY)
                .dataValue(UPDATED_DATA_VALUE)
                .isDeleted(UPDATED_IS_DELETED)
                .createdAt(UPDATED_CREATED_AT)
                .createdBy(UPDATED_CREATED_BY)
                .updatedAt(UPDATED_UPDATED_AT)
                .updatedBy(UPDATED_UPDATED_BY);

        restMasterdataMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedMasterdata.getId())
                                .contentType("application/merge-patch+json")
                                .content(om.writeValueAsBytes(partialUpdatedMasterdata))
                )
                .andExpect(status().isOk());

        // Validate the Masterdata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMasterdataUpdatableFieldsEquals(partialUpdatedMasterdata, getPersistedMasterdata(partialUpdatedMasterdata));
    }

    @Test
    void patchNonExistingMasterdata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterdata.setId(UUID.randomUUID().toString());

        // Create the Masterdata
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterdataMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, masterdataDTO.getId())
                                .contentType("application/merge-patch+json")
                                .content(om.writeValueAsBytes(masterdataDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Masterdata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMasterdata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterdata.setId(UUID.randomUUID().toString());

        // Create the Masterdata
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterdataMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                                .contentType("application/merge-patch+json")
                                .content(om.writeValueAsBytes(masterdataDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Masterdata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMasterdata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterdata.setId(UUID.randomUUID().toString());

        // Create the Masterdata
        MasterdataDTO masterdataDTO = masterdataMapper.toDto(masterdata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterdataMockMvc
                .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(masterdataDTO)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Masterdata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMasterdata() throws Exception {
        // Initialize the database
        insertedMasterdata = masterdataRepository.save(masterdata);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the masterdata
        restMasterdataMockMvc
                .perform(delete(ENTITY_API_URL_ID, masterdata.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return masterdataRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Masterdata getPersistedMasterdata(Masterdata masterdata) {
        return masterdataRepository.findById(masterdata.getId()).orElseThrow();
    }

    protected void assertPersistedMasterdataToMatchAllProperties(Masterdata expectedMasterdata) {
        assertMasterdataAllPropertiesEquals(expectedMasterdata, getPersistedMasterdata(expectedMasterdata));
    }

    protected void assertPersistedMasterdataToMatchUpdatableProperties(Masterdata expectedMasterdata) {
        assertMasterdataAllUpdatablePropertiesEquals(expectedMasterdata, getPersistedMasterdata(expectedMasterdata));
    }
}
