package de.pnp.manager.server.contoller;

import de.pnp.manager.component.user.GrantedUniverseAuthority;
import de.pnp.manager.component.user.IGrantedAuthorityDTO;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.component.user.UserUniversePermissionDTO;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserRepository;
import jakarta.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * A controller to create and manipulate user.
 */
@Component
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    /**
     * Creates a {@link PnPUser} with corresponding {@link PnPUserDetails}.
     */
    public void createNewUser(PnPUserCreation userCreation) {
        try {
            userRepository.addNewUser(
                new PnPUser(userCreation.getUsername(), userCreation.getDisplayName(), userCreation.getEmail()));
            userDetailsRepository.addNewUser(userCreation.getUsername(), userCreation.getPassword(),
                userCreation.getAuthorities().stream().map(IGrantedAuthorityDTO::convert).toList());
        } catch (ConstraintViolationException e) {
            userRepository.removeUser(userCreation.getUsername());
            userDetailsRepository.removeUser(userCreation.getUsername());
            throw e;
        }
    }

    /**
     * Removes the user from the databases.
     */
    public boolean removeUser(String username) {
        boolean removedFromUserRepo = userRepository.removeUser(username);
        boolean removedFromDetailsRepo = userDetailsRepository.removeUser(username);
        return removedFromUserRepo && removedFromDetailsRepo;
    }

    /**
     * Gets all usernames.
     */
    public Collection<String> getAllUsernames() {
        Set<String> usernames = new HashSet<>();
        usernames.addAll(userRepository.getAllUsers().stream().map(PnPUser::getUsername).toList());
        usernames.addAll(userDetailsRepository.getAllUsernames());
        return usernames;
    }

    /**
     * Adds the {@link GrantedUniverseAuthority authorities} to the user.
     */
    public void addGrantedAuthorityByDisplayName(String displayName, GrantedAuthority... newAuthorities) {
        Optional<PnPUser> user = userRepository.getUserByDisplayName(displayName);
        userDetailsRepository.addGrantedAuthority(
            user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with display name " + displayName + " not found.")).getUsername(), newAuthorities);
    }

    /**
     * Removes the {@link GrantedUniverseAuthority authorities} from the user.
     */
    public void removeGrantedUniverseAuthoritiesByDisplayName(String displayName, String universe) {
        Optional<PnPUser> user = userRepository.getUserByDisplayName(displayName);
        userDetailsRepository.removeGrantedUniverseAuthorities(
            user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with display name " + displayName + " not found.")).getUsername(), universe);
    }

    /**
     * Returns all users with their access right to the given universes except admins.
     */
    public Collection<UserUniversePermissionDTO> getAllUserWithUniversePermission(String universe) {
        Collection<PnPUserDetails> users = userDetailsRepository.getAllUsersWithUniversePermissions(
            universe);
        Map<String, String> displayNames = userRepository.getAllUsers(
                users.stream().map(PnPUserDetails::getUsername).toList()).stream()
            .collect(Collectors.toUnmodifiableMap(PnPUser::getUsername, PnPUser::getDisplayName));
        return users.stream().map(detail -> new UserUniversePermissionDTO(displayNames.get(detail.getUsername()),
            IGrantedAuthorityDTO.from(getHighestUniverseAuthority(universe, detail.getAuthorities())))).toList();
    }

    private static GrantedUniverseAuthority getHighestUniverseAuthority(String universe,
        Collection<? extends GrantedAuthority> authorities) {
        List<GrantedUniverseAuthority> universeAuthorities = authorities.stream()
            .filter(GrantedUniverseAuthority.class::isInstance).map(GrantedUniverseAuthority.class::cast)
            .filter(auth -> auth.getUniverse().equals(universe)).toList();
        Optional<GrantedUniverseAuthority> owner = universeAuthorities.stream().filter(auth -> auth.isOwner(universe))
            .findFirst();
        if (owner.isPresent()) {
            return owner.get();
        }
        Optional<GrantedUniverseAuthority> write = universeAuthorities.stream().filter(auth -> auth.canWrite(universe))
            .findFirst();
        return write.orElseGet(() -> universeAuthorities.stream().findFirst().orElseThrow());
    }

    /**
     * Returns if the user exists in at least one of the two databases.
     */
    public boolean exists(String username) {
        return userRepository.getUser(username).isPresent() || userDetailsRepository.getUser(username).isPresent();
    }
}
