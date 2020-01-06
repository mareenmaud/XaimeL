package fr.inti.xml.web.rest;

import fr.inti.xml.domain.ExtendedUser;
import fr.inti.xml.domain.PsychoProfile;
import fr.inti.xml.domain.User;
import fr.inti.xml.repository.ExtendedUserRepository;
import fr.inti.xml.repository.UserRepository;
import fr.inti.xml.repository.search.ExtendedUserSearchRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link fr.inti.xml.domain.ExtendedUser}.
 */
@RestController
@RequestMapping("/api")
public class ExtendedUserResource {

    public static final double distanceMax = 30;

    private final Logger log = LoggerFactory.getLogger(ExtendedUserResource.class);

    private static final String ENTITY_NAME = "extendedUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtendedUserRepository extendedUserRepository;

    private final ExtendedUserSearchRepository extendedUserSearchRepository;

    private final UserRepository userRepository;

    public ExtendedUserResource(ExtendedUserRepository extendedUserRepository, ExtendedUserSearchRepository extendedUserSearchRepository,
                                UserRepository userRepository) {
        this.extendedUserRepository = extendedUserRepository;
        this.extendedUserSearchRepository = extendedUserSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /extended-users} : Create a new extendedUser.
     *
     * @param extendedUser the extendedUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extendedUser, or with status {@code 400 (Bad Request)} if the extendedUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extended-users")
    public ResponseEntity<ExtendedUser> createExtendedUser(@RequestBody ExtendedUser extendedUser) throws URISyntaxException {
        log.debug("REST request to save ExtendedUser : {}", extendedUser);
        if (extendedUser.getId() != null) {
            throw new BadRequestAlertException("A new extendedUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtendedUser result = extendedUserRepository.save(extendedUser);
        extendedUserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/extended-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extended-users} : Updates an existing extendedUser.
     *
     * @param extendedUser the extendedUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extendedUser,
     * or with status {@code 400 (Bad Request)} if the extendedUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extendedUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extended-users")
    public ResponseEntity<ExtendedUser> updateExtendedUser(@RequestBody ExtendedUser extendedUser) throws URISyntaxException {
        log.debug("REST request to update ExtendedUser : {}", extendedUser);
        if (extendedUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExtendedUser result = extendedUserRepository.save(extendedUser);
        extendedUserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extendedUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /extended-users} : get all the extendedUsers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extendedUsers in body.
     */
    @GetMapping("/extended-users")
    public List<ExtendedUser> getAllExtendedUsers() {
        log.debug("REST request to get all ExtendedUsers");
        return extendedUserRepository.findAll();
    }

    /**
     * {@code GET  /extended-users/:id} : get the "id" extendedUser.
     *
     * @param id the id of the extendedUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extendedUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extended-users/{id}")
    public ResponseEntity<ExtendedUser> getExtendedUser(@PathVariable String id) {
        log.debug("REST request to get ExtendedUser : {}", id);
        Optional<ExtendedUser> extendedUser = extendedUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(extendedUser);
    }

    /**
     * {@code DELETE  /extended-users/:id} : delete the "id" extendedUser.
     *
     * @param id the id of the extendedUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extended-users/{id}")
    public ResponseEntity<Void> deleteExtendedUser(@PathVariable String id) {
        log.debug("REST request to delete ExtendedUser : {}", id);
        extendedUserRepository.deleteById(id);
        extendedUserSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/extended-users?query=:query} : search for the extendedUser corresponding
     * to the query.
     *
     * @param query the query of the extendedUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search/extended-users")
    public List<ExtendedUser> searchExtendedUsers(@RequestParam String query) {
        log.debug("REST request to search ExtendedUsers for query {}", query);
        return StreamSupport
            .stream(extendedUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * Computes psychological distance between two users
     */
    public double psychoDistance(ExtendedUser user1, ExtendedUser user2) {
        PsychoProfile psychoProfile1 = user1.getPsychoProfile();
        PsychoProfile psychoProfile2 = user2.getPsychoProfile();
        double[] jungValues1 = {psychoProfile1.getJungValue1(), psychoProfile1.getJungValue2(),
            psychoProfile1.getJungValue3(), psychoProfile1.getJungValue4()};
        double[] jungValues2 = {psychoProfile2.getJungValue1(), psychoProfile2.getJungValue2(),
            psychoProfile2.getJungValue3(), psychoProfile2.getJungValue4()};
        double distance = 0;
        for (int i=0; i<4; i++) {
            distance = distance + (jungValues1[i]*jungValues1[i] - jungValues2[i]*jungValues2[i]);
        }
        return Math.sqrt(distance);
    }

    /**
     * Computes geographical distance between two users
     */
    public double geoDistance(ExtendedUser user1, ExtendedUser user2) {

        Double[] location1 = {user1.getLocationLongitude(), user1.getLocationLatitude()};
        Double[] location2 = {user2.getLocationLongitude(), user2.getLocationLatitude()};
        double distance = 0;
        for (int i=0; i<2; i++) {
            distance = distance + (location1[i]*location1[i] - location2[i]*location2[i]);
        }
        return Math.sqrt(distance);
    }

    /**
     * Sorts a list of users by ascending distance
     */
    public List<ExtendedUser> sortUsersByDistance(List<ExtendedUser> users, List<Double> distances) {

        Double[] distancesArray = (Double[]) distances.toArray();
        ExtendedUser[] usersArray = (ExtendedUser[]) users.toArray();

        Double tempD;
        ExtendedUser tempU;
        boolean is_sorted;
        int nbMatches = distancesArray.length;
        for (int i = 0; i < nbMatches; i++) {

            is_sorted = true;

            for (int j = 1; j < (nbMatches - i); j++) {

                if (distancesArray[j-1] > distancesArray[j]) {
                    tempD = distancesArray[j-1];
                    distancesArray[j-1] = distancesArray[j];
                    distancesArray[j] = tempD;
                    tempU = usersArray[j-1];
                    usersArray[j-1] = usersArray[j];
                    usersArray[j] = tempU;
                    is_sorted = false;
                }
            }
            if (is_sorted) break;
        }
        return Arrays.asList(usersArray);
    }

    /**
     * Find an ExtendedUser by User.
     *
     *
     */
    public ExtendedUser findExtendedUserByUser(User user) {
        List<ExtendedUser> allExtendedUsers = extendedUserRepository.findAll();
        for (ExtendedUser exu : allExtendedUsers) {
            if (exu.getUser().getId()==user.getId()) return exu;
        }
        return null;
    }

    /**
     * Gets a user's matches and sorts by psychological distance
     */
    @GetMapping("/get-and-sort-matches/{id}")
    public List<ExtendedUser> getAndSortMatches(@PathVariable String id) throws URISyntaxException {
        log.debug("REST request to get and sort matches for ExtendedUser: {}", id);
        User user = userRepository.findById(id).get();
        ExtendedUser extendedUser = findExtendedUserByUser(user);
        List<Double> distances = new ArrayList<>();
        List<ExtendedUser> matches = new ArrayList<>();
        for (ExtendedUser match : extendedUser.getMatches()) {
            matches.add(match);
            distances.add(this.psychoDistance(extendedUser, match));
        }
        return sortUsersByDistance(matches, distances);
    }

    /**
     * Gets all users geographically close enough
     */
    public List<ExtendedUser> getCloseUsers(ExtendedUser user) {

        List<ExtendedUser> allUsers = extendedUserRepository.findAll();
        List<ExtendedUser> allUsersWithSameSummary = new ArrayList<>();
        for (ExtendedUser u : allUsers) {
            if (u.getPsychoProfile().getSummaryProfile().equals(user.getPsychoProfile().getSummaryProfile())) {
                allUsersWithSameSummary.add(u);
            }
        }

        List<ExtendedUser> closeUsers = new ArrayList<>();
        for (ExtendedUser u : allUsersWithSameSummary) {
            if (this.geoDistance(user, u)<=distanceMax) {
                closeUsers.add(u);
            }
        }
        return closeUsers;
    }

    /**
     * Gets possible matches
     */
    @GetMapping("/get-possible-matches/{id}")
    public List<ExtendedUser> getPossibleMatches(@PathVariable String id) throws URISyntaxException {
        log.debug("REST request to get possible matches for ExtendedUser: {}", id);
        User user = userRepository.findById(id).get();
        ExtendedUser extendedUser = findExtendedUserByUser(user);
        List<ExtendedUser> closeUsers = getCloseUsers(extendedUser);
        List<Double> distances = new ArrayList<>();
        for (ExtendedUser u : closeUsers) {
            distances.add(this.psychoDistance(u, extendedUser));
        }
        return this.sortUsersByDistance(closeUsers, distances);
    }
}
