package de.pnp.manager.server.database;

import static de.pnp.manager.server.database.UniverseRepository.DATABASE_NAME;

import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.user.GrantedUniverseAuthority;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.server.contoller.UserController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link PnPUserDetails userDetails}.
 */
@Component
public class UserDetailsRepository implements UserDetailsService {

    /**
     * Name of the authentication repository.
     */
    public static final String REPOSITORY_NAME = "authentication";

    private final MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    public UserDetailsRepository(@Autowired MongoConfig config) {
        mongoTemplate = config.mongoTemplate(DATABASE_NAME);
    }

    /**
     * Returns the {@link PnPUserDetails} of the given user.
     */
    public Optional<PnPUserDetails> getUser(String username) {
        return Optional.ofNullable(
            mongoTemplate.findById(username, PnPUserDetails.class, REPOSITORY_NAME));
    }

    /**
     * Gets all usernames.
     */
    public Collection<String> getAllUsernames() {
        return mongoTemplate.findAll(PnPUserDetails.class, REPOSITORY_NAME).stream().map(PnPUserDetails::getUsername)
            .toList();
    }

    /**
     * Inserts the user into the database.
     * <p>
     * Don't call this directly. Use {@link UserController#createNewUser(PnPUserCreation)}.
     */
    public void addNewUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        mongoTemplate.insert(
            new PnPUserDetails(username, passwordEncoder.encode(password), authorities, true, true, true, true),
            REPOSITORY_NAME);
    }

    /**
     * Removes the user from the database.
     * <p>
     * Don't call this directly. Use {@link UserController#removeUser(String)}.
     */
    public boolean removeUser(String username) {
        DeleteResult result = mongoTemplate.remove(Query.query(Criteria.where("_id").is(username)),
            REPOSITORY_NAME);
        return result.wasAcknowledged();
    }

    /**
     * Updates the password of the user.
     */
    public void updatePassword(String username, String oldPassword, String newPassword) {
        PnPUserDetails userDetails = loadUserByUsername(username);
        if (!passwordEncoder.matches(oldPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(this.messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        mongoTemplate.findAndReplace(Query.query(Criteria.where("_id").is(username)),
            new PnPUserDetails(username, passwordEncoder.encode(newPassword), userDetails.getAuthorities(),
                userDetails.isAccountNonExpired(), userDetails.isAccountNonLocked(),
                userDetails.isCredentialsNonExpired(), userDetails.isEnabled()),
            REPOSITORY_NAME);
    }

    /**
     * Updates the {@link GrantedUniverseAuthority authorities} of the user.
     */
    public void updateGrantedAuthority(String username, Collection<GrantedAuthority> newAuthorities) {
        PnPUserDetails userDetails = loadUserByUsername(username);
        setUserGrantedAuthorities(userDetails, newAuthorities);
    }

    /**
     * Adds the {@link GrantedUniverseAuthority authorities} to the user.
     */
    public void addGrantedAuthority(String username, GrantedAuthority... newAuthorities) {
        PnPUserDetails userDetails = loadUserByUsername(username);
        List<GrantedAuthority> allAuthorities = new ArrayList<>(userDetails.getAuthorities());
        allAuthorities.addAll(Arrays.asList(newAuthorities));
        setUserGrantedAuthorities(userDetails, allAuthorities);
    }

    /**
     * Removes the {@link GrantedUniverseAuthority authorities} from the user.
     */
    public void removeGrantedUniverseAuthorities(String username, String universe) {
        PnPUserDetails userDetails = loadUserByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());

        authorities.removeIf(auth -> {
            if (auth instanceof GrantedUniverseAuthority universeAuthority) {
                return universeAuthority.getUniverse().equals(universe);
            }
            return false;
        });
        setUserGrantedAuthorities(userDetails, authorities);
    }

    private void setUserGrantedAuthorities(PnPUserDetails userDetails, Collection<GrantedAuthority> authorities) {
        mongoTemplate.findAndReplace(Query.query(Criteria.where("_id").is(userDetails.getUsername())),
            new PnPUserDetails(userDetails.getUsername(), userDetails.getPassword(), authorities,
                userDetails.isAccountNonExpired(), userDetails.isAccountNonLocked(),
                userDetails.isCredentialsNonExpired(), userDetails.isEnabled()),
            REPOSITORY_NAME);
    }

    @Override
    public PnPUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username).orElseThrow(
            () -> new UsernameNotFoundException("User " + username + " not found."));
    }
}
