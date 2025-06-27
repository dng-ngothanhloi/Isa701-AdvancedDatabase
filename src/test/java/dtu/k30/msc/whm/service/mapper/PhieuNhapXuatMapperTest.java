package dtu.k30.msc.whm.service.mapper;

import static dtu.k30.msc.whm.domain.PhieuNhapXuatAsserts.*;
import static dtu.k30.msc.whm.domain.PhieuNhapXuatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhieuNhapXuatMapperTest {

    private PhieuNhapXuatMapper phieuNhapXuatMapper;

    @BeforeEach
    void setUp() {
        phieuNhapXuatMapper = new PhieuNhapXuatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPhieuNhapXuatSample1();
        var actual = phieuNhapXuatMapper.toEntity(phieuNhapXuatMapper.toDto(expected));
        assertPhieuNhapXuatAllPropertiesEquals(expected, actual);
    }
}
