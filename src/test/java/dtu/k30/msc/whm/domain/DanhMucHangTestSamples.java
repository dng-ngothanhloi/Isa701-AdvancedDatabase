package dtu.k30.msc.whm.domain;

import java.util.UUID;

public class DanhMucHangTestSamples {

    public static DanhMucHang getDanhMucHangSample1() {
        return new DanhMucHang()
            .id("id1")
            .maHang("maHang1")
            .tenHang("tenHang1")
            .donviTinh("donviTinh1")
            .noiSanXuat("noiSanXuat1")
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static DanhMucHang getDanhMucHangSample2() {
        return new DanhMucHang()
            .id("id2")
            .maHang("maHang2")
            .tenHang("tenHang2")
            .donviTinh("donviTinh2")
            .noiSanXuat("noiSanXuat2")
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static DanhMucHang getDanhMucHangRandomSampleGenerator() {
        return new DanhMucHang()
            .id(UUID.randomUUID().toString())
            .maHang(UUID.randomUUID().toString())
            .tenHang(UUID.randomUUID().toString())
            .donviTinh(UUID.randomUUID().toString())
            .noiSanXuat(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
