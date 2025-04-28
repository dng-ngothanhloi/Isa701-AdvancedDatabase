package dtu.k30.msc.whm.domain;

import java.util.UUID;

public class PhieuNhapXuatTestSamples {

    public static PhieuNhapXuat getPhieuNhapXuatSample1() {
        return new PhieuNhapXuat().id("id1").maPhieu("maPhieu1").createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static PhieuNhapXuat getPhieuNhapXuatSample2() {
        return new PhieuNhapXuat().id("id2").maPhieu("maPhieu2").createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static PhieuNhapXuat getPhieuNhapXuatRandomSampleGenerator() {
        return new PhieuNhapXuat()
                .id(UUID.randomUUID().toString())
                .maPhieu(UUID.randomUUID().toString())
                .createdBy(UUID.randomUUID().toString())
                .updatedBy(UUID.randomUUID().toString());
    }
}
