package dtu.k30.msc.whm.domain;

import static dtu.k30.msc.whm.domain.DanhMucHangTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DanhMucHangTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DanhMucHang.class);
        DanhMucHang danhMucHang1 = getDanhMucHangSample1();
        DanhMucHang danhMucHang2 = new DanhMucHang();
        assertThat(danhMucHang1).isNotEqualTo(danhMucHang2);

        danhMucHang2.setId(danhMucHang1.getId());
        assertThat(danhMucHang1).isEqualTo(danhMucHang2);

        danhMucHang2 = getDanhMucHangSample2();
        assertThat(danhMucHang1).isNotEqualTo(danhMucHang2);
    }
}
