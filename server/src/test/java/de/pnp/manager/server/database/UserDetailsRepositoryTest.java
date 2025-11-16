package de.pnp.manager.server.database;

import de.pnp.manager.component.user.GrantedDatabaseObjectAuthority;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.server.UniverseTestBase;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link UserDetailsRepository}
 */
public class UserDetailsRepositoryTest extends UniverseTestBase {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    void testListUsersWithUniverseAccess() {
        ObjectId otherUniverseId = new ObjectId();

        userDetailsRepository.addNewUser("A", "A", List.of());
        userDetailsRepository.addNewUser("B", "B", List.of(new SimpleGrantedAuthority(SecurityConstants.ADMIN_ROLE)));
        userDetailsRepository.addNewUser("C", "C", List.of(GrantedDatabaseObjectAuthority.readAuthority(getUniverseId())));
        userDetailsRepository.addNewUser("D", "D", List.of(GrantedDatabaseObjectAuthority.writeAuthority(getUniverseId())));
        userDetailsRepository.addNewUser("E", "E", List.of(GrantedDatabaseObjectAuthority.ownerAuthority(getUniverseId())));
        userDetailsRepository.addNewUser("F", "F", List.of(GrantedDatabaseObjectAuthority.readAuthority(getUniverseId()),
                GrantedDatabaseObjectAuthority.ownerAuthority(otherUniverseId)));
        userDetailsRepository.addNewUser("G", "G", List.of(GrantedDatabaseObjectAuthority.readAuthority(otherUniverseId)));

        assertThat(userDetailsRepository.getAllUsersWithDatabaseObjectPermissions(getUniverseId())).extracting(
                PnPUserDetails::getUsername).containsExactlyInAnyOrder("C", "D", "E", "F");
    }
}
