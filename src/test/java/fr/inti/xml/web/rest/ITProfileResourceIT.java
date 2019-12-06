package fr.inti.xml.web.rest;

import fr.inti.xml.XmLApp;
import fr.inti.xml.domain.ITProfile;
import fr.inti.xml.repository.ITProfileRepository;
import fr.inti.xml.repository.search.ITProfileSearchRepository;
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
 * Integration tests for the {@link ITProfileResource} REST controller.
 */
@SpringBootTest(classes = XmLApp.class)
public class ITProfileResourceIT {

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    private static final String DEFAULT_FAV_LANGUAGES = "AAAAAAAAAA";
    private static final String UPDATED_FAV_LANGUAGES = "BBBBBBBBBB";

    private static final String DEFAULT_FAV_OS = "AAAAAAAAAA";
    private static final String UPDATED_FAV_OS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_GAMER = false;
    private static final Boolean UPDATED_GAMER = true;

    private static final Boolean DEFAULT_GEEK = false;
    private static final Boolean UPDATED_GEEK = true;

    private static final Boolean DEFAULT_OTAKU = false;
    private static final Boolean UPDATED_OTAKU = true;

    @Autowired
    private ITProfileRepository iTProfileRepository;

    /**
     * This repository is mocked in the fr.inti.xml.repository.search test package.
     *
     * @see fr.inti.xml.repository.search.ITProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private ITProfileSearchRepository mockITProfileSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restITProfileMockMvc;

    private ITProfile iTProfile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ITProfileResource iTProfileResource = new ITProfileResource(iTProfileRepository, mockITProfileSearchRepository);
        this.restITProfileMockMvc = MockMvcBuilders.standaloneSetup(iTProfileResource)
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
    public static ITProfile createEntity() {
        ITProfile iTProfile = new ITProfile()
            .job(DEFAULT_JOB)
            .favLanguages(DEFAULT_FAV_LANGUAGES)
            .favOS(DEFAULT_FAV_OS)
            .gamer(DEFAULT_GAMER)
            .geek(DEFAULT_GEEK)
            .otaku(DEFAULT_OTAKU);
        return iTProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ITProfile createUpdatedEntity() {
        ITProfile iTProfile = new ITProfile()
            .job(UPDATED_JOB)
            .favLanguages(UPDATED_FAV_LANGUAGES)
            .favOS(UPDATED_FAV_OS)
            .gamer(UPDATED_GAMER)
            .geek(UPDATED_GEEK)
            .otaku(UPDATED_OTAKU);
        return iTProfile;
    }

    @BeforeEach
    public void initTest() {
        iTProfileRepository.deleteAll();
        iTProfile = createEntity();
    }

    @Test
    public void createITProfile() throws Exception {
        int databaseSizeBeforeCreate = iTProfileRepository.findAll().size();

        // Create the ITProfile
        restITProfileMockMvc.perform(post("/api/it-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iTProfile)))
            .andExpect(status().isCreated());

        // Validate the ITProfile in the database
        List<ITProfile> iTProfileList = iTProfileRepository.findAll();
        assertThat(iTProfileList).hasSize(databaseSizeBeforeCreate + 1);
        ITProfile testITProfile = iTProfileList.get(iTProfileList.size() - 1);
        assertThat(testITProfile.getJob()).isEqualTo(DEFAULT_JOB);
        assertThat(testITProfile.getFavLanguages()).isEqualTo(DEFAULT_FAV_LANGUAGES);
        assertThat(testITProfile.getFavOS()).isEqualTo(DEFAULT_FAV_OS);
        assertThat(testITProfile.isGamer()).isEqualTo(DEFAULT_GAMER);
        assertThat(testITProfile.isGeek()).isEqualTo(DEFAULT_GEEK);
        assertThat(testITProfile.isOtaku()).isEqualTo(DEFAULT_OTAKU);

        // Validate the ITProfile in Elasticsearch
        verify(mockITProfileSearchRepository, times(1)).save(testITProfile);
    }

    @Test
    public void createITProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = iTProfileRepository.findAll().size();

        // Create the ITProfile with an existing ID
        iTProfile.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restITProfileMockMvc.perform(post("/api/it-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iTProfile)))
            .andExpect(status().isBadRequest());

        // Validate the ITProfile in the database
        List<ITProfile> iTProfileList = iTProfileRepository.findAll();
        assertThat(iTProfileList).hasSize(databaseSizeBeforeCreate);

        // Validate the ITProfile in Elasticsearch
        verify(mockITProfileSearchRepository, times(0)).save(iTProfile);
    }


    @Test
    public void getAllITProfiles() throws Exception {
        // Initialize the database
        iTProfileRepository.save(iTProfile);

        // Get all the iTProfileList
        restITProfileMockMvc.perform(get("/api/it-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iTProfile.getId())))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].favLanguages").value(hasItem(DEFAULT_FAV_LANGUAGES)))
            .andExpect(jsonPath("$.[*].favOS").value(hasItem(DEFAULT_FAV_OS)))
            .andExpect(jsonPath("$.[*].gamer").value(hasItem(DEFAULT_GAMER.booleanValue())))
            .andExpect(jsonPath("$.[*].geek").value(hasItem(DEFAULT_GEEK.booleanValue())))
            .andExpect(jsonPath("$.[*].otaku").value(hasItem(DEFAULT_OTAKU.booleanValue())));
    }
    
    @Test
    public void getITProfile() throws Exception {
        // Initialize the database
        iTProfileRepository.save(iTProfile);

        // Get the iTProfile
        restITProfileMockMvc.perform(get("/api/it-profiles/{id}", iTProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(iTProfile.getId()))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB))
            .andExpect(jsonPath("$.favLanguages").value(DEFAULT_FAV_LANGUAGES))
            .andExpect(jsonPath("$.favOS").value(DEFAULT_FAV_OS))
            .andExpect(jsonPath("$.gamer").value(DEFAULT_GAMER.booleanValue()))
            .andExpect(jsonPath("$.geek").value(DEFAULT_GEEK.booleanValue()))
            .andExpect(jsonPath("$.otaku").value(DEFAULT_OTAKU.booleanValue()));
    }

    @Test
    public void getNonExistingITProfile() throws Exception {
        // Get the iTProfile
        restITProfileMockMvc.perform(get("/api/it-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateITProfile() throws Exception {
        // Initialize the database
        iTProfileRepository.save(iTProfile);

        int databaseSizeBeforeUpdate = iTProfileRepository.findAll().size();

        // Update the iTProfile
        ITProfile updatedITProfile = iTProfileRepository.findById(iTProfile.getId()).get();
        updatedITProfile
            .job(UPDATED_JOB)
            .favLanguages(UPDATED_FAV_LANGUAGES)
            .favOS(UPDATED_FAV_OS)
            .gamer(UPDATED_GAMER)
            .geek(UPDATED_GEEK)
            .otaku(UPDATED_OTAKU);

        restITProfileMockMvc.perform(put("/api/it-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedITProfile)))
            .andExpect(status().isOk());

        // Validate the ITProfile in the database
        List<ITProfile> iTProfileList = iTProfileRepository.findAll();
        assertThat(iTProfileList).hasSize(databaseSizeBeforeUpdate);
        ITProfile testITProfile = iTProfileList.get(iTProfileList.size() - 1);
        assertThat(testITProfile.getJob()).isEqualTo(UPDATED_JOB);
        assertThat(testITProfile.getFavLanguages()).isEqualTo(UPDATED_FAV_LANGUAGES);
        assertThat(testITProfile.getFavOS()).isEqualTo(UPDATED_FAV_OS);
        assertThat(testITProfile.isGamer()).isEqualTo(UPDATED_GAMER);
        assertThat(testITProfile.isGeek()).isEqualTo(UPDATED_GEEK);
        assertThat(testITProfile.isOtaku()).isEqualTo(UPDATED_OTAKU);

        // Validate the ITProfile in Elasticsearch
        verify(mockITProfileSearchRepository, times(1)).save(testITProfile);
    }

    @Test
    public void updateNonExistingITProfile() throws Exception {
        int databaseSizeBeforeUpdate = iTProfileRepository.findAll().size();

        // Create the ITProfile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restITProfileMockMvc.perform(put("/api/it-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iTProfile)))
            .andExpect(status().isBadRequest());

        // Validate the ITProfile in the database
        List<ITProfile> iTProfileList = iTProfileRepository.findAll();
        assertThat(iTProfileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ITProfile in Elasticsearch
        verify(mockITProfileSearchRepository, times(0)).save(iTProfile);
    }

    @Test
    public void deleteITProfile() throws Exception {
        // Initialize the database
        iTProfileRepository.save(iTProfile);

        int databaseSizeBeforeDelete = iTProfileRepository.findAll().size();

        // Delete the iTProfile
        restITProfileMockMvc.perform(delete("/api/it-profiles/{id}", iTProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ITProfile> iTProfileList = iTProfileRepository.findAll();
        assertThat(iTProfileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ITProfile in Elasticsearch
        verify(mockITProfileSearchRepository, times(1)).deleteById(iTProfile.getId());
    }

    @Test
    public void searchITProfile() throws Exception {
        // Initialize the database
        iTProfileRepository.save(iTProfile);
        when(mockITProfileSearchRepository.search(queryStringQuery("id:" + iTProfile.getId())))
            .thenReturn(Collections.singletonList(iTProfile));
        // Search the iTProfile
        restITProfileMockMvc.perform(get("/api/_search/it-profiles?query=id:" + iTProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iTProfile.getId())))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].favLanguages").value(hasItem(DEFAULT_FAV_LANGUAGES)))
            .andExpect(jsonPath("$.[*].favOS").value(hasItem(DEFAULT_FAV_OS)))
            .andExpect(jsonPath("$.[*].gamer").value(hasItem(DEFAULT_GAMER.booleanValue())))
            .andExpect(jsonPath("$.[*].geek").value(hasItem(DEFAULT_GEEK.booleanValue())))
            .andExpect(jsonPath("$.[*].otaku").value(hasItem(DEFAULT_OTAKU.booleanValue())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ITProfile.class);
        ITProfile iTProfile1 = new ITProfile();
        iTProfile1.setId("id1");
        ITProfile iTProfile2 = new ITProfile();
        iTProfile2.setId(iTProfile1.getId());
        assertThat(iTProfile1).isEqualTo(iTProfile2);
        iTProfile2.setId("id2");
        assertThat(iTProfile1).isNotEqualTo(iTProfile2);
        iTProfile1.setId(null);
        assertThat(iTProfile1).isNotEqualTo(iTProfile2);
    }
}
