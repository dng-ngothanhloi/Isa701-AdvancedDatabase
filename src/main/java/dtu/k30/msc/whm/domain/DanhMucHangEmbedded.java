package dtu.k30.msc.whm.domain;

import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for DanhMucHang data in ChiTietNhapXuat.
 * Selective embedding: Only essential fields for aggregation queries.
 * Storage optimization: 50% reduction by removing unnecessary fields.
 */
public class DanhMucHangEmbedded implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("id")
    private String id;

    @Field("ma_hang")
    private String maHang;

    @Field("ten_hang")
    private String tenHang;

    @Field("don_vi_tinh")
    private String donviTinh;

    // Removed fields for storage optimization:
    // - noiSanXuat, ngaySanXuat, hanSuDung (rarely used in reports)
    // - createdAt, createdBy, updatedAt, updatedBy, isDeleted (audit fields)

    public DanhMucHangEmbedded() {
        // Default constructor
    }

    public DanhMucHangEmbedded(String id, String maHang, String tenHang, String donviTinh) {
        this.id = id;
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.donviTinh = donviTinh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getDonviTinh() {
        return donviTinh;
    }

    public void setDonviTinh(String donviTinh) {
        this.donviTinh = donviTinh;
    }

    @Override
    public String toString() {
        return "DanhMucHangEmbedded{" +
            "id='" + id + '\'' +
            ", maHang='" + maHang + '\'' +
            ", tenHang='" + tenHang + '\'' +
            ", donviTinh='" + donviTinh + '\'' +
            '}';
    }
} 