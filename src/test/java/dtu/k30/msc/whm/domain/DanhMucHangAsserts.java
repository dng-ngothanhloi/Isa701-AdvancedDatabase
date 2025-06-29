package dtu.k30.msc.whm.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DanhMucHangAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhMucHangAllPropertiesEquals(DanhMucHang expected, DanhMucHang actual) {
        assertDanhMucHangAutoGeneratedPropertiesEquals(expected, actual);
        assertDanhMucHangAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhMucHangAllUpdatablePropertiesEquals(DanhMucHang expected, DanhMucHang actual) {
        assertDanhMucHangUpdatableFieldsEquals(expected, actual);
        assertDanhMucHangUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhMucHangAutoGeneratedPropertiesEquals(DanhMucHang expected, DanhMucHang actual) {
        assertThat(actual)
            .as("Verify DanhMucHang auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhMucHangUpdatableFieldsEquals(DanhMucHang expected, DanhMucHang actual) {
        assertThat(actual)
            .as("Verify DanhMucHang relevant properties")
            .satisfies(a -> assertThat(a.getMaHang()).as("check maHang").isEqualTo(expected.getMaHang()))
            .satisfies(a -> assertThat(a.getTenHang()).as("check tenHang").isEqualTo(expected.getTenHang()))
            .satisfies(a -> assertThat(a.getDonviTinh()).as("check donviTinh").isEqualTo(expected.getDonviTinh()))
            .satisfies(a -> assertThat(a.getNoiSanXuat()).as("check noiSanXuat").isEqualTo(expected.getNoiSanXuat()))
            .satisfies(a -> assertThat(a.getNgaySanXuat()).as("check ngaySanXuat").isEqualTo(expected.getNgaySanXuat()))
            .satisfies(a -> assertThat(a.getHanSuDung()).as("check hanSuDung").isEqualTo(expected.getHanSuDung()))
            .satisfies(a -> assertThat(a.getCreatedAt()).as("check createdAt").isEqualTo(expected.getCreatedAt()))
            .satisfies(a -> assertThat(a.getCreatedBy()).as("check createdBy").isEqualTo(expected.getCreatedBy()))
            .satisfies(a -> assertThat(a.getUpdatedAt()).as("check updatedAt").isEqualTo(expected.getUpdatedAt()))
            .satisfies(a -> assertThat(a.getUpdatedBy()).as("check updatedBy").isEqualTo(expected.getUpdatedBy()))
            .satisfies(a -> assertThat(a.getIsDeleted()).as("check isDeleted").isEqualTo(expected.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhMucHangUpdatableRelationshipsEquals(DanhMucHang expected, DanhMucHang actual) {
        // empty method
    }
}
