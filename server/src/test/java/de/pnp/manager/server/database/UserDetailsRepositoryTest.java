package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.user.GrantedUniverseAuthority;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.server.UniverseTestBase;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Tests for {@link UserDetailsRepository}
 */
public class UserDetailsRepositoryTest extends UniverseTestBase {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    void testListUsersWithUniverseAccess() {
        userDetailsRepository.addNewUser("A", "A", List.of());
        userDetailsRepository.addNewUser("B", "B", List.of(new SimpleGrantedAuthority(SecurityConstants.ADMIN_ROLE)));
        userDetailsRepository.addNewUser("C", "C", List.of(GrantedUniverseAuthority.readAuthority(getUniverseName())));
        userDetailsRepository.addNewUser("D", "D", List.of(GrantedUniverseAuthority.writeAuthority(getUniverseName())));
        userDetailsRepository.addNewUser("E", "E", List.of(GrantedUniverseAuthority.ownerAuthority(getUniverseName())));
        userDetailsRepository.addNewUser("F", "F", List.of(GrantedUniverseAuthority.readAuthority(getUniverseName()),
            GrantedUniverseAuthority.ownerAuthority("OTHER")));
        userDetailsRepository.addNewUser("G", "G", List.of(GrantedUniverseAuthority.readAuthority("OTHER")));

        assertThat(userDetailsRepository.getAllUsersWithUniversePermissions(getUniverseName())).extracting(
            PnPUserDetails::getUsername).containsExactlyInAnyOrder("C", "D", "E", "F");
    }
}
