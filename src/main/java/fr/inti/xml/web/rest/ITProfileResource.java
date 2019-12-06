package fr.inti.xml.web.rest;

import fr.inti.xml.domain.ITProfile;
import fr.inti.xml.repository.ITProfileRepository;
import fr.inti.xml.repository.search.ITProfileSearchRepository;
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
 * REST controller for managing {@link fr.inti.xml.domain.ITProfile}.
 */
@RestController
@RequestMapping("/api")
public class ITProfileResource {

    private final Logger log = LoggerFactory.getLogger(ITProfileResource.class);

    private static final String ENTITY_NAME = "iTProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ITProfileRepository iTProfileRepository;

    private final ITProfileSearchRepository iTProfileSearchRepository;

    public ITProfileResource(ITProfileRepository iTProfileRepository, ITProfileSearchRepository iTProfileSearchRepository) {
        this.iTProfileRepository = iTProfileRepository;
        this.iTProfileSearchRepository = iTProfileSearchRepository;
    }

    /**
     * {@code POST  /it-profiles} : Create a new iTProfile.
     *
     * @param iTProfile the iTProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iTProfile, or with status {@code 400 (Bad Request)} if the iTProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/it-profiles")
    public ResponseEntity<ITProfile> createITProfile(@RequestBody ITProfile iTProfile) throws URISyntaxException {
        log.debug("REST request to save ITProfile : {}", iTProfile);
        if (iTProfile.getId() != null) {
            throw new BadRequestAlertException("A new iTProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ITProfile result = iTProfileRepository.save(iTProfile);
        iTProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/it-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /it-profiles} : Updates an existing iTProfile.
     *
     * @param iTProfile the iTProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iTProfile,
     * or with status {@code 400 (Bad Request)} if the iTProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iTProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/it-profiles")
    public ResponseEntity<ITProfile> updateITProfile(@RequestBody ITProfile iTProfile) throws URISyntaxException {
        log.debug("REST request to update ITProfile : {}", iTProfile);
        if (iTProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ITProfile result = iTProfileRepository.save(iTProfile);
        iTProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, iTProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /it-profiles} : get all the iTProfiles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iTProfiles in body.
     */
    @GetMapping("/it-profiles")
    public List<ITProfile> getAllITProfiles() {
        log.debug("REST request to get all ITProfiles");
        return iTProfileRepository.findAll();
    }

    /**
     * {@code GET  /it-profiles/:id} : get the "id" iTProfile.
     *
     * @param id the id of the iTProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iTProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/it-profiles/{id}")
    public ResponseEntity<ITProfile> getITProfile(@PathVariable String id) {
        log.debug("REST request to get ITProfile : {}", id);
        Optional<ITProfile> iTProfile = iTProfileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iTProfile);
    }

    /**
     * {@code DELETE  /it-profiles/:id} : delete the "id" iTProfile.
     *
     * @param id the id of the iTProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/it-profiles/{id}")
    public ResponseEntity<Void> deleteITProfile(@PathVariable String id) {
        log.debug("REST request to delete ITProfile : {}", id);
        iTProfileRepository.deleteById(id);
        iTProfileSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/it-profiles?query=:query} : search for the iTProfile corresponding
     * to the query.
     *
     * @param query the query of the iTProfile search.
     * @return the result of the search.
     */
    @GetMapping("/_search/it-profiles")
    public List<ITProfile> searchITProfiles(@RequestParam String query) {
        log.debug("REST request to search ITProfiles for query {}", query);
        return StreamSupport
            .stream(iTProfileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
