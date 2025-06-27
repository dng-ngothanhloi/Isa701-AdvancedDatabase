package dtu.k30.msc.whm.domain;

import dtu.k30.msc.whm.domain.enumeration.EmpSex;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for KhachHang data in PhieuNhapXuatEmbedded.
 * This contains only essential fields needed for aggregation queries.
 */
public class KhachHangEmbedded implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("id")
    private String id;

    @Field("ma_kh")
    private String maKH;

    @Field("ten_kh")
    private String tenKH;

    @Field("goi_tinh")
    private EmpSex goiTinh;

    @Field("date_of_birth")
    private LocalDate dateOfBirth;

    @Field("dia_chi")
    private String diaChi;

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

    public KhachHangEmbedded() {
        // Default constructor
    }

    public KhachHangEmbedded(String id, String maKH, String tenKH) {
        this.id = id;
        this.maKH = maKH;
        this.tenKH = tenKH;
    }

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
    public String toString() {
        return "KhachHangEmbedded{" +
            "id='" + id + '\'' +
            ", maKH='" + maKH + '\'' +
            ", tenKH='" + tenKH + '\'' +
            '}';
    }
} 