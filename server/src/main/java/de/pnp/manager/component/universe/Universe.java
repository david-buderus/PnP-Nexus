package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.server.database.UniverseRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(min = 3, max = 64)
    private final String name;

    /**
     * The human-readable name of this universe.
     */
    @NotBlank
    @Size(min = 3, max = 64)
    private final String displayName;

    @NotNull
    private final String shortDescription;

    /**
     * The settings of the {@link Universe}
     */
    @Valid
    @NotNull
    private final UniverseSettings settings;

    public Universe(String name, String displayName) {
        this(name, displayName, "", UniverseSettings.DEFAULT);
    }

    @PersistenceCreator
    @JsonCreator
    public Universe(String name, String displayName, String shortDescription, UniverseSettings settings) {
        this.name = name;
        this.displayName = displayName;
        this.shortDescription = shortDescription;
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortDescription() {
        return shortDescription;
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
            && getShortDescription().equals(universe.getShortDescription())
            && getSettings().equals(universe.getSettings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDisplayName(), getShortDescription(), getSettings());
    }

}
