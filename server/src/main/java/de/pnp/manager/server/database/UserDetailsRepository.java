package de.pnp.manager.server.database;

import static de.pnp.manager.server.database.UniverseRepository.DATABASE_NAME;

import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.user.PnPGrantedAuthority;
import de.pnp.manager.component.user.PnPUserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.BadCredentialsException;
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
     * Inserts the user into the database.
     */
    public void addNewUser(String username, String password, Collection<PnPGrantedAuthority> authorities) {
        mongoTemplate.insert(
            new PnPUserDetails(username, passwordEncoder.encode(password), authorities, true, true, true, true, false),
            REPOSITORY_NAME);
    }

    /**
     * Inserts the user into the database.
     */
    public void addNewAdmin(String username, String password) {
        mongoTemplate.insert(
            new PnPUserDetails(username, passwordEncoder.encode(password), Collections.emptyList(), true, true, true,
                true, true),
            REPOSITORY_NAME);
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
            new PnPUserDetails(username, passwordEncoder.encode(newPassword), userDetails.getPnPAuthorities(),
                userDetails.isAccountNonExpired(), userDetails.isAccountNonLocked(),
                userDetails.isCredentialsNonExpired(), userDetails.isEnabled(), userDetails.isAdmin()),
            REPOSITORY_NAME);
    }

    /**
     * Updates the {@link PnPGrantedAuthority authorities} of the user.
     */
    public void updateGrantedAuthority(String username, Collection<PnPGrantedAuthority> newAuthorities) {
        PnPUserDetails userDetails = loadUserByUsername(username);
        mongoTemplate.findAndReplace(Query.query(Criteria.where("_id").is(username)),
            new PnPUserDetails(username, userDetails.getPassword(), newAuthorities,
                userDetails.isAccountNonExpired(), userDetails.isAccountNonLocked(),
                userDetails.isCredentialsNonExpired(), userDetails.isEnabled(), userDetails.isAdmin()),
            REPOSITORY_NAME);
    }

    /**
     * Removes the user from the database.
     */
    public boolean removeUser(String username) {
        DeleteResult result = mongoTemplate.remove(Query.query(Criteria.where("_id").is(username)),
            REPOSITORY_NAME);
        return result.wasAcknowledged();
    }

    @Override
    public PnPUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username).orElseThrow(
            () -> new UsernameNotFoundException("User " + username + " not found."));
    }
}
