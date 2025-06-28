package dtu.k30.msc.whm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link dtu.k30.msc.whm.domain.DanhMucHang} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DanhMucHangDTO implements Serializable {

    private String id;

    @Size(max = 10)
    private String maHang;

    @NotNull
    @Size(max = 100)
    private String tenHang;

    @Size(max = 50)
    private String donviTinh;

    @NotNull
    @Size(max = 100)
    private String noiSanXuat;

    private LocalDate ngaySanXuat;

    private LocalDate hanSuDung;

    private Instant createdAt;

    @Size(max = 50)
    private String createdBy;

    private Instant updatedAt;

    @Size(max = 50)
    private String updatedBy;

    private Boolean isDeleted;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DanhMucHangDTO)) {
            return false;
        }

        DanhMucHangDTO danhMucHangDTO = (DanhMucHangDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, danhMucHangDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DanhMucHangDTO{" +
            "id='" + getId() + "'" +
            ", maHang='" + getMaHang() + "'" +
            ", tenHang='" + getTenHang() + "'" +
            ", donviTinh='" + getDonviTinh() + "'" +
            ", noiSanXuat='" + getNoiSanXuat() + "'" +
            ", ngaySanXuat='" + getNgaySanXuat() + "'" +
            ", hanSuDung='" + getHanSuDung() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
