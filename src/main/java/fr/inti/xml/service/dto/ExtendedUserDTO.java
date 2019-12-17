package fr.inti.xml.service.dto;

import fr.inti.xml.domain.ExtendedUser;
import fr.inti.xml.domain.ITProfile;
import fr.inti.xml.domain.PsychoProfile;
import fr.inti.xml.domain.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO representing an extended user, with his authorities.
 */
public class ExtendedUserDTO {

    private String id;

    private Instant birthDate;

    private Instant memberSince;

    private Double locationLongitude;

    private Double locationLatitude;

    private String gender;

    private String interest;

    private Double note;

    private String hobbies;

    private String profilePhotoURL;

    private ITProfile iTProfile;

    private PsychoProfile psychoProfile;

    private User user;

    private ExtendedUser extendedUser;

    private Set<ExtendedUser> matches = new HashSet<>();

    public ExtendedUserDTO() { }

    public ExtendedUserDTO(ExtendedUser extendedUser) {
        this.id = extendedUser.getId();
        this.birthDate = extendedUser.getBirthDate();
        this.memberSince = extendedUser.getMemberSince();
        this.locationLongitude = extendedUser.getLocationLongitude();
        this.locationLatitude = extendedUser.getLocationLatitude();
        this.gender = extendedUser.getGender();
        this.interest = extendedUser.getInterest();
        this.note = extendedUser.getNote();
        this.hobbies = extendedUser.getHobbies();
        this.profilePhotoURL = extendedUser.getProfilePhotoURL();
        this.iTProfile = extendedUser.getITProfile();
        this.psychoProfile = extendedUser.getPsychoProfile();
        this.user = extendedUser.getUser();
        this.extendedUser = extendedUser.getExtendedUser();
        this.matches = extendedUser.getMatches();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Instant getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(Instant memberSince) {
        this.memberSince = memberSince;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public ITProfile getiTProfile() {
        return iTProfile;
    }

    public void setiTProfile(ITProfile iTProfile) {
        this.iTProfile = iTProfile;
    }

    public PsychoProfile getPsychoProfile() {
        return psychoProfile;
    }

    public void setPsychoProfile(PsychoProfile psychoProfile) {
        this.psychoProfile = psychoProfile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExtendedUser getExtendedUser() {
        return extendedUser;
    }

    public void setExtendedUser(ExtendedUser extendedUser) {
        this.extendedUser = extendedUser;
    }

    public Set<ExtendedUser> getMatches() {
        return matches;
    }

    public void setMatches(Set<ExtendedUser> matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return "ExtendedUserDTO{" +
            "id='" + id + '\'' +
            ", birthDate=" + birthDate +
            ", memberSince=" + memberSince +
            ", locationLongitude=" + locationLongitude +
            ", locationLatitude=" + locationLatitude +
            ", gender='" + gender + '\'' +
            ", interest='" + interest + '\'' +
            ", note=" + note +
            ", hobbies='" + hobbies + '\'' +
            ", profilePhotoURL='" + profilePhotoURL + '\'' +
            ", iTProfile=" + iTProfile +
            ", psychoProfile=" + psychoProfile +
            ", user=" + user +
            ", extendedUser=" + extendedUser +
            ", matches=" + matches +
            '}';
    }
}
