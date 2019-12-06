package fr.inti.xml.repository.search;
import fr.inti.xml.domain.ITProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ITProfile} entity.
 */
public interface ITProfileSearchRepository extends ElasticsearchRepository<ITProfile, String> {
}
