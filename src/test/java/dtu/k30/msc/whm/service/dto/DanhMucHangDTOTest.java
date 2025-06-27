package dtu.k30.msc.whm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DanhMucHangDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DanhMucHangDTO.class);
        DanhMucHangDTO danhMucHangDTO1 = new DanhMucHangDTO();
        danhMucHangDTO1.setId("id1");
        DanhMucHangDTO danhMucHangDTO2 = new DanhMucHangDTO();
        assertThat(danhMucHangDTO1).isNotEqualTo(danhMucHangDTO2);
        danhMucHangDTO2.setId(danhMucHangDTO1.getId());
        assertThat(danhMucHangDTO1).isEqualTo(danhMucHangDTO2);
        danhMucHangDTO2.setId("id2");
        assertThat(danhMucHangDTO1).isNotEqualTo(danhMucHangDTO2);
        danhMucHangDTO1.setId(null);
        assertThat(danhMucHangDTO1).isNotEqualTo(danhMucHangDTO2);
    }
}
