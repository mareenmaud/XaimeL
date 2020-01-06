package fr.inti.xml.web.rest;

import fr.inti.xml.XmLApp;
import fr.inti.xml.domain.ExtendedUser;
import fr.inti.xml.repository.ExtendedUserRepository;
import fr.inti.xml.repository.UserRepository;
import fr.inti.xml.repository.search.ExtendedUserSearchRepository;
import fr.inti.xml.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static fr.inti.xml.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExtendedUserResource} REST controller.
 */
@SpringBootTest(classes = XmLApp.class)
public class ExtendedUserResourceIT {

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MEMBER_SINCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MEMBER_SINCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_LOCATION_LONGITUDE = 1D;
    private static final Double UPDATED_LOCATION_LONGITUDE = 2D;

    private static final Double DEFAULT_LOCATION_LATITUDE = 1D;
    private static final Double UPDATED_LOCATION_LATITUDE = 2D;

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_INTEREST = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST = "BBBBBBBBBB";

    private static final Double DEFAULT_NOTE = 1D;
    private static final Double UPDATED_NOTE = 2D;

    private static final String DEFAULT_HOBBIES = "AAAAAAAAAA";
    private static final String UPDATED_HOBBIES = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PHOTO_URL = "BBBBBBBBBB";

    @Autowired
    private ExtendedUserRepository extendedUserRepository;

    /**
     * This repository is mocked in the fr.inti.xml.repository.search test package.
     *
     * @see fr.inti.xml.repository.search.ExtendedUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExtendedUserSearchRepository mockExtendedUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    @Autowired
    UserRepository userRepository;

    private MockMvc restExtendedUserMockMvc;

    private ExtendedUser extendedUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExtendedUserResource extendedUserResource = new ExtendedUserResource(extendedUserRepository, mockExtendedUserSearchRepository,
            userRepository);
        this.restExtendedUserMockMvc = MockMvcBuilders.standaloneSetup(extendedUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createEntity() {
        ExtendedUser extendedUser = new ExtendedUser()
            .birthDate(DEFAULT_BIRTH_DATE)
            .memberSince(DEFAULT_MEMBER_SINCE)
            .locationLongitude(DEFAULT_LOCATION_LONGITUDE)
            .locationLatitude(DEFAULT_LOCATION_LATITUDE)
            .gender(DEFAULT_GENDER)
            .interest(DEFAULT_INTEREST)
            .note(DEFAULT_NOTE)
            .hobbies(DEFAULT_HOBBIES)
            .profilePhotoURL(DEFAULT_PROFILE_PHOTO_URL);
        return extendedUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createUpdatedEntity() {
        ExtendedUser extendedUser = new ExtendedUser()
            .birthDate(UPDATED_BIRTH_DATE)
            .memberSince(UPDATED_MEMBER_SINCE)
            .locationLongitude(UPDATED_LOCATION_LONGITUDE)
            .locationLatitude(UPDATED_LOCATION_LATITUDE)
            .gender(UPDATED_GENDER)
            .interest(UPDATED_INTEREST)
            .note(UPDATED_NOTE)
            .hobbies(UPDATED_HOBBIES)
            .profilePhotoURL(UPDATED_PROFILE_PHOTO_URL);
        return extendedUser;
    }

    @BeforeEach
    public void initTest() {
        extendedUserRepository.deleteAll();
        extendedUser = createEntity();
    }

    @Test
    public void createExtendedUser() throws Exception {
        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();

        // Create the ExtendedUser
        restExtendedUserMockMvc.perform(post("/api/extended-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isCreated());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        assertThat(testExtendedUser.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testExtendedUser.getMemberSince()).isEqualTo(DEFAULT_MEMBER_SINCE);
        assertThat(testExtendedUser.getLocationLongitude()).isEqualTo(DEFAULT_LOCATION_LONGITUDE);
        assertThat(testExtendedUser.getLocationLatitude()).isEqualTo(DEFAULT_LOCATION_LATITUDE);
        assertThat(testExtendedUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testExtendedUser.getInterest()).isEqualTo(DEFAULT_INTEREST);
        assertThat(testExtendedUser.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testExtendedUser.getHobbies()).isEqualTo(DEFAULT_HOBBIES);
        assertThat(testExtendedUser.getProfilePhotoURL()).isEqualTo(DEFAULT_PROFILE_PHOTO_URL);

        // Validate the ExtendedUser in Elasticsearch
        verify(mockExtendedUserSearchRepository, times(1)).save(testExtendedUser);
    }

    @Test
    public void createExtendedUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();

        // Create the ExtendedUser with an existing ID
        extendedUser.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtendedUserMockMvc.perform(post("/api/extended-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the ExtendedUser in Elasticsearch
        verify(mockExtendedUserSearchRepository, times(0)).save(extendedUser);
    }


    @Test
    public void getAllExtendedUsers() throws Exception {
        // Initialize the database
        extendedUserRepository.save(extendedUser);

        // Get all the extendedUserList
        restExtendedUserMockMvc.perform(get("/api/extended-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendedUser.getId())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].memberSince").value(hasItem(DEFAULT_MEMBER_SINCE.toString())))
            .andExpect(jsonPath("$.[*].locationLongitude").value(hasItem(DEFAULT_LOCATION_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationLatitude").value(hasItem(DEFAULT_LOCATION_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].interest").value(hasItem(DEFAULT_INTEREST)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].hobbies").value(hasItem(DEFAULT_HOBBIES)))
            .andExpect(jsonPath("$.[*].profilePhotoURL").value(hasItem(DEFAULT_PROFILE_PHOTO_URL)));
    }

    @Test
    public void getExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.save(extendedUser);

        // Get the extendedUser
        restExtendedUserMockMvc.perform(get("/api/extended-users/{id}", extendedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extendedUser.getId()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.memberSince").value(DEFAULT_MEMBER_SINCE.toString()))
            .andExpect(jsonPath("$.locationLongitude").value(DEFAULT_LOCATION_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.locationLatitude").value(DEFAULT_LOCATION_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.interest").value(DEFAULT_INTEREST))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()))
            .andExpect(jsonPath("$.hobbies").value(DEFAULT_HOBBIES))
            .andExpect(jsonPath("$.profilePhotoURL").value(DEFAULT_PROFILE_PHOTO_URL));
    }

    @Test
    public void getNonExistingExtendedUser() throws Exception {
        // Get the extendedUser
        restExtendedUserMockMvc.perform(get("/api/extended-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.save(extendedUser);

        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // Update the extendedUser
        ExtendedUser updatedExtendedUser = extendedUserRepository.findById(extendedUser.getId()).get();
        updatedExtendedUser
            .birthDate(UPDATED_BIRTH_DATE)
            .memberSince(UPDATED_MEMBER_SINCE)
            .locationLongitude(UPDATED_LOCATION_LONGITUDE)
            .locationLatitude(UPDATED_LOCATION_LATITUDE)
            .gender(UPDATED_GENDER)
            .interest(UPDATED_INTEREST)
            .note(UPDATED_NOTE)
            .hobbies(UPDATED_HOBBIES)
            .profilePhotoURL(UPDATED_PROFILE_PHOTO_URL);

        restExtendedUserMockMvc.perform(put("/api/extended-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtendedUser)))
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        assertThat(testExtendedUser.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testExtendedUser.getMemberSince()).isEqualTo(UPDATED_MEMBER_SINCE);
        assertThat(testExtendedUser.getLocationLongitude()).isEqualTo(UPDATED_LOCATION_LONGITUDE);
        assertThat(testExtendedUser.getLocationLatitude()).isEqualTo(UPDATED_LOCATION_LATITUDE);
        assertThat(testExtendedUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testExtendedUser.getInterest()).isEqualTo(UPDATED_INTEREST);
        assertThat(testExtendedUser.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testExtendedUser.getHobbies()).isEqualTo(UPDATED_HOBBIES);
        assertThat(testExtendedUser.getProfilePhotoURL()).isEqualTo(UPDATED_PROFILE_PHOTO_URL);

        // Validate the ExtendedUser in Elasticsearch
        verify(mockExtendedUserSearchRepository, times(1)).save(testExtendedUser);
    }

    @Test
    public void updateNonExistingExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // Create the ExtendedUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc.perform(put("/api/extended-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExtendedUser in Elasticsearch
        verify(mockExtendedUserSearchRepository, times(0)).save(extendedUser);
    }

    @Test
    public void deleteExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.save(extendedUser);

        int databaseSizeBeforeDelete = extendedUserRepository.findAll().size();

        // Delete the extendedUser
        restExtendedUserMockMvc.perform(delete("/api/extended-users/{id}", extendedUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ExtendedUser in Elasticsearch
        verify(mockExtendedUserSearchRepository, times(1)).deleteById(extendedUser.getId());
    }

    @Test
    public void searchExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.save(extendedUser);
        when(mockExtendedUserSearchRepository.search(queryStringQuery("id:" + extendedUser.getId())))
            .thenReturn(Collections.singletonList(extendedUser));
        // Search the extendedUser
        restExtendedUserMockMvc.perform(get("/api/_search/extended-users?query=id:" + extendedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendedUser.getId())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].memberSince").value(hasItem(DEFAULT_MEMBER_SINCE.toString())))
            .andExpect(jsonPath("$.[*].locationLongitude").value(hasItem(DEFAULT_LOCATION_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationLatitude").value(hasItem(DEFAULT_LOCATION_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].interest").value(hasItem(DEFAULT_INTEREST)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].hobbies").value(hasItem(DEFAULT_HOBBIES)))
            .andExpect(jsonPath("$.[*].profilePhotoURL").value(hasItem(DEFAULT_PROFILE_PHOTO_URL)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtendedUser.class);
        ExtendedUser extendedUser1 = new ExtendedUser();
        extendedUser1.setId("id1");
        ExtendedUser extendedUser2 = new ExtendedUser();
        extendedUser2.setId(extendedUser1.getId());
        assertThat(extendedUser1).isEqualTo(extendedUser2);
        extendedUser2.setId("id2");
        assertThat(extendedUser1).isNotEqualTo(extendedUser2);
        extendedUser1.setId(null);
        assertThat(extendedUser1).isNotEqualTo(extendedUser2);
    }
}
