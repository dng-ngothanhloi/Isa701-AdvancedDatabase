package dtu.k30.msc.whm.domain;

import java.util.UUID;

public class KhachHangTestSamples {

    public static KhachHang getKhachHangSample1() {
        return new KhachHang().id("id1").maKH("maKH1").tenKH("tenKH1").diaChi("diaChi1").createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static KhachHang getKhachHangSample2() {
        return new KhachHang().id("id2").maKH("maKH2").tenKH("tenKH2").diaChi("diaChi2").createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static KhachHang getKhachHangRandomSampleGenerator() {
        return new KhachHang()
                .id(UUID.randomUUID().toString())
                .maKH(UUID.randomUUID().toString())
                .tenKH(UUID.randomUUID().toString())
                .diaChi(UUID.randomUUID().toString())
                .createdBy(UUID.randomUUID().toString())
                .updatedBy(UUID.randomUUID().toString());
    }
}
