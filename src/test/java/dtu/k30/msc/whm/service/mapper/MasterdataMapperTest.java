package dtu.k30.msc.whm.service.mapper;

import static dtu.k30.msc.whm.domain.MasterdataAsserts.*;
import static dtu.k30.msc.whm.domain.MasterdataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MasterdataMapperTest {

    private MasterdataMapper masterdataMapper;

    @BeforeEach
    void setUp() {
        masterdataMapper = new MasterdataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMasterdataSample1();
        var actual = masterdataMapper.toEntity(masterdataMapper.toDto(expected));
        assertMasterdataAllPropertiesEquals(expected, actual);
    }
}
