package fr.inti.xml.web.rest;

import fr.inti.xml.XmLApp;
import fr.inti.xml.domain.Message;
import fr.inti.xml.repository.MessageRepository;
import fr.inti.xml.repository.search.MessageSearchRepository;
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
 * Integration tests for the {@link MessageResource} REST controller.
 */
@SpringBootTest(classes = XmLApp.class)
public class MessageResourceIT {

    private static final String DEFAULT_ID_USER_SENDER = "AAAAAAAAAA";
    private static final String UPDATED_ID_USER_SENDER = "BBBBBBBBBB";

    private static final String DEFAULT_ID_USER_RECIPIENT = "AAAAAAAAAA";
    private static final String UPDATED_ID_USER_RECIPIENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_MESSAGE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MESSAGE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_READ_MESSAGE = false;
    private static final Boolean UPDATED_READ_MESSAGE = true;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * This repository is mocked in the fr.inti.xml.repository.search test package.
     *
     * @see fr.inti.xml.repository.search.MessageSearchRepositoryMockConfiguration
     */
    @Autowired
    private MessageSearchRepository mockMessageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restMessageMockMvc;

    private Message message;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessageResource messageResource = new MessageResource(messageRepository, mockMessageSearchRepository);
        this.restMessageMockMvc = MockMvcBuilders.standaloneSetup(messageResource)
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
    public static Message createEntity() {
        Message message = new Message()
            .idUserSender(DEFAULT_ID_USER_SENDER)
            .idUserRecipient(DEFAULT_ID_USER_RECIPIENT)
            .contentMessage(DEFAULT_CONTENT_MESSAGE)
            .dateMessage(DEFAULT_DATE_MESSAGE)
            .readMessage(DEFAULT_READ_MESSAGE);
        return message;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createUpdatedEntity() {
        Message message = new Message()
            .idUserSender(UPDATED_ID_USER_SENDER)
            .idUserRecipient(UPDATED_ID_USER_RECIPIENT)
            .contentMessage(UPDATED_CONTENT_MESSAGE)
            .dateMessage(UPDATED_DATE_MESSAGE)
            .readMessage(UPDATED_READ_MESSAGE);
        return message;
    }

    @BeforeEach
    public void initTest() {
        messageRepository.deleteAll();
        message = createEntity();
    }

    @Test
    public void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getIdUserSender()).isEqualTo(DEFAULT_ID_USER_SENDER);
        assertThat(testMessage.getIdUserRecipient()).isEqualTo(DEFAULT_ID_USER_RECIPIENT);
        assertThat(testMessage.getContentMessage()).isEqualTo(DEFAULT_CONTENT_MESSAGE);
        assertThat(testMessage.getDateMessage()).isEqualTo(DEFAULT_DATE_MESSAGE);
        assertThat(testMessage.isReadMessage()).isEqualTo(DEFAULT_READ_MESSAGE);

        // Validate the Message in Elasticsearch
        verify(mockMessageSearchRepository, times(1)).save(testMessage);
    }

    @Test
    public void createMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message with an existing ID
        message.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);

        // Validate the Message in Elasticsearch
        verify(mockMessageSearchRepository, times(0)).save(message);
    }


    @Test
    public void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        // Get all the messageList
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId())))
            .andExpect(jsonPath("$.[*].idUserSender").value(hasItem(DEFAULT_ID_USER_SENDER)))
            .andExpect(jsonPath("$.[*].idUserRecipient").value(hasItem(DEFAULT_ID_USER_RECIPIENT)))
            .andExpect(jsonPath("$.[*].contentMessage").value(hasItem(DEFAULT_CONTENT_MESSAGE)))
            .andExpect(jsonPath("$.[*].dateMessage").value(hasItem(DEFAULT_DATE_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].readMessage").value(hasItem(DEFAULT_READ_MESSAGE.booleanValue())));
    }
    
    @Test
    public void getMessage() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId()))
            .andExpect(jsonPath("$.idUserSender").value(DEFAULT_ID_USER_SENDER))
            .andExpect(jsonPath("$.idUserRecipient").value(DEFAULT_ID_USER_RECIPIENT))
            .andExpect(jsonPath("$.contentMessage").value(DEFAULT_CONTENT_MESSAGE))
            .andExpect(jsonPath("$.dateMessage").value(DEFAULT_DATE_MESSAGE.toString()))
            .andExpect(jsonPath("$.readMessage").value(DEFAULT_READ_MESSAGE.booleanValue()));
    }

    @Test
    public void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateMessage() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).get();
        updatedMessage
            .idUserSender(UPDATED_ID_USER_SENDER)
            .idUserRecipient(UPDATED_ID_USER_RECIPIENT)
            .contentMessage(UPDATED_CONTENT_MESSAGE)
            .dateMessage(UPDATED_DATE_MESSAGE)
            .readMessage(UPDATED_READ_MESSAGE);

        restMessageMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMessage)))
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getIdUserSender()).isEqualTo(UPDATED_ID_USER_SENDER);
        assertThat(testMessage.getIdUserRecipient()).isEqualTo(UPDATED_ID_USER_RECIPIENT);
        assertThat(testMessage.getContentMessage()).isEqualTo(UPDATED_CONTENT_MESSAGE);
        assertThat(testMessage.getDateMessage()).isEqualTo(UPDATED_DATE_MESSAGE);
        assertThat(testMessage.isReadMessage()).isEqualTo(UPDATED_READ_MESSAGE);

        // Validate the Message in Elasticsearch
        verify(mockMessageSearchRepository, times(1)).save(testMessage);
    }

    @Test
    public void updateNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Create the Message

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Message in Elasticsearch
        verify(mockMessageSearchRepository, times(0)).save(message);
    }

    @Test
    public void deleteMessage() throws Exception {
        // Initialize the database
        messageRepository.save(message);

        int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Delete the message
        restMessageMockMvc.perform(delete("/api/messages/{id}", message.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Message in Elasticsearch
        verify(mockMessageSearchRepository, times(1)).deleteById(message.getId());
    }

    @Test
    public void searchMessage() throws Exception {
        // Initialize the database
        messageRepository.save(message);
        when(mockMessageSearchRepository.search(queryStringQuery("id:" + message.getId())))
            .thenReturn(Collections.singletonList(message));
        // Search the message
        restMessageMockMvc.perform(get("/api/_search/messages?query=id:" + message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId())))
            .andExpect(jsonPath("$.[*].idUserSender").value(hasItem(DEFAULT_ID_USER_SENDER)))
            .andExpect(jsonPath("$.[*].idUserRecipient").value(hasItem(DEFAULT_ID_USER_RECIPIENT)))
            .andExpect(jsonPath("$.[*].contentMessage").value(hasItem(DEFAULT_CONTENT_MESSAGE)))
            .andExpect(jsonPath("$.[*].dateMessage").value(hasItem(DEFAULT_DATE_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].readMessage").value(hasItem(DEFAULT_READ_MESSAGE.booleanValue())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = new Message();
        message1.setId("id1");
        Message message2 = new Message();
        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);
        message2.setId("id2");
        assertThat(message1).isNotEqualTo(message2);
        message1.setId(null);
        assertThat(message1).isNotEqualTo(message2);
    }
}
