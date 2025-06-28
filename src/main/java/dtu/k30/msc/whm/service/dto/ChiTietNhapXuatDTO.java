package dtu.k30.msc.whm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link dtu.k30.msc.whm.domain.ChiTietNhapXuat} entity.
 * Selective embedding: Uses DTOs for performance optimization.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChiTietNhapXuatDTO implements Serializable {

    private String id;

    @NotNull
    private Integer soLuong;

    private BigDecimal donGia;

    private Instant createdAt;

    @Size(max = 50)
    private String createdBy;

    private Instant updatedAt;

    @Size(max = 50)
    private String updatedBy;

    private Boolean isDeleted;

    private PhieuNhapXuatDTO phieuNhapXuat;

    private DanhMucHangDTO maHang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
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

    public PhieuNhapXuatDTO getPhieuNhapXuat() {
        return phieuNhapXuat;
    }

    public void setPhieuNhapXuat(PhieuNhapXuatDTO phieuNhapXuat) {
        this.phieuNhapXuat = phieuNhapXuat;
    }

    public DanhMucHangDTO getMaHang() {
        return maHang;
    }

    public void setMaHang(DanhMucHangDTO maHang) {
        this.maHang = maHang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChiTietNhapXuatDTO)) {
            return false;
        }

        ChiTietNhapXuatDTO chiTietNhapXuatDTO = (ChiTietNhapXuatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chiTietNhapXuatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChiTietNhapXuatDTO{" +
            "id='" + getId() + "'" +
            ", soLuong=" + getSoLuong() +
            ", donGia=" + getDonGia() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", phieuNhapXuat=" + getPhieuNhapXuat() +
            ", maHang=" + getMaHang() +
            "}";
    }
}
