package fr.inti.xml.web.rest;

import fr.inti.xml.domain.Conversation;
import fr.inti.xml.domain.Message;
import fr.inti.xml.repository.MessageRepository;

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

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;



/**
 * REST controller for managing {@link fr.inti.xml.domain.Message}.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private static final String ENTITY_NAME = "message";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageRepository messageRepository;



    public MessageResource(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

    }

    /**
     * {@code POST  /messages} : Create a new message.
     *
     * @param message the message to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new message, or with status {@code 400 (Bad Request)} if the message has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);
        if (message.getId() != null) {
            throw new BadRequestAlertException("A new message cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Message result = messageRepository.save(message);

        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /messages} : Updates an existing message.
     *
     * @param message the message to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated message,
     * or with status {@code 400 (Bad Request)} if the message is not valid,
     * or with status {@code 500 (Internal Server Error)} if the message couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/messages")
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to update Message : {}", message);
        if (message.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Message result = messageRepository.save(message);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, message.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /messages} : get all the messages.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messages in body.
     */
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        log.debug("REST request to get all Messages");
        return messageRepository.findAll();
    }

    /**
     * {@code GET  /messages/:id} : get the "id" message.
     *
     * @param id the id of the message to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the message, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable String id) {
        log.debug("REST request to get Message : {}", id);
        Optional<Message> message = messageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(message);
    }

    /**
     * {@code DELETE  /messages/:id} : delete the "id" message.
     *
     * @param id the id of the message to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String id) {
        log.debug("REST request to delete Message : {}", id);
        messageRepository.deleteById(id);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }


// mes webservices
    //*********** methode pour créer un message
    //http://localhost:8082/api/createmessage
    //{"idUserSender":"adri", "idUserRecipient":"sam", "contentMessage":"bien et toi?","id":"5e148b1f7d74c70778b88734","idUser1":"sam","idUser2":"adri"}
    @PostMapping("/createmessage")
    public ResponseEntity<Message> createMessage(@RequestBody Map<String,String> json) throws URISyntaxException {
    Conversation conversation=new Conversation();
    conversation.setId(json.get("id"));
    conversation.setIdUser1(json.get("idUser1"));
        conversation.setIdUser2(json.get("idUser2"));
        Message messagef=new Message();
        messagef.setIdUserSender(json.get("idUserSender"));
        messagef.setIdUserRecipient(json.get("idUserRecipient"));
        messagef.setContentMessage(json.get("contentMessage"));
        messagef.setConversation(conversation);
        messagef.setDateMessage(Instant.now());
        messagef.setReadMessage(false);

        log.debug("REST request to save Message : {}", messagef);
        if (messagef.getId() != null) {
            throw new BadRequestAlertException("A new message cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Message result = messageRepository.save(messagef);

        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

//********** message lu
    //http://localhost:8082/api/messagelu?id_message=******
@PutMapping("/messagelu")
public ResponseEntity<Message> messagelu(@RequestParam String id_message) throws URISyntaxException {

    Message message=messageRepository.findOneById(id_message);
    log.debug("REST request to update Message : {}", message);
    if (message.getId() == null) {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    message.setReadMessage(true);
    messageRepository.delete(message);
    Message result = messageRepository.save(message);

    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, message.getId().toString()))
        .body(result);
}

}
