package dtu.k30.msc.whm.service.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link dtu.k30.msc.whm.domain.Masterdata} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MasterdataDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 20)
    private String category;

    @NotNull
    @Size(max = 50)
    private String dataKey;

    @NotNull
    @Size(max = 200)
    private String dataValue;

    private Boolean isDeleted;

    private Instant createdAt;

    @Size(max = 50)
    private String createdBy;

    private Instant updatedAt;

    @Size(max = 50)
    private String updatedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MasterdataDTO)) {
            return false;
        }

        MasterdataDTO masterdataDTO = (MasterdataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, masterdataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MasterdataDTO{" +
                "id='" + getId() + "'" +
                ", category='" + getCategory() + "'" +
                ", dataKey='" + getDataKey() + "'" +
                ", dataValue='" + getDataValue() + "'" +
                ", isDeleted='" + getIsDeleted() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", createdBy='" + getCreatedBy() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", updatedBy='" + getUpdatedBy() + "'" +
                "}";
    }
}
