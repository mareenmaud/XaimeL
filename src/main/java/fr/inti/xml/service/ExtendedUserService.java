package fr.inti.xml.service;

import fr.inti.xml.domain.*;
import fr.inti.xml.repository.ExtendedUserRepository;
import fr.inti.xml.repository.UserRepository;
import fr.inti.xml.security.SecurityUtils;
import fr.inti.xml.service.dto.ExtendedUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

public class ExtendedUserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final ExtendedUserRepository extendedUserRepository;
    private final UserRepository userRepository;
    public static final double distanceMax = 30;

    public ExtendedUserService(ExtendedUserRepository extendedUserRepository, UserRepository userRepository) {
        this.extendedUserRepository = extendedUserRepository;
        this.userRepository = userRepository;
    }

    public ExtendedUser createExtendedUser(ExtendedUserDTO extendedUserDTO) {
        ExtendedUser extendedUser = new ExtendedUser();
        extendedUser.setBirthDate(extendedUserDTO.getBirthDate());
        extendedUser.setMemberSince(extendedUserDTO.getMemberSince());
        extendedUser.setLocationLongitude(extendedUserDTO.getLocationLongitude());
        extendedUser.setLocationLatitude(extendedUserDTO.getLocationLatitude());
        extendedUser.setGender(extendedUserDTO.getGender());
        extendedUser.setInterest(extendedUserDTO.getInterest());
        extendedUser.setNote(extendedUserDTO.getNote());
        extendedUser.setHobbies(extendedUserDTO.getHobbies());
        extendedUser.setProfilePhotoURL(extendedUserDTO.getProfilePhotoURL());
        extendedUser.setITProfile(extendedUserDTO.getiTProfile());
        extendedUser.setPsychoProfile(extendedUserDTO.getPsychoProfile());
        extendedUser.setUser(extendedUserDTO.getUser());
        extendedUser.setExtendedUser(extendedUserDTO.getExtendedUser());
        extendedUser.setMatches(extendedUserDTO.getMatches());

        extendedUserRepository.save(extendedUser);
        log.debug("Created Information for ExtendedUser: {}", extendedUser);
        return extendedUser;
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
     * Update basic information for the current user.
     *
     *
     */
    public void updateExtendedUser(Instant birthDate, Instant memberSince, Double locationLongitude, Double locationLatitude,
                           String gender, String interest, Double note, String hobbies, String profilePhotoURL,
                           ITProfile iTProfile, PsychoProfile psychoProfile, User user1, ExtendedUser extendedUser1,
                           Set<String> matches) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                ExtendedUser extendedUser = findExtendedUserByUser(user);
                extendedUser.setBirthDate(birthDate);
                extendedUser.setMemberSince(memberSince);
                extendedUser.setLocationLongitude(locationLongitude);
                extendedUser.setLocationLatitude(locationLatitude);
                extendedUser.setGender(gender);
                extendedUser.setInterest(interest);
                extendedUser.setNote(note);
                extendedUser.setHobbies(hobbies);
                extendedUser.setProfilePhotoURL(profilePhotoURL);
                extendedUser.setITProfile(iTProfile);
                extendedUser.setPsychoProfile(psychoProfile);
                extendedUser.setUser(user1);
                extendedUser.setExtendedUser(extendedUser1);
                extendedUser.setMatches(matches);

                extendedUserRepository.save(extendedUser);
                log.debug("Changed Information for ExtendedUser: {}", extendedUser);
            });
    }

    /**
     * Update all information for a specific extended user, and return the modified extended user.
     *
     * @param extendedUserDTO user to update.
     * @return updated extended user.
     */
    public Optional<ExtendedUserDTO> updateExtendedUser(ExtendedUserDTO extendedUserDTO) {
        return Optional.of(extendedUserRepository
            .findById(extendedUserDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(extendedUser -> {
                extendedUser.setBirthDate(extendedUserDTO.getBirthDate());
                extendedUser.setMemberSince(extendedUserDTO.getMemberSince());
                extendedUser.setLocationLongitude(extendedUserDTO.getLocationLongitude());
                extendedUser.setLocationLatitude(extendedUserDTO.getLocationLatitude());
                extendedUser.setGender(extendedUserDTO.getGender());
                extendedUser.setInterest(extendedUserDTO.getInterest());
                extendedUser.setNote(extendedUserDTO.getNote());
                extendedUser.setHobbies(extendedUserDTO.getHobbies());
                extendedUser.setProfilePhotoURL(extendedUserDTO.getProfilePhotoURL());
                extendedUser.setITProfile(extendedUserDTO.getiTProfile());
                extendedUser.setPsychoProfile(extendedUserDTO.getPsychoProfile());
                extendedUser.setUser(extendedUserDTO.getUser());
                extendedUser.setExtendedUser(extendedUserDTO.getExtendedUser());
                extendedUser.setMatches(extendedUserDTO.getMatches());
                extendedUserRepository.save(extendedUser);
                log.debug("Changed Information for ExtendedUser: {}", extendedUser);
                return extendedUser;
            })
            .map(ExtendedUserDTO::new);
    }

    public void deleteExtendedUser(String id) {
        extendedUserRepository.findById(id).ifPresent(extendedUser -> {
            extendedUserRepository.delete(extendedUser);
            log.debug("Deleted ExtendedUser: {}", extendedUser);
        });
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

        double[] distancesArray = new double[distances.size()];
        ExtendedUser[] usersArray = new ExtendedUser[distances.size()];
        for (int i = 0; i < distancesArray.length; i++) {
            distancesArray[i] = distances.get(i);
            usersArray[i] = users.get(i);
        }

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
    public List<ExtendedUser> getPossibleMatches(ExtendedUser user) {

        List<ExtendedUser> closeUsers = getCloseUsers(user);
        List<Double> distances = new ArrayList<>();
        for (ExtendedUser u : closeUsers) {
            distances.add(this.psychoDistance(u, user));
        }
        return this.sortUsersByDistance(closeUsers, distances);
    }

    /**
     * Deletes a match from a user
     */
    public ExtendedUser deleteMatch(ExtendedUser user, ExtendedUser match) {

        try {
            user.getMatches().remove(match);
            return user;
        } catch (Exception e) {
            System.err.println("Error deleting match");
        }
        return user;
    }

}
