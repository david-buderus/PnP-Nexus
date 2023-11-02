package de.pnp.manager.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.server.database.UniverseRepository;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The description of a universe.
 */
@Document(UniverseRepository.REPOSITORY_NAME)
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

    /**
     * The settings of the {@link Universe}
     */
    private final UniverseSettings settings;

    public Universe(String name, String displayName) {
        this(name, displayName, UniverseSettings.DEFAULT);
    }

    @PersistenceCreator
    @JsonCreator
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Universe universe = (Universe) o;
        return getName().equals(universe.getName()) && getDisplayName().equals(universe.getDisplayName())
            && getSettings().equals(universe.getSettings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDisplayName(), getSettings());
    }

    /**
     * The settings of the {@link Universe}.
     */
    public record UniverseSettings(int wearFactor) {

        /**
         * The default settings
         */
        public static final UniverseSettings DEFAULT = new UniverseSettings(10);
    }
}
