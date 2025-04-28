package dtu.k30.msc.whm.domain;

import static dtu.k30.msc.whm.domain.MasterdataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterdataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Masterdata.class);
        Masterdata masterdata1 = getMasterdataSample1();
        Masterdata masterdata2 = new Masterdata();
        assertThat(masterdata1).isNotEqualTo(masterdata2);

        masterdata2.setId(masterdata1.getId());
        assertThat(masterdata1).isEqualTo(masterdata2);

        masterdata2 = getMasterdataSample2();
        assertThat(masterdata1).isNotEqualTo(masterdata2);
    }
}
