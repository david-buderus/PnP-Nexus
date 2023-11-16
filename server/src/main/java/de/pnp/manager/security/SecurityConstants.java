package de.pnp.manager.security;

/**
 * Constants needed in the security context.
 */
public abstract class SecurityConstants {

    /**
     * The identifier for the admin role.
     */
    public static final String ADMIN = "ADMIN";

    /**
     * Prefixed {@link #ADMIN} with the correct prefix.
     */
    public static final String ADMIN_ROLE = "ROLE_" + ADMIN;

    /**
     * The identifier for the universe creator role.
     */
    public static final String UNIVERSE_CREATOR = "UNIVERSE_CREATOR";

    /**
     * Prefixed {@link #UNIVERSE_CREATOR} with the correct prefix.
     */
    public static final String UNIVERSE_CREATOR_ROLE = "ROLE_" + UNIVERSE_CREATOR;

    /**
     * Identifier for read access permissions.
     */
    public static final String READ_ACCESS = "READ";

    /**
     * Identifier for write access permissions.
     */
    public static final String WRITE_ACCESS = "WRITE";

    /**
     * Identifier for owner permissions.
     */
    public static final String OWNER = "OWNER";

    /**
     * Identifier for permission targeted at universes.
     */
    public static final String UNIVERSE_TARGET_ID = "UNIVERSE";
}
