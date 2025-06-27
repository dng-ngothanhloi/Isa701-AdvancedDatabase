package dtu.k30.msc.whm.web.rest;

import static dtu.k30.msc.whm.domain.PhieuNhapXuatAsserts.*;
import static dtu.k30.msc.whm.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtu.k30.msc.whm.IntegrationTest;
import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.domain.enumeration.VoucherType;
import dtu.k30.msc.whm.repository.PhieuNhapXuatRepository;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import dtu.k30.msc.whm.service.mapper.PhieuNhapXuatMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PhieuNhapXuatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhieuNhapXuatResourceIT {

    private static final String DEFAULT_MA_PHIEU = "AAAAAAAAAA";
    private static final String UPDATED_MA_PHIEU = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NGAY_LAP_PHIEU = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NGAY_LAP_PHIEU = LocalDate.now(ZoneId.systemDefault());

    private static final VoucherType DEFAULT_LOAI_PHIEU = VoucherType.Nhap;
    private static final VoucherType UPDATED_LOAI_PHIEU = VoucherType.Xuat;

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

    private static final String ENTITY_API_URL = "/api/phieu-nhap-xuats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PhieuNhapXuatRepository phieuNhapXuatRepository;

    @Autowired
    private PhieuNhapXuatMapper phieuNhapXuatMapper;

    @Autowired
    private MockMvc restPhieuNhapXuatMockMvc;

    private PhieuNhapXuat phieuNhapXuat;

    private PhieuNhapXuat insertedPhieuNhapXuat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhieuNhapXuat createEntity() {
        return new PhieuNhapXuat()
            .maPhieu(DEFAULT_MA_PHIEU)
            .ngayLapPhieu(DEFAULT_NGAY_LAP_PHIEU)
            .loaiPhieu(DEFAULT_LOAI_PHIEU)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhieuNhapXuat createUpdatedEntity() {
        return new PhieuNhapXuat()
            .maPhieu(UPDATED_MA_PHIEU)
            .ngayLapPhieu(UPDATED_NGAY_LAP_PHIEU)
            .loaiPhieu(UPDATED_LOAI_PHIEU)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    void initTest() {
        phieuNhapXuat = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPhieuNhapXuat != null) {
            phieuNhapXuatRepository.delete(insertedPhieuNhapXuat);
            insertedPhieuNhapXuat = null;
        }
    }

    @Test
    void createPhieuNhapXuat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PhieuNhapXuat
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(phieuNhapXuat);
        var returnedPhieuNhapXuatDTO = om.readValue(
            restPhieuNhapXuatMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phieuNhapXuatDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PhieuNhapXuatDTO.class
        );

        // Validate the PhieuNhapXuat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPhieuNhapXuat = phieuNhapXuatMapper.toEntity(returnedPhieuNhapXuatDTO);
        assertPhieuNhapXuatUpdatableFieldsEquals(returnedPhieuNhapXuat, getPersistedPhieuNhapXuat(returnedPhieuNhapXuat));

        insertedPhieuNhapXuat = returnedPhieuNhapXuat;
    }

    @Test
    void createPhieuNhapXuatWithExistingId() throws Exception {
        // Create the PhieuNhapXuat with an existing ID
        phieuNhapXuat.setId("existing_id");
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(phieuNhapXuat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhieuNhapXuatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phieuNhapXuatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PhieuNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPhieuNhapXuats() throws Exception {
        // Initialize the database
        insertedPhieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);

        // Get all the phieuNhapXuatList
        restPhieuNhapXuatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phieuNhapXuat.getId())))
            .andExpect(jsonPath("$.[*].maPhieu").value(hasItem(DEFAULT_MA_PHIEU)))
            .andExpect(jsonPath("$.[*].ngayLapPhieu").value(hasItem(DEFAULT_NGAY_LAP_PHIEU.toString())))
            .andExpect(jsonPath("$.[*].loaiPhieu").value(hasItem(DEFAULT_LOAI_PHIEU.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    void getPhieuNhapXuat() throws Exception {
        // Initialize the database
        insertedPhieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);

        // Get the phieuNhapXuat
        restPhieuNhapXuatMockMvc
            .perform(get(ENTITY_API_URL_ID, phieuNhapXuat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phieuNhapXuat.getId()))
            .andExpect(jsonPath("$.maPhieu").value(DEFAULT_MA_PHIEU))
            .andExpect(jsonPath("$.ngayLapPhieu").value(DEFAULT_NGAY_LAP_PHIEU.toString()))
            .andExpect(jsonPath("$.loaiPhieu").value(DEFAULT_LOAI_PHIEU.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    void getNonExistingPhieuNhapXuat() throws Exception {
        // Get the phieuNhapXuat
        restPhieuNhapXuatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPhieuNhapXuat() throws Exception {
        // Initialize the database
        insertedPhieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phieuNhapXuat
        PhieuNhapXuat updatedPhieuNhapXuat = phieuNhapXuatRepository.findById(phieuNhapXuat.getId()).orElseThrow();
        updatedPhieuNhapXuat
            .maPhieu(UPDATED_MA_PHIEU)
            .ngayLapPhieu(UPDATED_NGAY_LAP_PHIEU)
            .loaiPhieu(UPDATED_LOAI_PHIEU)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(updatedPhieuNhapXuat);

        restPhieuNhapXuatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phieuNhapXuatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phieuNhapXuatDTO))
            )
            .andExpect(status().isOk());

        // Validate the PhieuNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPhieuNhapXuatToMatchAllProperties(updatedPhieuNhapXuat);
    }

    @Test
    void putNonExistingPhieuNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phieuNhapXuat.setId(UUID.randomUUID().toString());

        // Create the PhieuNhapXuat
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(phieuNhapXuat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhieuNhapXuatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phieuNhapXuatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phieuNhapXuatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhieuNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPhieuNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phieuNhapXuat.setId(UUID.randomUUID().toString());

        // Create the PhieuNhapXuat
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(phieuNhapXuat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhieuNhapXuatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phieuNhapXuatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhieuNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPhieuNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phieuNhapXuat.setId(UUID.randomUUID().toString());

        // Create the PhieuNhapXuat
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(phieuNhapXuat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhieuNhapXuatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phieuNhapXuatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhieuNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePhieuNhapXuatWithPatch() throws Exception {
        // Initialize the database
        insertedPhieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phieuNhapXuat using partial update
        PhieuNhapXuat partialUpdatedPhieuNhapXuat = new PhieuNhapXuat();
        partialUpdatedPhieuNhapXuat.setId(phieuNhapXuat.getId());

        partialUpdatedPhieuNhapXuat
            .maPhieu(UPDATED_MA_PHIEU)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restPhieuNhapXuatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhieuNhapXuat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhieuNhapXuat))
            )
            .andExpect(status().isOk());

        // Validate the PhieuNhapXuat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhieuNhapXuatUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPhieuNhapXuat, phieuNhapXuat),
            getPersistedPhieuNhapXuat(phieuNhapXuat)
        );
    }

    @Test
    void fullUpdatePhieuNhapXuatWithPatch() throws Exception {
        // Initialize the database
        insertedPhieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phieuNhapXuat using partial update
        PhieuNhapXuat partialUpdatedPhieuNhapXuat = new PhieuNhapXuat();
        partialUpdatedPhieuNhapXuat.setId(phieuNhapXuat.getId());

        partialUpdatedPhieuNhapXuat
            .maPhieu(UPDATED_MA_PHIEU)
            .ngayLapPhieu(UPDATED_NGAY_LAP_PHIEU)
            .loaiPhieu(UPDATED_LOAI_PHIEU)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restPhieuNhapXuatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhieuNhapXuat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhieuNhapXuat))
            )
            .andExpect(status().isOk());

        // Validate the PhieuNhapXuat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhieuNhapXuatUpdatableFieldsEquals(partialUpdatedPhieuNhapXuat, getPersistedPhieuNhapXuat(partialUpdatedPhieuNhapXuat));
    }

    @Test
    void patchNonExistingPhieuNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phieuNhapXuat.setId(UUID.randomUUID().toString());

        // Create the PhieuNhapXuat
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(phieuNhapXuat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhieuNhapXuatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phieuNhapXuatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(phieuNhapXuatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhieuNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPhieuNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phieuNhapXuat.setId(UUID.randomUUID().toString());

        // Create the PhieuNhapXuat
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(phieuNhapXuat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhieuNhapXuatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(phieuNhapXuatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhieuNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPhieuNhapXuat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phieuNhapXuat.setId(UUID.randomUUID().toString());

        // Create the PhieuNhapXuat
        PhieuNhapXuatDTO phieuNhapXuatDTO = phieuNhapXuatMapper.toDto(phieuNhapXuat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhieuNhapXuatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(phieuNhapXuatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhieuNhapXuat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePhieuNhapXuat() throws Exception {
        // Initialize the database
        insertedPhieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the phieuNhapXuat
        restPhieuNhapXuatMockMvc
            .perform(delete(ENTITY_API_URL_ID, phieuNhapXuat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return phieuNhapXuatRepository.count();
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

    protected PhieuNhapXuat getPersistedPhieuNhapXuat(PhieuNhapXuat phieuNhapXuat) {
        return phieuNhapXuatRepository.findById(phieuNhapXuat.getId()).orElseThrow();
    }

    protected void assertPersistedPhieuNhapXuatToMatchAllProperties(PhieuNhapXuat expectedPhieuNhapXuat) {
        assertPhieuNhapXuatAllPropertiesEquals(expectedPhieuNhapXuat, getPersistedPhieuNhapXuat(expectedPhieuNhapXuat));
    }

    protected void assertPersistedPhieuNhapXuatToMatchUpdatableProperties(PhieuNhapXuat expectedPhieuNhapXuat) {
        assertPhieuNhapXuatAllUpdatablePropertiesEquals(expectedPhieuNhapXuat, getPersistedPhieuNhapXuat(expectedPhieuNhapXuat));
    }
}
