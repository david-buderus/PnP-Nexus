package de.pnp.manager.component.attributes;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.character.Character;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * A secondary attribute of a {@link Character}.
 */
public class SecondaryAttribute extends DatabaseObject implements IUniquelyNamedDataObject {

    /**
     * The human-readable name of this attribute.
     * <p>
     * This entry is always unique.
     */
    @Indexed(unique = true)
    @NotBlank
    private final String name;

    private final boolean consumable;

    @NotEmpty
    private final Collection<PrimaryAttributeDependency> primaryAttributeDependencies;

    public SecondaryAttribute(ObjectId id, String name, boolean consumable,
        Collection<PrimaryAttributeDependency> primaryAttributeDependencies) {
        super(id);
        this.name = name;
        this.consumable = consumable;
        this.primaryAttributeDependencies = primaryAttributeDependencies;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public Collection<PrimaryAttributeDependency> getPrimaryAttributeDependencies() {
        return primaryAttributeDependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecondaryAttribute that = (SecondaryAttribute) o;
        return isConsumable() == that.isConsumable() && Objects.equals(getName(), that.getName())
            && Objects.equals(getPrimaryAttributeDependencies(), that.getPrimaryAttributeDependencies());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), isConsumable(), getPrimaryAttributeDependencies());
    }

    /**
     * Dependencies of a {@link SecondaryAttribute} to a {@link PrimaryAttribute}.
     */
    public record PrimaryAttributeDependency(double factor, @DBRef @NotNull PrimaryAttribute primaryAttribute) {

    }
}
