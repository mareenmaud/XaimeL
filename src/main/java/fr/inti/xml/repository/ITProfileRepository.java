package fr.inti.xml.repository;
import fr.inti.xml.domain.ITProfile;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the ITProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ITProfileRepository extends MongoRepository<ITProfile, String> {

}
