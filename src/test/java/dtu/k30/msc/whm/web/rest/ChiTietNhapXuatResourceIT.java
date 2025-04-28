package dtu.k30.msc.whm.web.rest;

import static dtu.k30.msc.whm.domain.ChiTietNhapXuatAsserts.*;
import static dtu.k30.msc.whm.web.rest.TestUtil.createUpdateProxyForBean;
import static dtu.k30.msc.whm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtu.k30.msc.whm.IntegrationTest;
import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import dtu.k30.msc.whm.repository.ChiTietNhapXuatRepository;
import dtu.k30.msc.whm.service.dto.ChiTietNhapXuatDTO;
import dtu.k30.msc.whm.service.mapper.ChiTietNhapXuatMapper;

import java.math.BigDecimal;
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
 * Integration tests for the {@link ChiTietNhapXuatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChiTietNhapXuatResourceIT {

    private static final Integer DEFAULT_SO_LUONG = 1;
    private static final Integer UPDATED_SO_LUONG = 2;

    private static final BigDecimal DEFAULT_DON_GIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_DON_GIA = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/chi-tiet-nhap-xuats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChiTietNhapXuatRepository chiTietNhapXuatRepository;

    @Autowired
    private ChiTietNhapXuatMapper chiTietNhapXuatMapper;

    @Autowired
    private MockMvc restChiTietNhapXuatMockMvc;

    private ChiTietNhapXuat chiTietNhapXuat;

    private ChiTietNhapXuat insertedChiTietNhapXuat;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChiTietNhapXuat createEntity() {
        return new ChiTietNhapXuat()
                .soLuong(DEFAULT_SO_LUONG)
                .donGia(DEFAULT_DON_GIA)
                .createdAt(DEFAULT_CREATED_AT)
                .createdBy(DEFAULT_CREATED_BY)
                .updatedAt(DEFAULT_UPDATED_AT)
                .updatedBy(DEFAULT_UPDATED_BY)
                .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChiTietNhapXuat createUpdatedEntity() {
        return new ChiTietNhapXuat()
                .soLuong(UPDATED_SO_LUONG)
                .donGia(UPDATED_DON_GIA)
                .createdAt(UPDATED_CREATED_AT)
                .createdBy(UPDATED_CREATED_BY)
                .updatedAt(UPDATED_UPDATED_AT)
                .updatedBy(UPDATED_UPDATED_BY)
                .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    void initTest() {
        chiTietNhapXuat = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedChiTietNhapXuat != null) {
            chiTietNhapXuatRepository.delete(insertedChiTietNhapXuat);
            insertedChiTietNhapXuat = null;
        }
    }

    @Test
    void createChiTietNhapXuat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChiTietNhapXuat
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);
        var returnedChiTietNhapXuatDTO = om.readValue(
                restChiTietNhapXuatMockMvc
                        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chiTietNhapXuatDTO)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ChiTietNhapXuatDTO.class
        );

        // Validate the ChiTietNhapXuat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChiTietNhapXuat = chiTietNhapXuatMapper.toEntity(returnedChiTietNhapXuatDTO);
        assertChiTietNhapXuatUpdatableFieldsEquals(returnedChiTietNhapXuat, getPersistedChiTietNhapXuat(returnedChiTietNhapXuat));

        insertedChiTietNhapXuat = returnedChiTietNhapXuat;
    }

    @Test
    void createChiTietNhapXuatWithExistingId() throws Exception {
        // Create the ChiTietNhapXuat with an existing ID
        chiTietNhapXuat.setId("existing_id");
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChiTietNhapXuatMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chiTietNhapXuatDTO)))
                .andExpect(status().isBadRequest());

        // Validate the ChiTietNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSoLuongIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chiTietNhapXuat.setSoLuong(null);

        // Create the ChiTietNhapXuat, which fails.
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);

        restChiTietNhapXuatMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chiTietNhapXuatDTO)))
                .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllChiTietNhapXuats() throws Exception {
        // Initialize the database
        insertedChiTietNhapXuat = chiTietNhapXuatRepository.save(chiTietNhapXuat);

        // Get all the chiTietNhapXuatList
        restChiTietNhapXuatMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(chiTietNhapXuat.getId())))
                .andExpect(jsonPath("$.[*].soLuong").value(hasItem(DEFAULT_SO_LUONG)))
                .andExpect(jsonPath("$.[*].donGia").value(hasItem(sameNumber(DEFAULT_DON_GIA))))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
                .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
                .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    void getChiTietNhapXuat() throws Exception {
        // Initialize the database
        insertedChiTietNhapXuat = chiTietNhapXuatRepository.save(chiTietNhapXuat);

        // Get the chiTietNhapXuat
        restChiTietNhapXuatMockMvc
                .perform(get(ENTITY_API_URL_ID, chiTietNhapXuat.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(chiTietNhapXuat.getId()))
                .andExpect(jsonPath("$.soLuong").value(DEFAULT_SO_LUONG))
                .andExpect(jsonPath("$.donGia").value(sameNumber(DEFAULT_DON_GIA)))
                .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
                .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
                .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
                .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
                .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    void getNonExistingChiTietNhapXuat() throws Exception {
        // Get the chiTietNhapXuat
        restChiTietNhapXuatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingChiTietNhapXuat() throws Exception {
        // Initialize the database
        insertedChiTietNhapXuat = chiTietNhapXuatRepository.save(chiTietNhapXuat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chiTietNhapXuat
        ChiTietNhapXuat updatedChiTietNhapXuat = chiTietNhapXuatRepository.findById(chiTietNhapXuat.getId()).orElseThrow();
        updatedChiTietNhapXuat
                .soLuong(UPDATED_SO_LUONG)
                .donGia(UPDATED_DON_GIA)
                .createdAt(UPDATED_CREATED_AT)
                .createdBy(UPDATED_CREATED_BY)
                .updatedAt(UPDATED_UPDATED_AT)
                .updatedBy(UPDATED_UPDATED_BY)
                .isDeleted(UPDATED_IS_DELETED);
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(updatedChiTietNhapXuat);

        restChiTietNhapXuatMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, chiTietNhapXuatDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsBytes(chiTietNhapXuatDTO))
                )
                .andExpect(status().isOk());

        // Validate the ChiTietNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChiTietNhapXuatToMatchAllProperties(updatedChiTietNhapXuat);
    }

    @Test
    void putNonExistingChiTietNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chiTietNhapXuat.setId(UUID.randomUUID().toString());

        // Create the ChiTietNhapXuat
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChiTietNhapXuatMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, chiTietNhapXuatDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsBytes(chiTietNhapXuatDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the ChiTietNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChiTietNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chiTietNhapXuat.setId(UUID.randomUUID().toString());

        // Create the ChiTietNhapXuat
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChiTietNhapXuatMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsBytes(chiTietNhapXuatDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the ChiTietNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChiTietNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chiTietNhapXuat.setId(UUID.randomUUID().toString());

        // Create the ChiTietNhapXuat
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChiTietNhapXuatMockMvc
                .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chiTietNhapXuatDTO)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the ChiTietNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChiTietNhapXuatWithPatch() throws Exception {
        // Initialize the database
        insertedChiTietNhapXuat = chiTietNhapXuatRepository.save(chiTietNhapXuat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chiTietNhapXuat using partial update
        ChiTietNhapXuat partialUpdatedChiTietNhapXuat = new ChiTietNhapXuat();
        partialUpdatedChiTietNhapXuat.setId(chiTietNhapXuat.getId());

        partialUpdatedChiTietNhapXuat.soLuong(UPDATED_SO_LUONG).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restChiTietNhapXuatMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedChiTietNhapXuat.getId())
                                .contentType("application/merge-patch+json")
                                .content(om.writeValueAsBytes(partialUpdatedChiTietNhapXuat))
                )
                .andExpect(status().isOk());

        // Validate the ChiTietNhapXuat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChiTietNhapXuatUpdatableFieldsEquals(
                createUpdateProxyForBean(partialUpdatedChiTietNhapXuat, chiTietNhapXuat),
                getPersistedChiTietNhapXuat(chiTietNhapXuat)
        );
    }

    @Test
    void fullUpdateChiTietNhapXuatWithPatch() throws Exception {
        // Initialize the database
        insertedChiTietNhapXuat = chiTietNhapXuatRepository.save(chiTietNhapXuat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chiTietNhapXuat using partial update
        ChiTietNhapXuat partialUpdatedChiTietNhapXuat = new ChiTietNhapXuat();
        partialUpdatedChiTietNhapXuat.setId(chiTietNhapXuat.getId());

        partialUpdatedChiTietNhapXuat
                .soLuong(UPDATED_SO_LUONG)
                .donGia(UPDATED_DON_GIA)
                .createdAt(UPDATED_CREATED_AT)
                .createdBy(UPDATED_CREATED_BY)
                .updatedAt(UPDATED_UPDATED_AT)
                .updatedBy(UPDATED_UPDATED_BY)
                .isDeleted(UPDATED_IS_DELETED);

        restChiTietNhapXuatMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, partialUpdatedChiTietNhapXuat.getId())
                                .contentType("application/merge-patch+json")
                                .content(om.writeValueAsBytes(partialUpdatedChiTietNhapXuat))
                )
                .andExpect(status().isOk());

        // Validate the ChiTietNhapXuat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChiTietNhapXuatUpdatableFieldsEquals(
                partialUpdatedChiTietNhapXuat,
                getPersistedChiTietNhapXuat(partialUpdatedChiTietNhapXuat)
        );
    }

    @Test
    void patchNonExistingChiTietNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chiTietNhapXuat.setId(UUID.randomUUID().toString());

        // Create the ChiTietNhapXuat
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChiTietNhapXuatMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, chiTietNhapXuatDTO.getId())
                                .contentType("application/merge-patch+json")
                                .content(om.writeValueAsBytes(chiTietNhapXuatDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the ChiTietNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChiTietNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chiTietNhapXuat.setId(UUID.randomUUID().toString());

        // Create the ChiTietNhapXuat
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChiTietNhapXuatMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                                .contentType("application/merge-patch+json")
                                .content(om.writeValueAsBytes(chiTietNhapXuatDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the ChiTietNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChiTietNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chiTietNhapXuat.setId(UUID.randomUUID().toString());

        // Create the ChiTietNhapXuat
        ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(chiTietNhapXuat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChiTietNhapXuatMockMvc
                .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chiTietNhapXuatDTO)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the ChiTietNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChiTietNhapXuat() throws Exception {
        // Initialize the database
        insertedChiTietNhapXuat = chiTietNhapXuatRepository.save(chiTietNhapXuat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chiTietNhapXuat
        restChiTietNhapXuatMockMvc
                .perform(delete(ENTITY_API_URL_ID, chiTietNhapXuat.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chiTietNhapXuatRepository.count();
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

    protected ChiTietNhapXuat getPersistedChiTietNhapXuat(ChiTietNhapXuat chiTietNhapXuat) {
        return chiTietNhapXuatRepository.findById(chiTietNhapXuat.getId()).orElseThrow();
    }

    protected void assertPersistedChiTietNhapXuatToMatchAllProperties(ChiTietNhapXuat expectedChiTietNhapXuat) {
        assertChiTietNhapXuatAllPropertiesEquals(expectedChiTietNhapXuat, getPersistedChiTietNhapXuat(expectedChiTietNhapXuat));
    }

    protected void assertPersistedChiTietNhapXuatToMatchUpdatableProperties(ChiTietNhapXuat expectedChiTietNhapXuat) {
        assertChiTietNhapXuatAllUpdatablePropertiesEquals(expectedChiTietNhapXuat, getPersistedChiTietNhapXuat(expectedChiTietNhapXuat));
    }
}
