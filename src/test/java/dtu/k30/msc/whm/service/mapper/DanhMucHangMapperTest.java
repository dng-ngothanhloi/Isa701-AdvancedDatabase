package dtu.k30.msc.whm.service.mapper;

import static dtu.k30.msc.whm.domain.DanhMucHangAsserts.*;
import static dtu.k30.msc.whm.domain.DanhMucHangTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DanhMucHangMapperTest {

    private DanhMucHangMapper danhMucHangMapper;

    @BeforeEach
    void setUp() {
        danhMucHangMapper = new DanhMucHangMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDanhMucHangSample1();
        var actual = danhMucHangMapper.toEntity(danhMucHangMapper.toDto(expected));
        assertDanhMucHangAllPropertiesEquals(expected, actual);
    }
}
