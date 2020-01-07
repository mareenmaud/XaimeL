package fr.inti.xml.repository;
    import fr.inti.xml.domain.Conversation;
    import org.springframework.data.mongodb.repository.MongoRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;


/**
 * Spring Data MongoDB repository for the Conversation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {

    List<Conversation> findAllByIdUser1(String user_id1);
    List<Conversation> findAllByIdUser2(String user_id2);

}
