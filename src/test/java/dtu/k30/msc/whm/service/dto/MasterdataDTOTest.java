package dtu.k30.msc.whm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dtu.k30.msc.whm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterdataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterdataDTO.class);
        MasterdataDTO masterdataDTO1 = new MasterdataDTO();
        masterdataDTO1.setId("id1");
        MasterdataDTO masterdataDTO2 = new MasterdataDTO();
        assertThat(masterdataDTO1).isNotEqualTo(masterdataDTO2);
        masterdataDTO2.setId(masterdataDTO1.getId());
        assertThat(masterdataDTO1).isEqualTo(masterdataDTO2);
        masterdataDTO2.setId("id2");
        assertThat(masterdataDTO1).isNotEqualTo(masterdataDTO2);
        masterdataDTO1.setId(null);
        assertThat(masterdataDTO1).isNotEqualTo(masterdataDTO2);
    }
}
