package dtu.k30.msc.whm.domain;

import java.util.UUID;

public class MasterdataTestSamples {

    public static Masterdata getMasterdataSample1() {
        return new Masterdata()
            .id("id1")
            .category("category1")
            .dataKey("dataKey1")
            .dataValue("dataValue1")
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static Masterdata getMasterdataSample2() {
        return new Masterdata()
            .id("id2")
            .category("category2")
            .dataKey("dataKey2")
            .dataValue("dataValue2")
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static Masterdata getMasterdataRandomSampleGenerator() {
        return new Masterdata()
            .id(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .dataKey(UUID.randomUUID().toString())
            .dataValue(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
