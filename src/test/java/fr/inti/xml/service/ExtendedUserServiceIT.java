package fr.inti.xml.service;

import fr.inti.xml.XmLApp;
import fr.inti.xml.domain.ExtendedUser;
import fr.inti.xml.domain.ITProfile;
import fr.inti.xml.domain.PsychoProfile;
import fr.inti.xml.domain.User;
import fr.inti.xml.repository.ExtendedUserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Integration tests for {@link ExtendedUserService}.
 */
@SpringBootTest(classes = XmLApp.class)
public class ExtendedUserServiceIT {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String DEFAULT_LASTNAME = "doe";
    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String DEFAULT_LANGKEY = "dummy";

    private static final String DEFAULT_JOB = "Unemployed";
    private static final String DEFAULT_FAVLANGUAGE = "Java";
    private static final String DEFAULT_FAVOS = "Windows";
    private static final Boolean DEFAULT_GAMER = true;
    private static final Boolean DEFAULT_GEEK = true;
    private static final Boolean DEFAULT_OTAKU = true;

    private static final String DEFAULT_SUMMARYPROFILE = "XXXX";
    private static final Double DEFAULT_JUNGVALUE1 = 0.0;
    private static final Double DEFAULT_JUNGVALUE2 = 0.0;
    private static final Double DEFAULT_JUNGVALUE3 = 0.0;
    private static final Double DEFAULT_JUNGVALUE4 = 0.0;

    private static final Instant DEFAULT_BIRTHDATE = Instant.now();
    private static final Instant DEFAULT_MEMBERSINCE = Instant.now();
    private static final Double DEFAULT_LOCATIONLONGITUDE = 0.0;
    private static final Double DEFAULT_LOCATIONLATITUDE = 0.0;
    private static final String DEFAULT_GENDER = "M";
    private static final String DEFAULT_INTEREST = "F";
    private static final Double DEFAULT_NOTE = 0.0;
    private static final String DEFAULT_HOBBIES = "hobbies";
    private static final String DEFAULT_PROFILEPHOTOURL = "photo  URL";
    private static final ITProfile DEFAULT_ITPROFILE = new ITProfile();
    private static final PsychoProfile DEFAULT_PSYCHOPROFILE = new PsychoProfile();
    private static final User DEFAULT_USER = new User();
    private static final ExtendedUser DEFAULT_EXTENDEDUSER = null;
    private static final Set<String> DEFAULT_MATCHES = new HashSet<>();

    @Autowired
    private ExtendedUserRepository extendedUserRepository;

    @Autowired
    private ExtendedUserService extendedUserService;

    private ExtendedUser extendedUser;

    @BeforeEach
    public void init() {
        DEFAULT_USER.setLogin(DEFAULT_LOGIN);
        DEFAULT_USER.setPassword(RandomStringUtils.random(60));
        DEFAULT_USER.setActivated(true);
        DEFAULT_USER.setEmail(DEFAULT_EMAIL);
        DEFAULT_USER.setFirstName(DEFAULT_FIRSTNAME);
        DEFAULT_USER.setLastName(DEFAULT_LASTNAME);
        DEFAULT_USER.setImageUrl(DEFAULT_IMAGEURL);
        DEFAULT_USER.setLangKey(DEFAULT_LANGKEY);

        DEFAULT_ITPROFILE.setJob(DEFAULT_JOB);
        DEFAULT_ITPROFILE.setFavLanguage(DEFAULT_FAVLANGUAGE);
        DEFAULT_ITPROFILE.setFavOS(DEFAULT_FAVOS);
        DEFAULT_ITPROFILE.setGamer(DEFAULT_GAMER);
        DEFAULT_ITPROFILE.setGeek(DEFAULT_GEEK);
        DEFAULT_ITPROFILE.setOtaku(DEFAULT_OTAKU);

        DEFAULT_PSYCHOPROFILE.setSummaryProfile(DEFAULT_SUMMARYPROFILE);
        DEFAULT_PSYCHOPROFILE.setJungValue1(DEFAULT_JUNGVALUE1);
        DEFAULT_PSYCHOPROFILE.setJungValue2(DEFAULT_JUNGVALUE2);
        DEFAULT_PSYCHOPROFILE.setJungValue3(DEFAULT_JUNGVALUE3);
        DEFAULT_PSYCHOPROFILE.setJungValue4(DEFAULT_JUNGVALUE4);

        extendedUserRepository.deleteAll();
        extendedUser = new ExtendedUser();
        extendedUser.setBirthDate(DEFAULT_BIRTHDATE);
        extendedUser.setMemberSince(DEFAULT_MEMBERSINCE);
        extendedUser.setLocationLongitude(DEFAULT_LOCATIONLONGITUDE);
        extendedUser.setLocationLatitude(DEFAULT_LOCATIONLATITUDE);
        extendedUser.setGender(DEFAULT_GENDER);
        extendedUser.setInterest(DEFAULT_INTEREST);
        extendedUser.setNote(DEFAULT_NOTE);
        extendedUser.setHobbies(DEFAULT_HOBBIES);
        extendedUser.setProfilePhotoURL(DEFAULT_IMAGEURL);
        extendedUser.setITProfile(DEFAULT_ITPROFILE);
        extendedUser.setPsychoProfile(DEFAULT_PSYCHOPROFILE);
        extendedUser.setUser(DEFAULT_USER);
        extendedUser.setExtendedUser(DEFAULT_EXTENDEDUSER);
        extendedUser.setMatches(DEFAULT_MATCHES);
    }

    @Test
    public void test1() {
        assertThat(true).isTrue();
    }

}
