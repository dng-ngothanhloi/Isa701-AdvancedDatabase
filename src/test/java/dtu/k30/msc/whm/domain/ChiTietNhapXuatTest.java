package dtu.k30.msc.whm.domain;

import static dtu.k30.msc.whm.domain.ChiTietNhapXuatTestSamples.*;
import static dtu.k30.msc.whm.domain.DanhMucHangTestSamples.*;
import static dtu.k30.msc.whm.domain.PhieuNhapXuatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChiTietNhapXuatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChiTietNhapXuat.class);
        ChiTietNhapXuat chiTietNhapXuat1 = getChiTietNhapXuatSample1();
        ChiTietNhapXuat chiTietNhapXuat2 = new ChiTietNhapXuat();
        assertThat(chiTietNhapXuat1).isNotEqualTo(chiTietNhapXuat2);

        chiTietNhapXuat2.setId(chiTietNhapXuat1.getId());
        assertThat(chiTietNhapXuat1).isEqualTo(chiTietNhapXuat2);

        chiTietNhapXuat2 = getChiTietNhapXuatSample2();
        assertThat(chiTietNhapXuat1).isNotEqualTo(chiTietNhapXuat2);
    }

    @Test
    void phieuNhapXuatTest() {
        ChiTietNhapXuat chiTietNhapXuat = getChiTietNhapXuatRandomSampleGenerator();
        PhieuNhapXuat phieuNhapXuatBack = getPhieuNhapXuatRandomSampleGenerator();

        chiTietNhapXuat.setPhieuNhapXuat(phieuNhapXuatBack);
        assertThat(chiTietNhapXuat.getPhieuNhapXuat()).isEqualTo(phieuNhapXuatBack);

        chiTietNhapXuat.phieuNhapXuat(null);
        assertThat(chiTietNhapXuat.getPhieuNhapXuat()).isNull();
    }

    @Test
    void maHangTest() {
        ChiTietNhapXuat chiTietNhapXuat = getChiTietNhapXuatRandomSampleGenerator();
        DanhMucHang danhMucHangBack = getDanhMucHangRandomSampleGenerator();

        chiTietNhapXuat.setMaHang(danhMucHangBack);
        assertThat(chiTietNhapXuat.getMaHang()).isEqualTo(danhMucHangBack);

        chiTietNhapXuat.maHang(null);
        assertThat(chiTietNhapXuat.getMaHang()).isNull();
    }
}
