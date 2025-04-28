package dtu.k30.msc.whm.domain;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DanhMucHang.
 */
@Document(collection = "DanhMucHang")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DanhMucHang implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Size(max = 10)
    @Field("ma_hang")
    private String maHang;

    @NotNull
    @Size(max = 100)
    @Field("ten_hang")
    private String tenHang;

    @Size(max = 50)
    @Field("don_vitinh")
    private String donVitinh;

    @NotNull
    @Size(max = 100)
    @Field("noi_san_xuat")
    private String noiSanXuat;

    @Field("ngay_san_xuat")
    private LocalDate ngaySanXuat;

    @Field("han_su_dung")
    private LocalDate hanSuDung;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DanhMucHang id(String id) {
        this.setId(id);
        return this;
    }

    public String getMaHang() {
        return this.maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public DanhMucHang maHang(String maHang) {
        this.setMaHang(maHang);
        return this;
    }

    public String getTenHang() {
        return this.tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public DanhMucHang tenHang(String tenHang) {
        this.setTenHang(tenHang);
        return this;
    }

    public String getDonVitinh() {
        return this.donVitinh;
    }

    public void setDonVitinh(String donVitinh) {
        this.donVitinh = donVitinh;
    }

    public DanhMucHang donVitinh(String donVitinh) {
        this.setDonVitinh(donVitinh);
        return this;
    }

    public String getNoiSanXuat() {
        return this.noiSanXuat;
    }

    public void setNoiSanXuat(String noiSanXuat) {
        this.noiSanXuat = noiSanXuat;
    }

    public DanhMucHang noiSanXuat(String noiSanXuat) {
        this.setNoiSanXuat(noiSanXuat);
        return this;
    }

    public LocalDate getNgaySanXuat() {
        return this.ngaySanXuat;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public DanhMucHang ngaySanXuat(LocalDate ngaySanXuat) {
        this.setNgaySanXuat(ngaySanXuat);
        return this;
    }

    public LocalDate getHanSuDung() {
        return this.hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public DanhMucHang hanSuDung(LocalDate hanSuDung) {
        this.setHanSuDung(hanSuDung);
        return this;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public DanhMucHang createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DanhMucHang createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DanhMucHang updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public DanhMucHang updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public DanhMucHang isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DanhMucHang)) {
            return false;
        }
        return getId() != null && getId().equals(((DanhMucHang) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DanhMucHang{" +
                "id=" + getId() +
                ", maHang='" + getMaHang() + "'" +
                ", tenHang='" + getTenHang() + "'" +
                ", donVitinh='" + getDonVitinh() + "'" +
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
