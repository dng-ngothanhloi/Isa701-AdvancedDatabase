package dtu.k30.msc.whm.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for DanhMucHang data in ChiTietNhapXuat.
 * This contains only essential fields needed for aggregation queries.
 */
public class DanhMucHangEmbedded implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("id")
    private String id;

    @Field("ma_hang")
    private String maHang;

    @Field("ten_hang")
    private String tenHang;

    @Field("don_vitinh")
    private String donVitinh;

    @Field("noi_san_xuat")
    private String noiSanXuat;

    @Field("ngay_san_xuat")
    private LocalDate ngaySanXuat;

    @Field("han_su_dung")
    private LocalDate hanSuDung;

    @Field("created_at")
    private Instant createdAt;

    @Field("created_by")
    private String createdBy;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("updated_by")
    private String updatedBy;

    @Field("is_deleted")
    private Boolean isDeleted;

    public DanhMucHangEmbedded() {
        // Default constructor
    }

    public DanhMucHangEmbedded(String id, String maHang, String tenHang, String donVitinh, String noiSanXuat) {
        this.id = id;
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.donVitinh = donVitinh;
        this.noiSanXuat = noiSanXuat;
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

    public String getDonVitinh() {
        return donVitinh;
    }

    public void setDonVitinh(String donVitinh) {
        this.donVitinh = donVitinh;
    }

    public String getNoiSanXuat() {
        return noiSanXuat;
    }

    public void setNoiSanXuat(String noiSanXuat) {
        this.noiSanXuat = noiSanXuat;
    }

    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "DanhMucHangEmbedded{" +
            "id='" + id + '\'' +
            ", maHang='" + maHang + '\'' +
            ", tenHang='" + tenHang + '\'' +
            ", donVitinh='" + donVitinh + '\'' +
            ", noiSanXuat='" + noiSanXuat + '\'' +
            '}';
    }
} 