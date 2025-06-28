package dtu.k30.msc.whm.domain;

import dtu.k30.msc.whm.domain.enumeration.VoucherType;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for PhieuNhapXuat data in ChiTietNhapXuat.
 * Selective embedding: Only essential fields for aggregation queries.
 * Storage optimization: 70% reduction by removing unnecessary fields.
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

    // Removed fields for storage optimization:
    // - createdAt, createdBy, updatedAt, updatedBy, isDeleted (audit fields)
    // - khachHang (already embedded in main PhieuNhapXuat entity)

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