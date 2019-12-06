package fr.inti.xml.web.rest;

import fr.inti.xml.domain.PsychoProfile;
import fr.inti.xml.repository.PsychoProfileRepository;
import fr.inti.xml.repository.search.PsychoProfileSearchRepository;
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
 * REST controller for managing {@link fr.inti.xml.domain.PsychoProfile}.
 */
@RestController
@RequestMapping("/api")
public class PsychoProfileResource {

    private final Logger log = LoggerFactory.getLogger(PsychoProfileResource.class);

    private static final String ENTITY_NAME = "psychoProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PsychoProfileRepository psychoProfileRepository;

    private final PsychoProfileSearchRepository psychoProfileSearchRepository;

    public PsychoProfileResource(PsychoProfileRepository psychoProfileRepository, PsychoProfileSearchRepository psychoProfileSearchRepository) {
        this.psychoProfileRepository = psychoProfileRepository;
        this.psychoProfileSearchRepository = psychoProfileSearchRepository;
    }

    /**
     * {@code POST  /psycho-profiles} : Create a new psychoProfile.
     *
     * @param psychoProfile the psychoProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new psychoProfile, or with status {@code 400 (Bad Request)} if the psychoProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/psycho-profiles")
    public ResponseEntity<PsychoProfile> createPsychoProfile(@RequestBody PsychoProfile psychoProfile) throws URISyntaxException {
        log.debug("REST request to save PsychoProfile : {}", psychoProfile);
        if (psychoProfile.getId() != null) {
            throw new BadRequestAlertException("A new psychoProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PsychoProfile result = psychoProfileRepository.save(psychoProfile);
        psychoProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/psycho-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /psycho-profiles} : Updates an existing psychoProfile.
     *
     * @param psychoProfile the psychoProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated psychoProfile,
     * or with status {@code 400 (Bad Request)} if the psychoProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the psychoProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/psycho-profiles")
    public ResponseEntity<PsychoProfile> updatePsychoProfile(@RequestBody PsychoProfile psychoProfile) throws URISyntaxException {
        log.debug("REST request to update PsychoProfile : {}", psychoProfile);
        if (psychoProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PsychoProfile result = psychoProfileRepository.save(psychoProfile);
        psychoProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, psychoProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /psycho-profiles} : get all the psychoProfiles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of psychoProfiles in body.
     */
    @GetMapping("/psycho-profiles")
    public List<PsychoProfile> getAllPsychoProfiles() {
        log.debug("REST request to get all PsychoProfiles");
        return psychoProfileRepository.findAll();
    }

    /**
     * {@code GET  /psycho-profiles/:id} : get the "id" psychoProfile.
     *
     * @param id the id of the psychoProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the psychoProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/psycho-profiles/{id}")
    public ResponseEntity<PsychoProfile> getPsychoProfile(@PathVariable String id) {
        log.debug("REST request to get PsychoProfile : {}", id);
        Optional<PsychoProfile> psychoProfile = psychoProfileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(psychoProfile);
    }

    /**
     * {@code DELETE  /psycho-profiles/:id} : delete the "id" psychoProfile.
     *
     * @param id the id of the psychoProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/psycho-profiles/{id}")
    public ResponseEntity<Void> deletePsychoProfile(@PathVariable String id) {
        log.debug("REST request to delete PsychoProfile : {}", id);
        psychoProfileRepository.deleteById(id);
        psychoProfileSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/psycho-profiles?query=:query} : search for the psychoProfile corresponding
     * to the query.
     *
     * @param query the query of the psychoProfile search.
     * @return the result of the search.
     */
    @GetMapping("/_search/psycho-profiles")
    public List<PsychoProfile> searchPsychoProfiles(@RequestParam String query) {
        log.debug("REST request to search PsychoProfiles for query {}", query);
        return StreamSupport
            .stream(psychoProfileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
