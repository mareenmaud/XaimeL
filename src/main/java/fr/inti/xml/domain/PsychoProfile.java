package fr.inti.xml.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * A PsychoProfile.
 */
@Document(collection = "psycho_profile")
public class PsychoProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("summary_profile")
    private String summaryProfile;

    @Field("jung_value_1")
    private Double jungValue1;

    @Field("jung_value_2")
    private Double jungValue2;

    @Field("jung_value_3")
    private Double jungValue3;

    @Field("jung_value_4")
    private Double jungValue4;

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

    public Double getJungValue1() {
        return jungValue1;
    }

    public PsychoProfile jungValue1(Double jungValue1) {
        this.jungValue1 = jungValue1;
        return this;
    }

    public void setJungValue1(Double jungValue1) {
        this.jungValue1 = jungValue1;
    }

    public Double getJungValue2() {
        return jungValue2;
    }

    public PsychoProfile jungValue2(Double jungValue2) {
        this.jungValue2 = jungValue2;
        return this;
    }

    public void setJungValue2(Double jungValue2) {
        this.jungValue2 = jungValue2;
    }

    public Double getJungValue3() {
        return jungValue3;
    }

    public PsychoProfile jungValue3(Double jungValue3) {
        this.jungValue3 = jungValue3;
        return this;
    }

    public void setJungValue3(Double jungValue3) {
        this.jungValue3 = jungValue3;
    }

    public Double getJungValue4() {
        return jungValue4;
    }

    public PsychoProfile jungValue4(Double jungValue4) {
        this.jungValue4 = jungValue4;
        return this;
    }

    public void setJungValue4(Double jungValue4) {
        this.jungValue4 = jungValue4;
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
            ", jungValue1=" + getJungValue1() +
            ", jungValue2=" + getJungValue2() +
            ", jungValue3=" + getJungValue3() +
            ", jungValue4=" + getJungValue4() +
            "}";
    }
}
