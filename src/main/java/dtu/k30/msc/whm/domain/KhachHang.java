package dtu.k30.msc.whm.domain;

import dtu.k30.msc.whm.domain.enumeration.EmpSex;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A KhachHang.
 */
@Document(collection = "KhachHang")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KhachHang implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Size(max = 10)
    @Field("ma_kh")
    private String maKH;

    @NotNull
    @Size(max = 50)
    @Field("ten_kh")
    private String tenKH;

    @Field("goi_tinh")
    private EmpSex goiTinh;

    @Field("date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 500)
    @Field("dia_chi")
    private String diaChi;

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

    public KhachHang id(String id) {
        this.setId(id);
        return this;
    }

    public String getMaKH() {
        return this.maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public KhachHang maKH(String maKH) {
        this.setMaKH(maKH);
        return this;
    }

    public String getTenKH() {
        return this.tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public KhachHang tenKH(String tenKH) {
        this.setTenKH(tenKH);
        return this;
    }

    public EmpSex getGoiTinh() {
        return this.goiTinh;
    }

    public void setGoiTinh(EmpSex goiTinh) {
        this.goiTinh = goiTinh;
    }

    public KhachHang goiTinh(EmpSex goiTinh) {
        this.setGoiTinh(goiTinh);
        return this;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public KhachHang dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public String getDiaChi() {
        return this.diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public KhachHang diaChi(String diaChi) {
        this.setDiaChi(diaChi);
        return this;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public KhachHang createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public KhachHang createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public KhachHang updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public KhachHang updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public KhachHang isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KhachHang)) {
            return false;
        }
        return getId() != null && getId().equals(((KhachHang) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KhachHang{" +
                "id=" + getId() +
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
