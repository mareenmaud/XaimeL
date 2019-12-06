package fr.inti.xml.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A ITProfile.
 */
@Document(collection = "it_profile")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "itprofile")
public class ITProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("job")
    private String job;

    @Field("fav_language")
    private String favLanguage;

    @Field("fav_os")
    private String favOS;

    @Field("gamer")
    private Boolean gamer;

    @Field("geek")
    private Boolean geek;

    @Field("otaku")
    private Boolean otaku;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public ITProfile job(String job) {
        this.job = job;
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFavLanguage() {
        return favLanguage;
    }

    public ITProfile favLanguage(String favLanguage) {
        this.favLanguage = favLanguage;
        return this;
    }

    public void setFavLanguage(String favLanguage) {
        this.favLanguage = favLanguage;
    }

    public String getFavOS() {
        return favOS;
    }

    public ITProfile favOS(String favOS) {
        this.favOS = favOS;
        return this;
    }

    public void setFavOS(String favOS) {
        this.favOS = favOS;
    }

    public Boolean isGamer() {
        return gamer;
    }

    public ITProfile gamer(Boolean gamer) {
        this.gamer = gamer;
        return this;
    }

    public void setGamer(Boolean gamer) {
        this.gamer = gamer;
    }

    public Boolean isGeek() {
        return geek;
    }

    public ITProfile geek(Boolean geek) {
        this.geek = geek;
        return this;
    }

    public void setGeek(Boolean geek) {
        this.geek = geek;
    }

    public Boolean isOtaku() {
        return otaku;
    }

    public ITProfile otaku(Boolean otaku) {
        this.otaku = otaku;
        return this;
    }

    public void setOtaku(Boolean otaku) {
        this.otaku = otaku;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ITProfile)) {
            return false;
        }
        return id != null && id.equals(((ITProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ITProfile{" +
            "id=" + getId() +
            ", job='" + getJob() + "'" +
            ", favLanguage='" + getFavLanguage() + "'" +
            ", favOS='" + getFavOS() + "'" +
            ", gamer='" + isGamer() + "'" +
            ", geek='" + isGeek() + "'" +
            ", otaku='" + isOtaku() + "'" +
            "}";
    }
}
