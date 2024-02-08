package de.pnp.manager.server;

import de.pnp.manager.component.user.IGrantedAuthorityDTO.RoleAuthorityDTO;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.server.contoller.UserController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Loads initial data.
 */
@Component
@SuppressWarnings("unused") // Used due to ApplicationRunner
public class DataLoader implements ApplicationRunner {

    @Autowired
    private UserController userController;

    @Override
    public void run(ApplicationArguments args) {
        if (userController.getAllUsernames().isEmpty()) {
            userController.createNewUser(
                new PnPUserCreation("admin", "admin", "admin", null,
                    List.of(new RoleAuthorityDTO(SecurityConstants.ADMIN))));
        }
    }
}