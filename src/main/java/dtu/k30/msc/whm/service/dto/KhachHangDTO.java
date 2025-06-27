package dtu.k30.msc.whm.service.dto;

import dtu.k30.msc.whm.domain.enumeration.EmpSex;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link dtu.k30.msc.whm.domain.KhachHang} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KhachHangDTO implements Serializable {

    private String id;

    @Size(max = 10)
    private String maKH;

    @NotNull
    @Size(max = 50)
    private String tenKH;

    private EmpSex goiTinh;

    private LocalDate dateOfBirth;

    @Size(max = 500)
    private String diaChi;

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

    public EmpSex getGoiTinh() {
        return goiTinh;
    }

    public void setGoiTinh(EmpSex goiTinh) {
        this.goiTinh = goiTinh;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
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
        if (!(o instanceof KhachHangDTO)) {
            return false;
        }

        KhachHangDTO khachHangDTO = (KhachHangDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, khachHangDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KhachHangDTO{" +
            "id='" + getId() + "'" +
            ", maKH='" + getMaKH() + "'" +
            ", tenKH='" + getTenKH() + "'" +
            ", goiTinh='" + getGoiTinh() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", diaChi='" + getDiaChi() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
