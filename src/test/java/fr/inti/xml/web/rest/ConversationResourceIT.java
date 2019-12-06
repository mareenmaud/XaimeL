package fr.inti.xml.web.rest;

import fr.inti.xml.XmLApp;
import fr.inti.xml.domain.Conversation;
import fr.inti.xml.repository.ConversationRepository;
import fr.inti.xml.repository.search.ConversationSearchRepository;
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
 * Integration tests for the {@link ConversationResource} REST controller.
 */
@SpringBootTest(classes = XmLApp.class)
public class ConversationResourceIT {

    private static final Integer DEFAULT_ID_USERS = 1;
    private static final Integer UPDATED_ID_USERS = 2;

    @Autowired
    private ConversationRepository conversationRepository;

    /**
     * This repository is mocked in the fr.inti.xml.repository.search test package.
     *
     * @see fr.inti.xml.repository.search.ConversationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConversationSearchRepository mockConversationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restConversationMockMvc;

    private Conversation conversation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConversationResource conversationResource = new ConversationResource(conversationRepository, mockConversationSearchRepository);
        this.restConversationMockMvc = MockMvcBuilders.standaloneSetup(conversationResource)
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
    public static Conversation createEntity() {
        Conversation conversation = new Conversation()
            .idUsers(DEFAULT_ID_USERS);
        return conversation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conversation createUpdatedEntity() {
        Conversation conversation = new Conversation()
            .idUsers(UPDATED_ID_USERS);
        return conversation;
    }

    @BeforeEach
    public void initTest() {
        conversationRepository.deleteAll();
        conversation = createEntity();
    }

    @Test
    public void createConversation() throws Exception {
        int databaseSizeBeforeCreate = conversationRepository.findAll().size();

        // Create the Conversation
        restConversationMockMvc.perform(post("/api/conversations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conversation)))
            .andExpect(status().isCreated());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeCreate + 1);
        Conversation testConversation = conversationList.get(conversationList.size() - 1);
        assertThat(testConversation.getIdUsers()).isEqualTo(DEFAULT_ID_USERS);

        // Validate the Conversation in Elasticsearch
        verify(mockConversationSearchRepository, times(1)).save(testConversation);
    }

    @Test
    public void createConversationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = conversationRepository.findAll().size();

        // Create the Conversation with an existing ID
        conversation.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restConversationMockMvc.perform(post("/api/conversations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conversation)))
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Conversation in Elasticsearch
        verify(mockConversationSearchRepository, times(0)).save(conversation);
    }


    @Test
    public void getAllConversations() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);

        // Get all the conversationList
        restConversationMockMvc.perform(get("/api/conversations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversation.getId())))
            .andExpect(jsonPath("$.[*].idUsers").value(hasItem(DEFAULT_ID_USERS)));
    }
    
    @Test
    public void getConversation() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);

        // Get the conversation
        restConversationMockMvc.perform(get("/api/conversations/{id}", conversation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conversation.getId()))
            .andExpect(jsonPath("$.idUsers").value(DEFAULT_ID_USERS));
    }

    @Test
    public void getNonExistingConversation() throws Exception {
        // Get the conversation
        restConversationMockMvc.perform(get("/api/conversations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateConversation() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);

        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();

        // Update the conversation
        Conversation updatedConversation = conversationRepository.findById(conversation.getId()).get();
        updatedConversation
            .idUsers(UPDATED_ID_USERS);

        restConversationMockMvc.perform(put("/api/conversations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConversation)))
            .andExpect(status().isOk());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
        Conversation testConversation = conversationList.get(conversationList.size() - 1);
        assertThat(testConversation.getIdUsers()).isEqualTo(UPDATED_ID_USERS);

        // Validate the Conversation in Elasticsearch
        verify(mockConversationSearchRepository, times(1)).save(testConversation);
    }

    @Test
    public void updateNonExistingConversation() throws Exception {
        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();

        // Create the Conversation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversationMockMvc.perform(put("/api/conversations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conversation)))
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Conversation in Elasticsearch
        verify(mockConversationSearchRepository, times(0)).save(conversation);
    }

    @Test
    public void deleteConversation() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);

        int databaseSizeBeforeDelete = conversationRepository.findAll().size();

        // Delete the conversation
        restConversationMockMvc.perform(delete("/api/conversations/{id}", conversation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Conversation in Elasticsearch
        verify(mockConversationSearchRepository, times(1)).deleteById(conversation.getId());
    }

    @Test
    public void searchConversation() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);
        when(mockConversationSearchRepository.search(queryStringQuery("id:" + conversation.getId())))
            .thenReturn(Collections.singletonList(conversation));
        // Search the conversation
        restConversationMockMvc.perform(get("/api/_search/conversations?query=id:" + conversation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversation.getId())))
            .andExpect(jsonPath("$.[*].idUsers").value(hasItem(DEFAULT_ID_USERS)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conversation.class);
        Conversation conversation1 = new Conversation();
        conversation1.setId("id1");
        Conversation conversation2 = new Conversation();
        conversation2.setId(conversation1.getId());
        assertThat(conversation1).isEqualTo(conversation2);
        conversation2.setId("id2");
        assertThat(conversation1).isNotEqualTo(conversation2);
        conversation1.setId(null);
        assertThat(conversation1).isNotEqualTo(conversation2);
    }
}
