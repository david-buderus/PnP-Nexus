package de.pnp.manager.server.database;

import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.user.GrantedUniverseAuthority;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.server.contoller.UserController;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

import static de.pnp.manager.server.database.DatabaseConstants.METADATA_DATABASE;

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
        mongoTemplate = config.mongoTemplate(METADATA_DATABASE);
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
     * Checks if the user password is valid.
     */
    public boolean isValidPassword(String username, String password) {
        PnPUserDetails userDetails = loadUserByUsername(username);
        return passwordEncoder.matches(password, userDetails.getPassword());
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
        refreshSession(username);
    }

    private void refreshSession(String username) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth == null || !currentAuth.getName().equals(username)) {
            return;
        }
        UserDetails updatedUser = loadUserByUsername(username);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser,
                updatedUser.getPassword(),
                updatedUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    /**
     * Removes the {@link GrantedUniverseAuthority authorities} from the user.
     */
    public void removeGrantedUniverseAuthorities(String username, ObjectId universe) {
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

    /**
     * Returns all users which have access to the given universes except admins.
     */
    public Collection<PnPUserDetails> getAllUsersWithUniversePermissions(ObjectId universe) {
        return mongoTemplate.find(Query.query(Criteria.where("authorities.universe").is(universe)),
                PnPUserDetails.class, REPOSITORY_NAME);
    }

    @Override
    public PnPUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found."));
    }
}
