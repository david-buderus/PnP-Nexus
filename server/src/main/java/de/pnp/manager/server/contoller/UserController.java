package de.pnp.manager.server.contoller;

import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                userCreation.getAuthorities());
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
     * Returns if the user exists in at least one of the two databases.
     */
    public boolean exists(String username) {
        return userRepository.getUser(username).isPresent() || userDetailsRepository.getUser(username).isPresent();
    }
}
