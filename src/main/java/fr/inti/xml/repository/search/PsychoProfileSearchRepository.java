package fr.inti.xml.repository.search;
import fr.inti.xml.domain.PsychoProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PsychoProfile} entity.
 */
public interface PsychoProfileSearchRepository extends ElasticsearchRepository<PsychoProfile, String> {
}
