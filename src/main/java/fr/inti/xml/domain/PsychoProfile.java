package fr.inti.xml.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A PsychoProfile.
 */
@Document(collection = "psycho_profile")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "psychoprofile")
public class PsychoProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("summary_profile")
    private String summaryProfile;

    @Field("jung_values")
    private Double jungValues;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummaryProfile() {
        return summaryProfile;
    }

    public PsychoProfile summaryProfile(String summaryProfile) {
        this.summaryProfile = summaryProfile;
        return this;
    }

    public void setSummaryProfile(String summaryProfile) {
        this.summaryProfile = summaryProfile;
    }

    public Double getJungValues() {
        return jungValues;
    }

    public PsychoProfile jungValues(Double jungValues) {
        this.jungValues = jungValues;
        return this;
    }

    public void setJungValues(Double jungValues) {
        this.jungValues = jungValues;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PsychoProfile)) {
            return false;
        }
        return id != null && id.equals(((PsychoProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PsychoProfile{" +
            "id=" + getId() +
            ", summaryProfile='" + getSummaryProfile() + "'" +
            ", jungValues=" + getJungValues() +
            "}";
    }
}
