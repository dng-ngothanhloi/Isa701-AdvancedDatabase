package dtu.k30.msc.whm.service.mapper;

import static dtu.k30.msc.whm.domain.KhachHangAsserts.*;
import static dtu.k30.msc.whm.domain.KhachHangTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KhachHangMapperTest {

    private KhachHangMapper khachHangMapper;

    @BeforeEach
    void setUp() {
        khachHangMapper = new KhachHangMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getKhachHangSample1();
        var actual = khachHangMapper.toEntity(khachHangMapper.toDto(expected));
        assertKhachHangAllPropertiesEquals(expected, actual);
    }
}
