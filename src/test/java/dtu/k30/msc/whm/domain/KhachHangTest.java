package dtu.k30.msc.whm.domain;

import static dtu.k30.msc.whm.domain.KhachHangTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KhachHangTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KhachHang.class);
        KhachHang khachHang1 = getKhachHangSample1();
        KhachHang khachHang2 = new KhachHang();
        assertThat(khachHang1).isNotEqualTo(khachHang2);

        khachHang2.setId(khachHang1.getId());
        assertThat(khachHang1).isEqualTo(khachHang2);

        khachHang2 = getKhachHangSample2();
        assertThat(khachHang1).isNotEqualTo(khachHang2);
    }
}
