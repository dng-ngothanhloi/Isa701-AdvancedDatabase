package dtu.k30.msc.whm.service.dto;

import dtu.k30.msc.whm.domain.enumeration.VoucherType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link dtu.k30.msc.whm.domain.PhieuNhapXuat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhieuNhapXuatDTO implements Serializable {

    private String id;

    @Size(max = 10)
    private String maPhieu;

    private LocalDate ngayLapPhieu;

    private VoucherType loaiPhieu;

    private Instant createdAt;

    @Size(max = 50)
    private String createdBy;

    private Instant updatedAt;

    @Size(max = 50)
    private String updatedBy;

    private Boolean isDeleted;

    private KhachHangDTO khachHang;

    private String tenKhachHang;
    private List<ChiTietNhapXuatDTO> ChiTietNhapXuatDTOList;

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

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
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

    public KhachHangDTO getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHangDTO khachHang) {
        this.khachHang = khachHang;
    }

    public List<ChiTietNhapXuatDTO> getChiTietNhapXuatDTOList() {
        return ChiTietNhapXuatDTOList;
    }

    public void setChiTietNhapXuatDTOList(List<ChiTietNhapXuatDTO> chiTietNhapXuatDTOList) {
        ChiTietNhapXuatDTOList = chiTietNhapXuatDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhieuNhapXuatDTO)) {
            return false;
        }

        PhieuNhapXuatDTO phieuNhapXuatDTO = (PhieuNhapXuatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, phieuNhapXuatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhieuNhapXuatDTO{" +
            "id='" + getId() + "'" +
            ", maPhieu='" + getMaPhieu() + "'" +
            ", ngayLapPhieu='" + getNgayLapPhieu() + "'" +
            ", loaiPhieu='" + getLoaiPhieu() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", khachHang=" + getKhachHang() +
            "}";
    }
}
