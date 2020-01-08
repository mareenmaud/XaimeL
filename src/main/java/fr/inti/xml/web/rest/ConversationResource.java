package fr.inti.xml.web.rest;

import fr.inti.xml.domain.Conversation;
import fr.inti.xml.domain.Message;
import fr.inti.xml.repository.ConversationRepository;

import fr.inti.xml.service.UserService;
import fr.inti.xml.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;



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



    public ConversationResource(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;

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

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }



    // ************** mes webservices
    // créer une conversation à partir des id des deux utilisateurs
    //http://localhost:8082/api/createconversation?id_user1=***&id_user2=***
    @PostMapping("/createconversation")
    public ResponseEntity<Conversation> createConversation(@RequestParam String id_user1,@RequestParam String id_user2) throws URISyntaxException {
        Conversation conversation = new Conversation();
        conversation.setIdUser1(id_user1);
        conversation.setIdUser2(id_user2);
        Conversation conversationexist=null;
        List<Conversation> conversations1= conversationRepository.findAllByIdUser1(id_user1);
        List<Conversation> conversations2=conversationRepository.findAllByIdUser2(id_user2);
        for (Conversation i : conversations1){
            if (conversations2.contains(i)) {
                conversationexist = conversation;
            }
        }
        List<Conversation> conversations3= conversationRepository.findAllByIdUser1(id_user2);
        List<Conversation> conversations4=conversationRepository.findAllByIdUser2(id_user1);
        for (Conversation i : conversations3){
            if (conversations4.contains(i)) {
                conversationexist = conversation;
            }
        }
    log.debug("REST request to save Conversation : {}", conversation);
        if (conversation.getId() != null) {
            throw new BadRequestAlertException("A new conversation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        else if( conversationexist!=null ){
            throw new BadRequestAlertException("A new conversation cannot already have an ID", ENTITY_NAME, "these users have already a conversation");


        }
        Conversation result = conversationRepository.save(conversation);
        return ResponseEntity.created(new URI("/api/conversations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
// trouver une conversation a partir des id des deux utilisateurs
    // http://localhost:8082/api/conversationuser?id_user1=***&id_user2=***
    @GetMapping("/conversationuser")
    public ResponseEntity<Conversation> getConversationUser(@RequestParam String id_user1,@RequestParam String id_user2) {
        Conversation conversationtampon=null;
        List<Conversation> conversations1= conversationRepository.findAllByIdUser1(id_user1);
        List<Conversation> conversations2=conversationRepository.findAllByIdUser2(id_user2);
        for (Conversation i : conversations1){
            for (Conversation j : conversations2) {
                if(i.equals(j)){
                    conversationtampon=i;
                }

            }
        }
        List<Conversation> conversations3= conversationRepository.findAllByIdUser1(id_user2);
        List<Conversation> conversations4=conversationRepository.findAllByIdUser2(id_user1);
        for (Conversation i : conversations3){
            for (Conversation j : conversations4) {
                if(i.equals(j)){
                    conversationtampon=i;
                }

            }
        }

        Optional<Conversation> conversation = conversationRepository.findById(conversationtampon.getId());
        return ResponseUtil.wrapOrNotFound(conversation);
    }
    //********* ajouter un message
    //http://localhost:8082/api/ajoutermessage

    @PutMapping("/ajoutermessage")
    public ResponseEntity<Conversation> ajouterMessage(@RequestBody Message message) throws URISyntaxException {
        Conversation conversationtampon=null;
        conversationtampon=conversationRepository.findOneById(message.getConversation().getId());
if( conversationtampon.getId()==null){
    throw new BadRequestAlertException("Invalid request", ENTITY_NAME, "the conversation doesn't exist");

}
        Set<Message> messages= conversationtampon.getMessages();
    if(messages.contains(message.getId())){
        conversationtampon.removeMessages(message);

        }

        conversationtampon.addMessages(message);

        Conversation result = conversationRepository.save(conversationtampon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conversationtampon.getId().toString()))
            .body(result);
    }

}
