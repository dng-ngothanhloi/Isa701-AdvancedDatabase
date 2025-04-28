package dtu.k30.msc.whm.domain;

import dtu.k30.msc.whm.domain.enumeration.VoucherType;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PhieuNhapXuat.
 */
@Document(collection = "PhieuNhapXuat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhieuNhapXuat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Size(max = 10)
    @Field("ma_phieu")
    private String maPhieu;

    @Field("ngay_lap_phieu")
    private LocalDate ngayLapPhieu;

    @Field("loai_phieu")
    private VoucherType loaiPhieu;

    @Field("created_at")
    private Instant createdAt;

    @Size(max = 50)
    @Field("created_by")
    private String createdBy;

    @Field("updated_at")
    private Instant updatedAt;

    @Size(max = 50)
    @Field("updated_by")
    private String updatedBy;

    @Field("is_deleted")
    private Boolean isDeleted;

    @DBRef
    @Field("khachHang")
    private KhachHang khachHang;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PhieuNhapXuat id(String id) {
        this.setId(id);
        return this;
    }

    public String getMaPhieu() {
        return this.maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public PhieuNhapXuat maPhieu(String maPhieu) {
        this.setMaPhieu(maPhieu);
        return this;
    }

    public LocalDate getNgayLapPhieu() {
        return this.ngayLapPhieu;
    }

    public void setNgayLapPhieu(LocalDate ngayLapPhieu) {
        this.ngayLapPhieu = ngayLapPhieu;
    }

    public PhieuNhapXuat ngayLapPhieu(LocalDate ngayLapPhieu) {
        this.setNgayLapPhieu(ngayLapPhieu);
        return this;
    }

    public VoucherType getLoaiPhieu() {
        return this.loaiPhieu;
    }

    public void setLoaiPhieu(VoucherType loaiPhieu) {
        this.loaiPhieu = loaiPhieu;
    }

    public PhieuNhapXuat loaiPhieu(VoucherType loaiPhieu) {
        this.setLoaiPhieu(loaiPhieu);
        return this;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public PhieuNhapXuat createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public PhieuNhapXuat createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PhieuNhapXuat updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public PhieuNhapXuat updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public PhieuNhapXuat isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public KhachHang getKhachHang() {
        return this.khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public PhieuNhapXuat khachHang(KhachHang khachHang) {
        this.setKhachHang(khachHang);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhieuNhapXuat)) {
            return false;
        }
        return getId() != null && getId().equals(((PhieuNhapXuat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhieuNhapXuat{" +
                "id=" + getId() +
                ", maPhieu='" + getMaPhieu() + "'" +
                ", ngayLapPhieu='" + getNgayLapPhieu() + "'" +
                ", loaiPhieu='" + getLoaiPhieu() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", createdBy='" + getCreatedBy() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", updatedBy='" + getUpdatedBy() + "'" +
                ", isDeleted='" + getIsDeleted() + "'" +
                "}";
    }
}
