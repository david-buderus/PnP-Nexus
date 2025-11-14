package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.database.universe.UniverseRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * The description of a universe.
 */
@Document(UniverseRepository.REPOSITORY_NAME)
public class Universe extends DatabaseObject {

    /**
     * The human-readable name of this universe.
     */
    @NotBlank
    @Size(min = 3, max = 64)
    private final String displayName;

    @NotNull
    private final String shortDescription;

    @NotNull
    private final String description;

    public Universe(ObjectId id, String displayName) {
        this(id, displayName, "", "");
    }

    @PersistenceCreator
    @JsonCreator
    public Universe(ObjectId id, String displayName, String shortDescription, String description) {
        super(id);
        this.displayName = displayName;
        this.shortDescription = shortDescription;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
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
        return Objects.equals(getId(), universe.getId()) && getDisplayName().equals(universe.getDisplayName())
                && getShortDescription().equals(universe.getShortDescription()) && getDescription().equals(
                universe.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDisplayName(), getShortDescription(), getDescription());
    }
}
