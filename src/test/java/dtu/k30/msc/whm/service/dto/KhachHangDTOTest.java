package dtu.k30.msc.whm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KhachHangDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KhachHangDTO.class);
        KhachHangDTO khachHangDTO1 = new KhachHangDTO();
        khachHangDTO1.setId("id1");
        KhachHangDTO khachHangDTO2 = new KhachHangDTO();
        assertThat(khachHangDTO1).isNotEqualTo(khachHangDTO2);
        khachHangDTO2.setId(khachHangDTO1.getId());
        assertThat(khachHangDTO1).isEqualTo(khachHangDTO2);
        khachHangDTO2.setId("id2");
        assertThat(khachHangDTO1).isNotEqualTo(khachHangDTO2);
        khachHangDTO1.setId(null);
        assertThat(khachHangDTO1).isNotEqualTo(khachHangDTO2);
    }
}
