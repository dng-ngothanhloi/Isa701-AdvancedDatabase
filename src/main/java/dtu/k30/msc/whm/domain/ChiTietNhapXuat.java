package dtu.k30.msc.whm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ChiTietNhapXuat.
 */
@Document(collection = "ChiTietNhapXuat")
public class ChiTietNhapXuat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("so_luong")
    private Integer soLuong;

    @Field("don_gia")
    private BigDecimal donGia;

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

    // Embedded document for PhieuNhapXuat data (denormalized)
    @Field("phieu_nhap_xuat")
    private PhieuNhapXuatEmbedded phieuNhapXuat;

    // Embedded document for DanhMucHang data (denormalized)
    @Field("ma_hang")
    private DanhMucHangEmbedded maHang;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ChiTietNhapXuat id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public ChiTietNhapXuat soLuong(Integer soLuong) {
        this.setSoLuong(soLuong);
        return this;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return this.donGia;
    }

    public ChiTietNhapXuat donGia(BigDecimal donGia) {
        this.setDonGia(donGia);
        return this;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ChiTietNhapXuat createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ChiTietNhapXuat createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ChiTietNhapXuat updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ChiTietNhapXuat updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public ChiTietNhapXuat isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public PhieuNhapXuatEmbedded getPhieuNhapXuat() {
        return this.phieuNhapXuat;
    }

    public void setPhieuNhapXuat(PhieuNhapXuatEmbedded phieuNhapXuat) {
        this.phieuNhapXuat = phieuNhapXuat;
    }

    public ChiTietNhapXuat phieuNhapXuat(PhieuNhapXuatEmbedded phieuNhapXuat) {
        this.setPhieuNhapXuat(phieuNhapXuat);
        return this;
    }

    public DanhMucHangEmbedded getMaHang() {
        return this.maHang;
    }

    public void setMaHang(DanhMucHangEmbedded maHang) {
        this.maHang = maHang;
    }

    public ChiTietNhapXuat maHang(DanhMucHangEmbedded maHang) {
        this.setMaHang(maHang);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChiTietNhapXuat)) {
            return false;
        }
        return getId() != null && getId().equals(((ChiTietNhapXuat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChiTietNhapXuat{" +
            "id=" + getId() +
            ", soLuong=" + getSoLuong() +
            ", donGia=" + getDonGia() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
