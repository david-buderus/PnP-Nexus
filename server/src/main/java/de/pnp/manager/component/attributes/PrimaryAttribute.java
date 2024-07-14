package de.pnp.manager.component.attributes;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.character.PnPCharacter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * A primary attribute of a {@link PnPCharacter}.
 */
public class PrimaryAttribute extends DatabaseObject implements IUniquelyNamedDataObject {

    /**
     * The human-readable name of this attribute.
     * <p>
     * This entry is always unique.
     */
    @Indexed(unique = true)
    @NotBlank
    private final String name;

    /**
     * The human-readable short name of this attribute.
     */
    @Indexed(unique = true)
    @Pattern(regexp = "(?!LVL)[A-Z]+", message = "{character.primary.shortName}")
    private final String shortName;

    public PrimaryAttribute(ObjectId id, String name, String shortName) {
        super(id);
        this.name = name;
        this.shortName = shortName;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrimaryAttribute that = (PrimaryAttribute) o;
        return getName().equals(that.getName()) && getShortName().equals(that.getShortName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getShortName());
    }
}
