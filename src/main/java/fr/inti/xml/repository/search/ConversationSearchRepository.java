package fr.inti.xml.repository.search;
import fr.inti.xml.domain.Conversation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Conversation} entity.
 */
public interface ConversationSearchRepository extends ElasticsearchRepository<Conversation, String> {
}
