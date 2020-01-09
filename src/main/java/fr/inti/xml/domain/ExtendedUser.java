package fr.inti.xml.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ExtendedUser.
 */
@Document(collection = "extended_user")

public class ExtendedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id

    private String id;

    @Field("birth_date")
    private Instant birthDate;

    @Field("member_since")
    private Instant memberSince;

    @Field("location_longitude")
    private Double locationLongitude;

    @Field("location_latitude")
    private Double locationLatitude;

    @Field("gender")
    private String gender;

    @Field("interest")
    private String interest;

    @Field("note")
    private Double note;

    @Field("hobbies")
    private String hobbies;

    @Field("profile_photo_url")
    private String profilePhotoURL;

    @DBRef
    @Field("iTProfile")
    private ITProfile iTProfile;

    @DBRef
    @Field("psychoProfile")
    private PsychoProfile psychoProfile;

    @DBRef
    @Field("user")
    private User user;

    @DBRef
    @Field("extendedUser")
    @JsonIgnoreProperties("extendedUsers")
    private ExtendedUser extendedUser;

    @Field("invitations")
    private Set<String> invitations = new HashSet<>();

    @Field("matches")
    private Set<String> matches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public ExtendedUser birthDate(Instant birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Instant getMemberSince() {
        return memberSince;
    }

    public ExtendedUser memberSince(Instant memberSince) {
        this.memberSince = memberSince;
        return this;
    }

    public void setMemberSince(Instant memberSince) {
        this.memberSince = memberSince;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public ExtendedUser locationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
        return this;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public ExtendedUser locationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
        return this;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getGender() {
        return gender;
    }

    public ExtendedUser gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInterest() {
        return interest;
    }

    public ExtendedUser interest(String interest) {
        this.interest = interest;
        return this;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public Double getNote() {
        return note;
    }

    public ExtendedUser note(Double note) {
        this.note = note;
        return this;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public String getHobbies() {
        return hobbies;
    }

    public ExtendedUser hobbies(String hobbies) {
        this.hobbies = hobbies;
        return this;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public ExtendedUser profilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
        return this;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public ITProfile getITProfile() {
        return iTProfile;
    }

    public ExtendedUser iTProfile(ITProfile iTProfile) {
        this.iTProfile = iTProfile;
        return this;
    }

    public void setITProfile(ITProfile iTProfile) {
        this.iTProfile = iTProfile;
    }

    public PsychoProfile getPsychoProfile() {
        return psychoProfile;
    }

    public ExtendedUser psychoProfile(PsychoProfile psychoProfile) {
        this.psychoProfile = psychoProfile;
        return this;
    }

    public void setPsychoProfile(PsychoProfile psychoProfile) {
        this.psychoProfile = psychoProfile;
    }

    public User getUser() {
        return user;
    }

    public ExtendedUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExtendedUser getExtendedUser() {
        return extendedUser;
    }

    public ExtendedUser extendedUser(ExtendedUser extendedUser) {
        this.extendedUser = extendedUser;
        return this;
    }

    public void setExtendedUser(ExtendedUser extendedUser) {
        this.extendedUser = extendedUser;
    }

    public Set<String> getInvitations() {
        return invitations;
    }

    public ExtendedUser invitations(Set<String> extendedUsers) {
        this.invitations = extendedUsers;
        return this;
    }

    public ExtendedUser addInvitations(String extendedUser) {
        this.invitations.add(extendedUser);
        return this;
    }

    public ExtendedUser removeInvitations(String extendedUser) {
        this.invitations.remove(extendedUser);
        return this;
    }

    public Set<String> getMatches() {
        return matches;
    }

    public ExtendedUser matches(Set<String> extendedUsers) {
        this.matches = extendedUsers;
        return this;
    }

    public ExtendedUser addMatches(String extendedUser) {
        this.matches.add(extendedUser);
        return this;
    }

    public ExtendedUser removeMatches(String extendedUser) {
        this.matches.remove(extendedUser);
        return this;
    }

    public void setMatches(Set<String> extendedUsers) {
        this.matches = extendedUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtendedUser)) {
            return false;
        }
        return id != null && id.equals(((ExtendedUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ExtendedUser{" +
            "id=" + getId() +
            ", birthDate='" + getBirthDate() + "'" +
            ", memberSince='" + getMemberSince() + "'" +
            ", locationLongitude=" + getLocationLongitude() +
            ", locationLatitude=" + getLocationLatitude() +
            ", gender='" + getGender() + "'" +
            ", interest='" + getInterest() + "'" +
            ", note=" + getNote() +
            ", hobbies='" + getHobbies() + "'" +
            ", profilePhotoURL='" + getProfilePhotoURL() + "'" +
            "}";
    }
}
