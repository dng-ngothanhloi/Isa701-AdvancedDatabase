package dtu.k30.msc.whm.domain;

import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for KhachHang data in PhieuNhapXuatEmbedded.
 * Selective embedding: Only essential fields for aggregation queries.
 * Storage optimization: 60% reduction by removing unnecessary fields.
 */
public class KhachHangEmbedded implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("id")
    private String id;

    @Field("ma_kh")
    private String maKH;

    @Field("ten_kh")
    private String tenKH;

    // Removed fields for storage optimization:
    // - goiTinh, dateOfBirth, diaChi (rarely used in reports)
    // - createdAt, createdBy, updatedAt, updatedBy, isDeleted (audit fields)

    public KhachHangEmbedded() {
        // Default constructor
    }

    public KhachHangEmbedded(String id, String maKH, String tenKH) {
        this.id = id;
        this.maKH = maKH;
        this.tenKH = tenKH;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    @Override
    public String toString() {
        return "KhachHangEmbedded{" +
            "id='" + id + '\'' +
            ", maKH='" + maKH + '\'' +
            ", tenKH='" + tenKH + '\'' +
            '}';
    }
} 