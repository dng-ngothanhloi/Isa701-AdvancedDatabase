package dtu.k30.msc.whm.web.rest;

import static dtu.k30.msc.whm.domain.KhachHangAsserts.*;
import static dtu.k30.msc.whm.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtu.k30.msc.whm.IntegrationTest;
import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.domain.enumeration.EmpSex;
import dtu.k30.msc.whm.repository.KhachHangRepository;
import dtu.k30.msc.whm.service.dto.KhachHangDTO;
import dtu.k30.msc.whm.service.mapper.KhachHangMapper;
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
 * Integration tests for the {@link KhachHangResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KhachHangResourceIT {

    private static final String DEFAULT_MA_KH = "AAAAAAAAAA";
    private static final String UPDATED_MA_KH = "BBBBBBBBBB";

    private static final String DEFAULT_TEN_KH = "AAAAAAAAAA";
    private static final String UPDATED_TEN_KH = "BBBBBBBBBB";

    private static final EmpSex DEFAULT_GOI_TINH = EmpSex.Nam;
    private static final EmpSex UPDATED_GOI_TINH = EmpSex.Nu;

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DIA_CHI = "AAAAAAAAAA";
    private static final String UPDATED_DIA_CHI = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/khach-hangs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private KhachHangMapper khachHangMapper;

    @Autowired
    private MockMvc restKhachHangMockMvc;

    private KhachHang khachHang;

    private KhachHang insertedKhachHang;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KhachHang createEntity() {
        return new KhachHang()
            .maKH(DEFAULT_MA_KH)
            .tenKH(DEFAULT_TEN_KH)
            .goiTinh(DEFAULT_GOI_TINH)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .diaChi(DEFAULT_DIA_CHI)
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
    public static KhachHang createUpdatedEntity() {
        return new KhachHang()
            .maKH(UPDATED_MA_KH)
            .tenKH(UPDATED_TEN_KH)
            .goiTinh(UPDATED_GOI_TINH)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .diaChi(UPDATED_DIA_CHI)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    void initTest() {
        khachHang = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKhachHang != null) {
            khachHangRepository.delete(insertedKhachHang);
            insertedKhachHang = null;
        }
    }

    @Test
    void createKhachHang() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KhachHang
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);
        var returnedKhachHangDTO = om.readValue(
            restKhachHangMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(khachHangDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KhachHangDTO.class
        );

        // Validate the KhachHang in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedKhachHang = khachHangMapper.toEntity(returnedKhachHangDTO);
        assertKhachHangUpdatableFieldsEquals(returnedKhachHang, getPersistedKhachHang(returnedKhachHang));

        insertedKhachHang = returnedKhachHang;
    }

    @Test
    void createKhachHangWithExistingId() throws Exception {
        // Create the KhachHang with an existing ID
        khachHang.setId("existing_id");
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKhachHangMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(khachHangDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KhachHang in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTenKHIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        khachHang.setTenKH(null);

        // Create the KhachHang, which fails.
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);

        restKhachHangMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(khachHangDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllKhachHangs() throws Exception {
        // Initialize the database
        insertedKhachHang = khachHangRepository.save(khachHang);

        // Get all the khachHangList
        restKhachHangMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(khachHang.getId())))
            .andExpect(jsonPath("$.[*].maKH").value(hasItem(DEFAULT_MA_KH)))
            .andExpect(jsonPath("$.[*].tenKH").value(hasItem(DEFAULT_TEN_KH)))
            .andExpect(jsonPath("$.[*].goiTinh").value(hasItem(DEFAULT_GOI_TINH.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].diaChi").value(hasItem(DEFAULT_DIA_CHI)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    void getKhachHang() throws Exception {
        // Initialize the database
        insertedKhachHang = khachHangRepository.save(khachHang);

        // Get the khachHang
        restKhachHangMockMvc
            .perform(get(ENTITY_API_URL_ID, khachHang.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(khachHang.getId()))
            .andExpect(jsonPath("$.maKH").value(DEFAULT_MA_KH))
            .andExpect(jsonPath("$.tenKH").value(DEFAULT_TEN_KH))
            .andExpect(jsonPath("$.goiTinh").value(DEFAULT_GOI_TINH.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.diaChi").value(DEFAULT_DIA_CHI))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    void getNonExistingKhachHang() throws Exception {
        // Get the khachHang
        restKhachHangMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingKhachHang() throws Exception {
        // Initialize the database
        insertedKhachHang = khachHangRepository.save(khachHang);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the khachHang
        KhachHang updatedKhachHang = khachHangRepository.findById(khachHang.getId()).orElseThrow();
        updatedKhachHang
            .maKH(UPDATED_MA_KH)
            .tenKH(UPDATED_TEN_KH)
            .goiTinh(UPDATED_GOI_TINH)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .diaChi(UPDATED_DIA_CHI)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(updatedKhachHang);

        restKhachHangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, khachHangDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(khachHangDTO))
            )
            .andExpect(status().isOk());

        // Validate the KhachHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKhachHangToMatchAllProperties(updatedKhachHang);
    }

    @Test
    void putNonExistingKhachHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        khachHang.setId(UUID.randomUUID().toString());

        // Create the KhachHang
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKhachHangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, khachHangDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(khachHangDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KhachHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchKhachHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        khachHang.setId(UUID.randomUUID().toString());

        // Create the KhachHang
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKhachHangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(khachHangDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KhachHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamKhachHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        khachHang.setId(UUID.randomUUID().toString());

        // Create the KhachHang
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKhachHangMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(khachHangDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KhachHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateKhachHangWithPatch() throws Exception {
        // Initialize the database
        insertedKhachHang = khachHangRepository.save(khachHang);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the khachHang using partial update
        KhachHang partialUpdatedKhachHang = new KhachHang();
        partialUpdatedKhachHang.setId(khachHang.getId());

        partialUpdatedKhachHang
            .maKH(UPDATED_MA_KH)
            .tenKH(UPDATED_TEN_KH)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restKhachHangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKhachHang.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKhachHang))
            )
            .andExpect(status().isOk());

        // Validate the KhachHang in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKhachHangUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKhachHang, khachHang),
            getPersistedKhachHang(khachHang)
        );
    }

    @Test
    void fullUpdateKhachHangWithPatch() throws Exception {
        // Initialize the database
        insertedKhachHang = khachHangRepository.save(khachHang);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the khachHang using partial update
        KhachHang partialUpdatedKhachHang = new KhachHang();
        partialUpdatedKhachHang.setId(khachHang.getId());

        partialUpdatedKhachHang
            .maKH(UPDATED_MA_KH)
            .tenKH(UPDATED_TEN_KH)
            .goiTinh(UPDATED_GOI_TINH)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .diaChi(UPDATED_DIA_CHI)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restKhachHangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKhachHang.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKhachHang))
            )
            .andExpect(status().isOk());

        // Validate the KhachHang in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKhachHangUpdatableFieldsEquals(partialUpdatedKhachHang, getPersistedKhachHang(partialUpdatedKhachHang));
    }

    @Test
    void patchNonExistingKhachHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        khachHang.setId(UUID.randomUUID().toString());

        // Create the KhachHang
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKhachHangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, khachHangDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(khachHangDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KhachHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchKhachHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        khachHang.setId(UUID.randomUUID().toString());

        // Create the KhachHang
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKhachHangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(khachHangDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KhachHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamKhachHang() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        khachHang.setId(UUID.randomUUID().toString());

        // Create the KhachHang
        KhachHangDTO khachHangDTO = khachHangMapper.toDto(khachHang);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKhachHangMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(khachHangDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KhachHang in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteKhachHang() throws Exception {
        // Initialize the database
        insertedKhachHang = khachHangRepository.save(khachHang);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the khachHang
        restKhachHangMockMvc
            .perform(delete(ENTITY_API_URL_ID, khachHang.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return khachHangRepository.count();
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

    protected KhachHang getPersistedKhachHang(KhachHang khachHang) {
        return khachHangRepository.findById(khachHang.getId()).orElseThrow();
    }

    protected void assertPersistedKhachHangToMatchAllProperties(KhachHang expectedKhachHang) {
        assertKhachHangAllPropertiesEquals(expectedKhachHang, getPersistedKhachHang(expectedKhachHang));
    }

    protected void assertPersistedKhachHangToMatchUpdatableProperties(KhachHang expectedKhachHang) {
        assertKhachHangAllUpdatablePropertiesEquals(expectedKhachHang, getPersistedKhachHang(expectedKhachHang));
    }
}
