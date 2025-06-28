package dtu.k30.msc.whm.domain;

import static dtu.k30.msc.whm.domain.KhachHangTestSamples.*;
import static dtu.k30.msc.whm.domain.PhieuNhapXuatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhieuNhapXuatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhieuNhapXuat.class);
        PhieuNhapXuat phieuNhapXuat1 = getPhieuNhapXuatSample1();
        PhieuNhapXuat phieuNhapXuat2 = new PhieuNhapXuat();
        assertThat(phieuNhapXuat1).isNotEqualTo(phieuNhapXuat2);

        phieuNhapXuat2.setId(phieuNhapXuat1.getId());
        assertThat(phieuNhapXuat1).isEqualTo(phieuNhapXuat2);

        phieuNhapXuat2 = getPhieuNhapXuatSample2();
        assertThat(phieuNhapXuat1).isNotEqualTo(phieuNhapXuat2);
    }

    @Test
    void khachHangTest() {
        PhieuNhapXuat phieuNhapXuat = getPhieuNhapXuatRandomSampleGenerator();
        KhachHang khachHangBack = getKhachHangRandomSampleGenerator();
        
        // Convert KhachHang to KhachHangEmbedded with selective embedding
        KhachHangEmbedded khachHangEmbedded = new KhachHangEmbedded();
        khachHangEmbedded.setId(khachHangBack.getId());
        khachHangEmbedded.setMaKH(khachHangBack.getMaKH());
        khachHangEmbedded.setTenKH(khachHangBack.getTenKH());
        // Removed: goiTinh, dateOfBirth, diaChi, audit fields for selective embedding

        phieuNhapXuat.setKhachHang(khachHangEmbedded);
        assertThat(phieuNhapXuat.getKhachHang()).isEqualTo(khachHangEmbedded);

        phieuNhapXuat.khachHang(null);
        assertThat(phieuNhapXuat.getKhachHang()).isNull();
    }
}
