package de.pnp.manager.server.database;

/**
 * Constants used in the database.
 */
public abstract class DatabaseConstants {

    /**
     * Name of the database which stores the metadata like universes and users.
     */
    public static final String METADATA_DATABASE = "metadata";

    /**
     * Prefix which is in front of a universe database.
     */
    public static final String UNIVERSE_PREFIX = "universe-";
}
