package dtu.k30.msc.whm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhieuNhapXuatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhieuNhapXuatDTO.class);
        PhieuNhapXuatDTO phieuNhapXuatDTO1 = new PhieuNhapXuatDTO();
        phieuNhapXuatDTO1.setId("id1");
        PhieuNhapXuatDTO phieuNhapXuatDTO2 = new PhieuNhapXuatDTO();
        assertThat(phieuNhapXuatDTO1).isNotEqualTo(phieuNhapXuatDTO2);
        phieuNhapXuatDTO2.setId(phieuNhapXuatDTO1.getId());
        assertThat(phieuNhapXuatDTO1).isEqualTo(phieuNhapXuatDTO2);
        phieuNhapXuatDTO2.setId("id2");
        assertThat(phieuNhapXuatDTO1).isNotEqualTo(phieuNhapXuatDTO2);
        phieuNhapXuatDTO1.setId(null);
        assertThat(phieuNhapXuatDTO1).isNotEqualTo(phieuNhapXuatDTO2);
    }
}
