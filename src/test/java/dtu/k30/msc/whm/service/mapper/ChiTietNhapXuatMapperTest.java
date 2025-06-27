package dtu.k30.msc.whm.service.mapper;

import static dtu.k30.msc.whm.domain.ChiTietNhapXuatAsserts.*;
import static dtu.k30.msc.whm.domain.ChiTietNhapXuatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChiTietNhapXuatMapperTest {

    private ChiTietNhapXuatMapper chiTietNhapXuatMapper;

    @BeforeEach
    void setUp() {
        chiTietNhapXuatMapper = new ChiTietNhapXuatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChiTietNhapXuatSample1();
        var actual = chiTietNhapXuatMapper.toEntity(chiTietNhapXuatMapper.toDto(expected));
        assertChiTietNhapXuatAllPropertiesEquals(expected, actual);
    }
}
