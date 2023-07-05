package de.pnp.manager.component;

import org.springframework.data.annotation.Id;

/**
 * The description of a universe.
 */
public class Universe {

    /**
     * The unique name of this universe.
     * <p>
     * This will never change.
     */
    @Id
    private final String name;

    /**
     * The human-readable name of this universe.
     */
    private final String displayName;

    private StatComputationRules statComputationRules;

    /**
     * The settings of the {@link Universe}
     */
    private final UniverseSettings settings;

    public Universe(String name, String displayName) {
        this(name, displayName, UniverseSettings.DEFAULT);
    }

    public Universe(String name, String displayName, UniverseSettings settings) {
        this.name = name;
        this.displayName = displayName;
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UniverseSettings getSettings() {
        return settings;
    }

    /**
     * The settings of the {@link Universe}.
     */
    public record UniverseSettings(int wearFactor) {

        /**
         * The default settings
         */
        public final static UniverseSettings DEFAULT = new UniverseSettings(10);
    }
}
