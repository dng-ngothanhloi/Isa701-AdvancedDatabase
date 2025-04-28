package dtu.k30.msc.whm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChiTietNhapXuatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChiTietNhapXuatDTO.class);
        ChiTietNhapXuatDTO chiTietNhapXuatDTO1 = new ChiTietNhapXuatDTO();
        chiTietNhapXuatDTO1.setId("id1");
        ChiTietNhapXuatDTO chiTietNhapXuatDTO2 = new ChiTietNhapXuatDTO();
        assertThat(chiTietNhapXuatDTO1).isNotEqualTo(chiTietNhapXuatDTO2);
        chiTietNhapXuatDTO2.setId(chiTietNhapXuatDTO1.getId());
        assertThat(chiTietNhapXuatDTO1).isEqualTo(chiTietNhapXuatDTO2);
        chiTietNhapXuatDTO2.setId("id2");
        assertThat(chiTietNhapXuatDTO1).isNotEqualTo(chiTietNhapXuatDTO2);
        chiTietNhapXuatDTO1.setId(null);
        assertThat(chiTietNhapXuatDTO1).isNotEqualTo(chiTietNhapXuatDTO2);
    }
}
