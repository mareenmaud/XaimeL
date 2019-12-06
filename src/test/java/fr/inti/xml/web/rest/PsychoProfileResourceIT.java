package fr.inti.xml.web.rest;

import fr.inti.xml.XmLApp;
import fr.inti.xml.domain.PsychoProfile;
import fr.inti.xml.repository.PsychoProfileRepository;
import fr.inti.xml.repository.search.PsychoProfileSearchRepository;
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
 * Integration tests for the {@link PsychoProfileResource} REST controller.
 */
@SpringBootTest(classes = XmLApp.class)
public class PsychoProfileResourceIT {

    private static final String DEFAULT_SUMMARY_PROFILE = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY_PROFILE = "BBBBBBBBBB";

    private static final Double DEFAULT_JUNG_VALUES = 1D;
    private static final Double UPDATED_JUNG_VALUES = 2D;

    @Autowired
    private PsychoProfileRepository psychoProfileRepository;

    /**
     * This repository is mocked in the fr.inti.xml.repository.search test package.
     *
     * @see fr.inti.xml.repository.search.PsychoProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private PsychoProfileSearchRepository mockPsychoProfileSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPsychoProfileMockMvc;

    private PsychoProfile psychoProfile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PsychoProfileResource psychoProfileResource = new PsychoProfileResource(psychoProfileRepository, mockPsychoProfileSearchRepository);
        this.restPsychoProfileMockMvc = MockMvcBuilders.standaloneSetup(psychoProfileResource)
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
    public static PsychoProfile createEntity() {
        PsychoProfile psychoProfile = new PsychoProfile()
            .summaryProfile(DEFAULT_SUMMARY_PROFILE)
            .jungValues(DEFAULT_JUNG_VALUES);
        return psychoProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PsychoProfile createUpdatedEntity() {
        PsychoProfile psychoProfile = new PsychoProfile()
            .summaryProfile(UPDATED_SUMMARY_PROFILE)
            .jungValues(UPDATED_JUNG_VALUES);
        return psychoProfile;
    }

    @BeforeEach
    public void initTest() {
        psychoProfileRepository.deleteAll();
        psychoProfile = createEntity();
    }

    @Test
    public void createPsychoProfile() throws Exception {
        int databaseSizeBeforeCreate = psychoProfileRepository.findAll().size();

        // Create the PsychoProfile
        restPsychoProfileMockMvc.perform(post("/api/psycho-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(psychoProfile)))
            .andExpect(status().isCreated());

        // Validate the PsychoProfile in the database
        List<PsychoProfile> psychoProfileList = psychoProfileRepository.findAll();
        assertThat(psychoProfileList).hasSize(databaseSizeBeforeCreate + 1);
        PsychoProfile testPsychoProfile = psychoProfileList.get(psychoProfileList.size() - 1);
        assertThat(testPsychoProfile.getSummaryProfile()).isEqualTo(DEFAULT_SUMMARY_PROFILE);
        assertThat(testPsychoProfile.getJungValues()).isEqualTo(DEFAULT_JUNG_VALUES);

        // Validate the PsychoProfile in Elasticsearch
        verify(mockPsychoProfileSearchRepository, times(1)).save(testPsychoProfile);
    }

    @Test
    public void createPsychoProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = psychoProfileRepository.findAll().size();

        // Create the PsychoProfile with an existing ID
        psychoProfile.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPsychoProfileMockMvc.perform(post("/api/psycho-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(psychoProfile)))
            .andExpect(status().isBadRequest());

        // Validate the PsychoProfile in the database
        List<PsychoProfile> psychoProfileList = psychoProfileRepository.findAll();
        assertThat(psychoProfileList).hasSize(databaseSizeBeforeCreate);

        // Validate the PsychoProfile in Elasticsearch
        verify(mockPsychoProfileSearchRepository, times(0)).save(psychoProfile);
    }


    @Test
    public void getAllPsychoProfiles() throws Exception {
        // Initialize the database
        psychoProfileRepository.save(psychoProfile);

        // Get all the psychoProfileList
        restPsychoProfileMockMvc.perform(get("/api/psycho-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(psychoProfile.getId())))
            .andExpect(jsonPath("$.[*].summaryProfile").value(hasItem(DEFAULT_SUMMARY_PROFILE)))
            .andExpect(jsonPath("$.[*].jungValues").value(hasItem(DEFAULT_JUNG_VALUES.doubleValue())));
    }
    
    @Test
    public void getPsychoProfile() throws Exception {
        // Initialize the database
        psychoProfileRepository.save(psychoProfile);

        // Get the psychoProfile
        restPsychoProfileMockMvc.perform(get("/api/psycho-profiles/{id}", psychoProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(psychoProfile.getId()))
            .andExpect(jsonPath("$.summaryProfile").value(DEFAULT_SUMMARY_PROFILE))
            .andExpect(jsonPath("$.jungValues").value(DEFAULT_JUNG_VALUES.doubleValue()));
    }

    @Test
    public void getNonExistingPsychoProfile() throws Exception {
        // Get the psychoProfile
        restPsychoProfileMockMvc.perform(get("/api/psycho-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePsychoProfile() throws Exception {
        // Initialize the database
        psychoProfileRepository.save(psychoProfile);

        int databaseSizeBeforeUpdate = psychoProfileRepository.findAll().size();

        // Update the psychoProfile
        PsychoProfile updatedPsychoProfile = psychoProfileRepository.findById(psychoProfile.getId()).get();
        updatedPsychoProfile
            .summaryProfile(UPDATED_SUMMARY_PROFILE)
            .jungValues(UPDATED_JUNG_VALUES);

        restPsychoProfileMockMvc.perform(put("/api/psycho-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPsychoProfile)))
            .andExpect(status().isOk());

        // Validate the PsychoProfile in the database
        List<PsychoProfile> psychoProfileList = psychoProfileRepository.findAll();
        assertThat(psychoProfileList).hasSize(databaseSizeBeforeUpdate);
        PsychoProfile testPsychoProfile = psychoProfileList.get(psychoProfileList.size() - 1);
        assertThat(testPsychoProfile.getSummaryProfile()).isEqualTo(UPDATED_SUMMARY_PROFILE);
        assertThat(testPsychoProfile.getJungValues()).isEqualTo(UPDATED_JUNG_VALUES);

        // Validate the PsychoProfile in Elasticsearch
        verify(mockPsychoProfileSearchRepository, times(1)).save(testPsychoProfile);
    }

    @Test
    public void updateNonExistingPsychoProfile() throws Exception {
        int databaseSizeBeforeUpdate = psychoProfileRepository.findAll().size();

        // Create the PsychoProfile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPsychoProfileMockMvc.perform(put("/api/psycho-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(psychoProfile)))
            .andExpect(status().isBadRequest());

        // Validate the PsychoProfile in the database
        List<PsychoProfile> psychoProfileList = psychoProfileRepository.findAll();
        assertThat(psychoProfileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PsychoProfile in Elasticsearch
        verify(mockPsychoProfileSearchRepository, times(0)).save(psychoProfile);
    }

    @Test
    public void deletePsychoProfile() throws Exception {
        // Initialize the database
        psychoProfileRepository.save(psychoProfile);

        int databaseSizeBeforeDelete = psychoProfileRepository.findAll().size();

        // Delete the psychoProfile
        restPsychoProfileMockMvc.perform(delete("/api/psycho-profiles/{id}", psychoProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PsychoProfile> psychoProfileList = psychoProfileRepository.findAll();
        assertThat(psychoProfileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PsychoProfile in Elasticsearch
        verify(mockPsychoProfileSearchRepository, times(1)).deleteById(psychoProfile.getId());
    }

    @Test
    public void searchPsychoProfile() throws Exception {
        // Initialize the database
        psychoProfileRepository.save(psychoProfile);
        when(mockPsychoProfileSearchRepository.search(queryStringQuery("id:" + psychoProfile.getId())))
            .thenReturn(Collections.singletonList(psychoProfile));
        // Search the psychoProfile
        restPsychoProfileMockMvc.perform(get("/api/_search/psycho-profiles?query=id:" + psychoProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(psychoProfile.getId())))
            .andExpect(jsonPath("$.[*].summaryProfile").value(hasItem(DEFAULT_SUMMARY_PROFILE)))
            .andExpect(jsonPath("$.[*].jungValues").value(hasItem(DEFAULT_JUNG_VALUES.doubleValue())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PsychoProfile.class);
        PsychoProfile psychoProfile1 = new PsychoProfile();
        psychoProfile1.setId("id1");
        PsychoProfile psychoProfile2 = new PsychoProfile();
        psychoProfile2.setId(psychoProfile1.getId());
        assertThat(psychoProfile1).isEqualTo(psychoProfile2);
        psychoProfile2.setId("id2");
        assertThat(psychoProfile1).isNotEqualTo(psychoProfile2);
        psychoProfile1.setId(null);
        assertThat(psychoProfile1).isNotEqualTo(psychoProfile2);
    }
}
