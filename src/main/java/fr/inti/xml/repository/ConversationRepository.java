package fr.inti.xml.repository;
import fr.inti.xml.domain.Conversation;
import fr.inti.xml.web.rest.AuditResource;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Conversation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
 Conversation findConversationByUsers(String user_id1,String user_id2);

}
