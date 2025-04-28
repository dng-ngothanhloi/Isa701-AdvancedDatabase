package dtu.k30.msc.whm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ChiTietNhapXuatTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ChiTietNhapXuat getChiTietNhapXuatSample1() {
        return new ChiTietNhapXuat().id("id1").soLuong(1).createdBy("createdBy1").updatedBy("updatedBy1");
    }

    public static ChiTietNhapXuat getChiTietNhapXuatSample2() {
        return new ChiTietNhapXuat().id("id2").soLuong(2).createdBy("createdBy2").updatedBy("updatedBy2");
    }

    public static ChiTietNhapXuat getChiTietNhapXuatRandomSampleGenerator() {
        return new ChiTietNhapXuat()
                .id(UUID.randomUUID().toString())
                .soLuong(intCount.incrementAndGet())
                .createdBy(UUID.randomUUID().toString())
                .updatedBy(UUID.randomUUID().toString());
    }
}
