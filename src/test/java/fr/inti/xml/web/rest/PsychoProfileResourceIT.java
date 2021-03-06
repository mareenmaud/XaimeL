package fr.inti.xml.web.rest;

import fr.inti.xml.XmLApp;
import fr.inti.xml.domain.PsychoProfile;
import fr.inti.xml.repository.PsychoProfileRepository;

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

    private static final Double DEFAULT_JUNG_VALUE_1 = 1D;
    private static final Double UPDATED_JUNG_VALUE_1 = 2D;

    private static final Double DEFAULT_JUNG_VALUE_2 = 1D;
    private static final Double UPDATED_JUNG_VALUE_2 = 2D;

    private static final Double DEFAULT_JUNG_VALUE_3 = 1D;
    private static final Double UPDATED_JUNG_VALUE_3 = 2D;

    private static final Double DEFAULT_JUNG_VALUE_4 = 1D;
    private static final Double UPDATED_JUNG_VALUE_4 = 2D;

    @Autowired
    private PsychoProfileRepository psychoProfileRepository;



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
        final PsychoProfileResource psychoProfileResource = new PsychoProfileResource(psychoProfileRepository);
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
            .jungValue1(DEFAULT_JUNG_VALUE_1)
            .jungValue2(DEFAULT_JUNG_VALUE_2)
            .jungValue3(DEFAULT_JUNG_VALUE_3)
            .jungValue4(DEFAULT_JUNG_VALUE_4);
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
            .jungValue1(UPDATED_JUNG_VALUE_1)
            .jungValue2(UPDATED_JUNG_VALUE_2)
            .jungValue3(UPDATED_JUNG_VALUE_3)
            .jungValue4(UPDATED_JUNG_VALUE_4);
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
        assertThat(testPsychoProfile.getJungValue1()).isEqualTo(DEFAULT_JUNG_VALUE_1);
        assertThat(testPsychoProfile.getJungValue2()).isEqualTo(DEFAULT_JUNG_VALUE_2);
        assertThat(testPsychoProfile.getJungValue3()).isEqualTo(DEFAULT_JUNG_VALUE_3);
        assertThat(testPsychoProfile.getJungValue4()).isEqualTo(DEFAULT_JUNG_VALUE_4);

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
            .andExpect(jsonPath("$.[*].jungValue1").value(hasItem(DEFAULT_JUNG_VALUE_1.doubleValue())))
            .andExpect(jsonPath("$.[*].jungValue2").value(hasItem(DEFAULT_JUNG_VALUE_2.doubleValue())))
            .andExpect(jsonPath("$.[*].jungValue3").value(hasItem(DEFAULT_JUNG_VALUE_3.doubleValue())))
            .andExpect(jsonPath("$.[*].jungValue4").value(hasItem(DEFAULT_JUNG_VALUE_4.doubleValue())));
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
            .andExpect(jsonPath("$.jungValue1").value(DEFAULT_JUNG_VALUE_1.doubleValue()))
            .andExpect(jsonPath("$.jungValue2").value(DEFAULT_JUNG_VALUE_2.doubleValue()))
            .andExpect(jsonPath("$.jungValue3").value(DEFAULT_JUNG_VALUE_3.doubleValue()))
            .andExpect(jsonPath("$.jungValue4").value(DEFAULT_JUNG_VALUE_4.doubleValue()));
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
            .jungValue1(UPDATED_JUNG_VALUE_1)
            .jungValue2(UPDATED_JUNG_VALUE_2)
            .jungValue3(UPDATED_JUNG_VALUE_3)
            .jungValue4(UPDATED_JUNG_VALUE_4);

        restPsychoProfileMockMvc.perform(put("/api/psycho-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPsychoProfile)))
            .andExpect(status().isOk());

        // Validate the PsychoProfile in the database
        List<PsychoProfile> psychoProfileList = psychoProfileRepository.findAll();
        assertThat(psychoProfileList).hasSize(databaseSizeBeforeUpdate);
        PsychoProfile testPsychoProfile = psychoProfileList.get(psychoProfileList.size() - 1);
        assertThat(testPsychoProfile.getSummaryProfile()).isEqualTo(UPDATED_SUMMARY_PROFILE);
        assertThat(testPsychoProfile.getJungValue1()).isEqualTo(UPDATED_JUNG_VALUE_1);
        assertThat(testPsychoProfile.getJungValue2()).isEqualTo(UPDATED_JUNG_VALUE_2);
        assertThat(testPsychoProfile.getJungValue3()).isEqualTo(UPDATED_JUNG_VALUE_3);
        assertThat(testPsychoProfile.getJungValue4()).isEqualTo(UPDATED_JUNG_VALUE_4);

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
