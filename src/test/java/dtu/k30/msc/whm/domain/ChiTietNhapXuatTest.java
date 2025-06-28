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
        
        // Convert PhieuNhapXuat to PhieuNhapXuatEmbedded with selective embedding
        PhieuNhapXuatEmbedded phieuNhapXuatEmbedded = new PhieuNhapXuatEmbedded();
        phieuNhapXuatEmbedded.setId(phieuNhapXuatBack.getId());
        phieuNhapXuatEmbedded.setMaPhieu(phieuNhapXuatBack.getMaPhieu());
        phieuNhapXuatEmbedded.setNgayLapPhieu(phieuNhapXuatBack.getNgayLapPhieu());
        phieuNhapXuatEmbedded.setLoaiPhieu(phieuNhapXuatBack.getLoaiPhieu());
        // Removed: audit fields for selective embedding

        chiTietNhapXuat.setPhieuNhapXuat(phieuNhapXuatEmbedded);
        assertThat(chiTietNhapXuat.getPhieuNhapXuat()).isEqualTo(phieuNhapXuatEmbedded);

        chiTietNhapXuat.phieuNhapXuat(null);
        assertThat(chiTietNhapXuat.getPhieuNhapXuat()).isNull();
    }

    @Test
    void maHangTest() {
        ChiTietNhapXuat chiTietNhapXuat = getChiTietNhapXuatRandomSampleGenerator();
        DanhMucHang danhMucHangBack = getDanhMucHangRandomSampleGenerator();
        
        // Convert DanhMucHang to DanhMucHangEmbedded with selective embedding
        DanhMucHangEmbedded danhMucHangEmbedded = new DanhMucHangEmbedded();
        danhMucHangEmbedded.setId(danhMucHangBack.getId());
        danhMucHangEmbedded.setMaHang(danhMucHangBack.getMaHang());
        danhMucHangEmbedded.setTenHang(danhMucHangBack.getTenHang());
        danhMucHangEmbedded.setDonviTinh(danhMucHangBack.getDonviTinh());
        // Removed: noiSanXuat, ngaySanXuat, hanSuDung, audit fields for selective embedding

        chiTietNhapXuat.setMaHang(danhMucHangEmbedded);
        assertThat(chiTietNhapXuat.getMaHang()).isEqualTo(danhMucHangEmbedded);

        chiTietNhapXuat.maHang(null);
        assertThat(chiTietNhapXuat.getMaHang()).isNull();
    }
}
