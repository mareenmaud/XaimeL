package fr.inti.xml.web.rest;

import fr.inti.xml.domain.Conversation;
import fr.inti.xml.repository.ConversationRepository;
import fr.inti.xml.repository.search.ConversationSearchRepository;
import fr.inti.xml.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link fr.inti.xml.domain.Conversation}.
 */
@RestController
@RequestMapping("/api")
public class ConversationResource {

    private final Logger log = LoggerFactory.getLogger(ConversationResource.class);

    private static final String ENTITY_NAME = "conversation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConversationRepository conversationRepository;

    private final ConversationSearchRepository conversationSearchRepository;

    public ConversationResource(ConversationRepository conversationRepository, ConversationSearchRepository conversationSearchRepository) {
        this.conversationRepository = conversationRepository;
        this.conversationSearchRepository = conversationSearchRepository;
    }

    /**
     * {@code POST  /conversations} : Create a new conversation.
     *
     * @param conversation the conversation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conversation, or with status {@code 400 (Bad Request)} if the conversation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conversations")
    public ResponseEntity<Conversation> createConversation(@RequestBody Conversation conversation) throws URISyntaxException {
        log.debug("REST request to save Conversation : {}", conversation);
        if (conversation.getId() != null) {
            throw new BadRequestAlertException("A new conversation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conversation result = conversationRepository.save(conversation);
        conversationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/conversations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conversations} : Updates an existing conversation.
     *
     * @param conversation the conversation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversation,
     * or with status {@code 400 (Bad Request)} if the conversation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conversation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conversations")
    public ResponseEntity<Conversation> updateConversation(@RequestBody Conversation conversation) throws URISyntaxException {
        log.debug("REST request to update Conversation : {}", conversation);
        if (conversation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Conversation result = conversationRepository.save(conversation);
        conversationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conversation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /conversations} : get all the conversations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conversations in body.
     */
    @GetMapping("/conversations")
    public List<Conversation> getAllConversations() {
        log.debug("REST request to get all Conversations");
        return conversationRepository.findAll();
    }

    /**
     * {@code GET  /conversations/:id} : get the "id" conversation.
     *
     * @param id the id of the conversation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conversation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conversations/{id}")
    public ResponseEntity<Conversation> getConversation(@PathVariable String id) {
        log.debug("REST request to get Conversation : {}", id);
        Optional<Conversation> conversation = conversationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(conversation);
    }

    /**
     * {@code DELETE  /conversations/:id} : delete the "id" conversation.
     *
     * @param id the id of the conversation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conversations/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable String id) {
        log.debug("REST request to delete Conversation : {}", id);
        conversationRepository.deleteById(id);
        conversationSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/conversations?query=:query} : search for the conversation corresponding
     * to the query.
     *
     * @param query the query of the conversation search.
     * @return the result of the search.
     */
    @GetMapping("/_search/conversations")
    public List<Conversation> searchConversations(@RequestParam String query) {
        log.debug("REST request to search Conversations for query {}", query);
        return StreamSupport
            .stream(conversationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
