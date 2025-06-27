package dtu.k30.msc.whm.web.rest;

import static dtu.k30.msc.whm.domain.DanhMucHangAsserts.*;
import static dtu.k30.msc.whm.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtu.k30.msc.whm.IntegrationTest;
import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.repository.DanhMucHangRepository;
import dtu.k30.msc.whm.service.dto.DanhMucHangDTO;
import dtu.k30.msc.whm.service.mapper.DanhMucHangMapper;
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
 * Integration tests for the {@link DanhMucHangResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DanhMucHangResourceIT {

    private static final String DEFAULT_MA_HANG = "AAAAAAAAAA";
    private static final String UPDATED_MA_HANG = "BBBBBBBBBB";

    private static final String DEFAULT_TEN_HANG = "AAAAAAAAAA";
    private static final String UPDATED_TEN_HANG = "BBBBBBBBBB";

    private static final String DEFAULT_DON_VITINH = "AAAAAAAAAA";
    private static final String UPDATED_DON_VITINH = "BBBBBBBBBB";

    private static final String DEFAULT_NOI_SAN_XUAT = "AAAAAAAAAA";
    private static final String UPDATED_NOI_SAN_XUAT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NGAY_SAN_XUAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NGAY_SAN_XUAT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_HAN_SU_DUNG = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HAN_SU_DUNG = LocalDate.now(ZoneId.systemDefault());

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

    private static final String ENTITY_API_URL = "/api/danh-muc-hangs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DanhMucHangRepository danhMucHangRepository;

    @Autowired
    private DanhMucHangMapper danhMucHangMapper;

    @Autowired
    private MockMvc restDanhMucHangMockMvc;

    private DanhMucHang danhMucHang;

    private DanhMucHang insertedDanhMucHang;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DanhMucHang createEntity() {
        return new DanhMucHang()
            .maHang(DEFAULT_MA_HANG)
            .tenHang(DEFAULT_TEN_HANG)
            .donVitinh(DEFAULT_DON_VITINH)
            .noiSanXuat(DEFAULT_NOI_SAN_XUAT)
            .ngaySanXuat(DEFAULT_NGAY_SAN_XUAT)
            .hanSuDung(DEFAULT_HAN_SU_DUNG)
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
    public static DanhMucHang createUpdatedEntity() {
        return new DanhMucHang()
            .maHang(UPDATED_MA_HANG)
            .tenHang(UPDATED_TEN_HANG)
            .donVitinh(UPDATED_DON_VITINH)
            .noiSanXuat(UPDATED_NOI_SAN_XUAT)
            .ngaySanXuat(UPDATED_NGAY_SAN_XUAT)
            .hanSuDung(UPDATED_HAN_SU_DUNG)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    void initTest() {
        danhMucHang = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDanhMucHang != null) {
            danhMucHangRepository.delete(insertedDanhMucHang);
            insertedDanhMucHang = null;
        }
    }

    @Test
    void createDanhMucHang() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DanhMucHang
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);
        var returnedDanhMucHangDTO = om.readValue(
            restDanhMucHangMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhMucHangDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DanhMucHangDTO.class
        );

        // Validate the DanhMucHang in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDanhMucHang = danhMucHangMapper.toEntity(returnedDanhMucHangDTO);
        assertDanhMucHangUpdatableFieldsEquals(returnedDanhMucHang, getPersistedDanhMucHang(returnedDanhMucHang));

        insertedDanhMucHang = returnedDanhMucHang;
    }

    @Test
    void createDanhMucHangWithExistingId() throws Exception {
        // Create the DanhMucHang with an existing ID
        danhMucHang.setId("existing_id");
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDanhMucHangMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhMucHangDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DanhMucHang in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTenHangIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        danhMucHang.setTenHang(null);

        // Create the DanhMucHang, which fails.
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        restDanhMucHangMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhMucHangDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNoiSanXuatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        danhMucHang.setNoiSanXuat(null);

        // Create the DanhMucHang, which fails.
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        restDanhMucHangMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhMucHangDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllDanhMucHangs() throws Exception {
        // Initialize the database
        insertedDanhMucHang = danhMucHangRepository.save(danhMucHang);

        // Get all the danhMucHangList
        restDanhMucHangMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(danhMucHang.getId())))
            .andExpect(jsonPath("$.[*].maHang").value(hasItem(DEFAULT_MA_HANG)))
            .andExpect(jsonPath("$.[*].tenHang").value(hasItem(DEFAULT_TEN_HANG)))
            .andExpect(jsonPath("$.[*].donVitinh").value(hasItem(DEFAULT_DON_VITINH)))
            .andExpect(jsonPath("$.[*].noiSanXuat").value(hasItem(DEFAULT_NOI_SAN_XUAT)))
            .andExpect(jsonPath("$.[*].ngaySanXuat").value(hasItem(DEFAULT_NGAY_SAN_XUAT.toString())))
            .andExpect(jsonPath("$.[*].hanSuDung").value(hasItem(DEFAULT_HAN_SU_DUNG.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    void getDanhMucHang() throws Exception {
        // Initialize the database
        insertedDanhMucHang = danhMucHangRepository.save(danhMucHang);

        // Get the danhMucHang
        restDanhMucHangMockMvc
            .perform(get(ENTITY_API_URL_ID, danhMucHang.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(danhMucHang.getId()))
            .andExpect(jsonPath("$.maHang").value(DEFAULT_MA_HANG))
            .andExpect(jsonPath("$.tenHang").value(DEFAULT_TEN_HANG))
            .andExpect(jsonPath("$.donVitinh").value(DEFAULT_DON_VITINH))
            .andExpect(jsonPath("$.noiSanXuat").value(DEFAULT_NOI_SAN_XUAT))
            .andExpect(jsonPath("$.ngaySanXuat").value(DEFAULT_NGAY_SAN_XUAT.toString()))
            .andExpect(jsonPath("$.hanSuDung").value(DEFAULT_HAN_SU_DUNG.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    void getNonExistingDanhMucHang() throws Exception {
        // Get the danhMucHang
        restDanhMucHangMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingDanhMucHang() throws Exception {
        // Initialize the database
        insertedDanhMucHang = danhMucHangRepository.save(danhMucHang);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhMucHang
        DanhMucHang updatedDanhMucHang = danhMucHangRepository.findById(danhMucHang.getId()).orElseThrow();
        updatedDanhMucHang
            .maHang(UPDATED_MA_HANG)
            .tenHang(UPDATED_TEN_HANG)
            .donVitinh(UPDATED_DON_VITINH)
            .noiSanXuat(UPDATED_NOI_SAN_XUAT)
            .ngaySanXuat(UPDATED_NGAY_SAN_XUAT)
            .hanSuDung(UPDATED_HAN_SU_DUNG)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(updatedDanhMucHang);

        restDanhMucHangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, danhMucHangDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(danhMucHangDTO))
            )
            .andExpect(status().isOk());

        // Validate the DanhMucHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDanhMucHangToMatchAllProperties(updatedDanhMucHang);
    }

    @Test
    void putNonExistingDanhMucHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhMucHang.setId(UUID.randomUUID().toString());

        // Create the DanhMucHang
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDanhMucHangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, danhMucHangDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(danhMucHangDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhMucHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDanhMucHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhMucHang.setId(UUID.randomUUID().toString());

        // Create the DanhMucHang
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhMucHangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(danhMucHangDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhMucHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDanhMucHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhMucHang.setId(UUID.randomUUID().toString());

        // Create the DanhMucHang
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhMucHangMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhMucHangDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DanhMucHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDanhMucHangWithPatch() throws Exception {
        // Initialize the database
        insertedDanhMucHang = danhMucHangRepository.save(danhMucHang);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhMucHang using partial update
        DanhMucHang partialUpdatedDanhMucHang = new DanhMucHang();
        partialUpdatedDanhMucHang.setId(danhMucHang.getId());

        partialUpdatedDanhMucHang.maHang(UPDATED_MA_HANG).ngaySanXuat(UPDATED_NGAY_SAN_XUAT).updatedAt(UPDATED_UPDATED_AT);

        restDanhMucHangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDanhMucHang.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDanhMucHang))
            )
            .andExpect(status().isOk());

        // Validate the DanhMucHang in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDanhMucHangUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDanhMucHang, danhMucHang),
            getPersistedDanhMucHang(danhMucHang)
        );
    }

    @Test
    void fullUpdateDanhMucHangWithPatch() throws Exception {
        // Initialize the database
        insertedDanhMucHang = danhMucHangRepository.save(danhMucHang);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhMucHang using partial update
        DanhMucHang partialUpdatedDanhMucHang = new DanhMucHang();
        partialUpdatedDanhMucHang.setId(danhMucHang.getId());

        partialUpdatedDanhMucHang
            .maHang(UPDATED_MA_HANG)
            .tenHang(UPDATED_TEN_HANG)
            .donVitinh(UPDATED_DON_VITINH)
            .noiSanXuat(UPDATED_NOI_SAN_XUAT)
            .ngaySanXuat(UPDATED_NGAY_SAN_XUAT)
            .hanSuDung(UPDATED_HAN_SU_DUNG)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restDanhMucHangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDanhMucHang.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDanhMucHang))
            )
            .andExpect(status().isOk());

        // Validate the DanhMucHang in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDanhMucHangUpdatableFieldsEquals(partialUpdatedDanhMucHang, getPersistedDanhMucHang(partialUpdatedDanhMucHang));
    }

    @Test
    void patchNonExistingDanhMucHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhMucHang.setId(UUID.randomUUID().toString());

        // Create the DanhMucHang
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDanhMucHangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, danhMucHangDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(danhMucHangDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhMucHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDanhMucHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhMucHang.setId(UUID.randomUUID().toString());

        // Create the DanhMucHang
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhMucHangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(danhMucHangDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhMucHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDanhMucHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhMucHang.setId(UUID.randomUUID().toString());

        // Create the DanhMucHang
        DanhMucHangDTO danhMucHangDTO = danhMucHangMapper.toDto(danhMucHang);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhMucHangMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(danhMucHangDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DanhMucHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDanhMucHang() throws Exception {
        // Initialize the database
        insertedDanhMucHang = danhMucHangRepository.save(danhMucHang);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the danhMucHang
        restDanhMucHangMockMvc
            .perform(delete(ENTITY_API_URL_ID, danhMucHang.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return danhMucHangRepository.count();
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

    protected DanhMucHang getPersistedDanhMucHang(DanhMucHang danhMucHang) {
        return danhMucHangRepository.findById(danhMucHang.getId()).orElseThrow();
    }

    protected void assertPersistedDanhMucHangToMatchAllProperties(DanhMucHang expectedDanhMucHang) {
        assertDanhMucHangAllPropertiesEquals(expectedDanhMucHang, getPersistedDanhMucHang(expectedDanhMucHang));
    }

    protected void assertPersistedDanhMucHangToMatchUpdatableProperties(DanhMucHang expectedDanhMucHang) {
        assertDanhMucHangAllUpdatablePropertiesEquals(expectedDanhMucHang, getPersistedDanhMucHang(expectedDanhMucHang));
    }
}
