package dtu.k30.msc.whm.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Masterdata.
 */
@Document(collection = "Masterdata")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Masterdata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 20)
    @Field("category")
    private String category;

    @NotNull
    @Size(max = 50)
    @Field("data_key")
    private String dataKey;

    @NotNull
    @Size(max = 200)
    @Field("data_value")
    private String dataValue;

    @Field("is_deleted")
    private Boolean isDeleted;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Masterdata id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public Masterdata category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDataKey() {
        return this.dataKey;
    }

    public Masterdata dataKey(String dataKey) {
        this.setDataKey(dataKey);
        return this;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataValue() {
        return this.dataValue;
    }

    public Masterdata dataValue(String dataValue) {
        this.setDataValue(dataValue);
        return this;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Masterdata isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Masterdata createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Masterdata createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Masterdata updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Masterdata updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Masterdata)) {
            return false;
        }
        return getId() != null && getId().equals(((Masterdata) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Masterdata{" +
            "id=" + getId() +
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
