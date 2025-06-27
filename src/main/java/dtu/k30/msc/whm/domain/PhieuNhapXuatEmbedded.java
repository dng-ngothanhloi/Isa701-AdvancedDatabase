package dtu.k30.msc.whm.domain;

import dtu.k30.msc.whm.domain.enumeration.VoucherType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for PhieuNhapXuat data in ChiTietNhapXuat.
 * This contains only essential fields needed for aggregation queries.
 */
public class PhieuNhapXuatEmbedded implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("id")
    private String id;

    @Field("ma_phieu")
    private String maPhieu;

    @Field("ngay_lap_phieu")
    private LocalDate ngayLapPhieu;

    @Field("loai_phieu")
    private VoucherType loaiPhieu;

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

    // Embedded KhachHang data for aggregation queries
    @Field("khach_hang")
    private KhachHangEmbedded khachHang;

    public PhieuNhapXuatEmbedded() {
        // Default constructor
    }

    public PhieuNhapXuatEmbedded(String id, String maPhieu, LocalDate ngayLapPhieu, VoucherType loaiPhieu) {
        this.id = id;
        this.maPhieu = maPhieu;
        this.ngayLapPhieu = ngayLapPhieu;
        this.loaiPhieu = loaiPhieu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public LocalDate getNgayLapPhieu() {
        return ngayLapPhieu;
    }

    public void setNgayLapPhieu(LocalDate ngayLapPhieu) {
        this.ngayLapPhieu = ngayLapPhieu;
    }

    public VoucherType getLoaiPhieu() {
        return loaiPhieu;
    }

    public void setLoaiPhieu(VoucherType loaiPhieu) {
        this.loaiPhieu = loaiPhieu;
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

    public KhachHangEmbedded getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHangEmbedded khachHang) {
        this.khachHang = khachHang;
    }

    @Override
    public String toString() {
        return "PhieuNhapXuatEmbedded{" +
            "id='" + id + '\'' +
            ", maPhieu='" + maPhieu + '\'' +
            ", ngayLapPhieu=" + ngayLapPhieu +
            ", loaiPhieu=" + loaiPhieu +
            '}';
    }
} 