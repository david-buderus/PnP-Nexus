package de.pnp.manager.server.contoller;

import de.pnp.manager.component.user.*;
import de.pnp.manager.exception.UniverseNotFoundException;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserPreferenceRepository;
import de.pnp.manager.server.database.UserRepository;
import de.pnp.manager.server.database.universe.UniverseRepository;
import jakarta.validation.ConstraintViolationException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A controller to create and manipulate user.
 */
@Component
public class UserController {

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final UserPreferenceRepository preferenceRepository;

    private final UniverseRepository universeRepository;

    public UserController(@Autowired UserRepository userRepository,
                          @Autowired UserDetailsRepository userDetailsRepository,
                          @Autowired UserPreferenceRepository preferenceRepository,
                          @Autowired UniverseRepository universeRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.preferenceRepository = preferenceRepository;
        this.universeRepository = universeRepository;
    }

    /**
     * Creates a {@link PnPUser} with corresponding {@link PnPUserDetails}.
     */
    public void createNewUser(PnPUserCreation userCreation) {
        String username = userCreation.getUsername();
        try {
            userRepository.addNewUser(
                    new PnPUser(username, userCreation.getDisplayName(), userCreation.getEmail()));
            userDetailsRepository.addNewUser(username, userCreation.getPassword(),
                    userCreation.getAuthorities().stream().map(IGrantedAuthorityDTO::convert).toList());
            preferenceRepository.addNewPreference(new PnPUserPreference(username, null, null));
        } catch (ConstraintViolationException e) {
            userRepository.removeUser(username);
            userDetailsRepository.removeUser(username);
            preferenceRepository.removeUser(username);
            throw e;
        }
    }

    /**
     * Removes the user from the databases.
     */
    public boolean removeUser(String username) {
        boolean removedFromUserRepo = userRepository.removeUser(username);
        boolean removedFromDetailsRepo = userDetailsRepository.removeUser(username);
        boolean removedFromPreferenceRepo = preferenceRepository.removeUser(username);
        return removedFromUserRepo && removedFromDetailsRepo && removedFromPreferenceRepo;
    }

    /**
     * Gets all usernames.
     */
    public Collection<String> getAllUsernames() {
        Set<String> usernames = new HashSet<>();
        usernames.addAll(userRepository.getAllUsers().stream().map(PnPUser::username).toList());
        usernames.addAll(userDetailsRepository.getAllUsernames());
        return usernames;
    }

    /**
     * Adds the {@link GrantedDatabaseObjectAuthority authorities} to the user.
     */
    public void addGrantedAuthorityByDisplayName(String displayName, GrantedAuthority... newAuthorities) {
        Optional<PnPUser> user = userRepository.getUserByDisplayName(displayName);
        userDetailsRepository.addGrantedAuthority(
                user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with display name " + displayName + " not found.")).username(), newAuthorities);
    }

    /**
     * Removes the {@link GrantedDatabaseObjectAuthority authorities} from the user.
     */
    public void removeGrantedDatabaseObjectAuthoritiesByDisplayName(String displayName, ObjectId id) {
        Optional<PnPUser> user = userRepository.getUserByDisplayName(displayName);
        userDetailsRepository.removeGrantedDatabaseObjectAuthorities(
                user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with display name " + displayName + " not found.")).username(), id);
    }

    /**
     * Returns all users with their access right to the given universes except admins.
     *
     * @throws UniverseNotFoundException if the universe does not exist
     */
    public Collection<UserDatabaseObjectPermissionDTO> getAllUserWithUniversePermission(ObjectId universe) {
        if (!universeRepository.exists(universe)) {
            throw new UniverseNotFoundException(universe);
        }
        return getAllUserWithDatabaseObjectPermission(universe);
    }

    /**
     * Returns all users with their access right to the given database object except admins.
     */
    public Collection<UserDatabaseObjectPermissionDTO> getAllUserWithDatabaseObjectPermission(ObjectId id) {
        Collection<PnPUserDetails> users = userDetailsRepository.getAllUsersWithDatabaseObjectPermissions(id);
        Map<String, String> displayNames = userRepository.getAllUsers(
                        users.stream().map(PnPUserDetails::getUsername).toList()).stream()
                .collect(Collectors.toUnmodifiableMap(PnPUser::username, PnPUser::displayName));
        return users.stream().map(detail -> new UserDatabaseObjectPermissionDTO(displayNames.get(detail.getUsername()),
                IGrantedAuthorityDTO.from(getHighestDatabaseObjectAuthority(id, detail.getAuthorities())))).toList();
    }

    private static GrantedDatabaseObjectAuthority getHighestDatabaseObjectAuthority(
            ObjectId id, Collection<? extends GrantedAuthority> authorities) {
        List<GrantedDatabaseObjectAuthority> databaseObjectAuthorities = authorities.stream()
                .filter(GrantedDatabaseObjectAuthority.class::isInstance).map(GrantedDatabaseObjectAuthority.class::cast)
                .filter(auth -> auth.getObjectId().equals(id)).toList();
        Optional<GrantedDatabaseObjectAuthority> owner = databaseObjectAuthorities.stream().filter(auth -> auth.isOwner(id))
                .findFirst();
        if (owner.isPresent()) {
            return owner.get();
        }
        Optional<GrantedDatabaseObjectAuthority> write = databaseObjectAuthorities.stream().filter(auth -> auth.canWrite(id))
                .findFirst();
        return write.orElseGet(() -> databaseObjectAuthorities.stream().findFirst().orElseThrow());
    }

    /**
     * Returns if the user exists in at least one of the two databases.
     */
    public boolean exists(String username) {
        return userRepository.getUser(username).isPresent() || userDetailsRepository.getUser(username).isPresent();
    }
}
