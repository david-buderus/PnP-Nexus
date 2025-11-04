package de.pnp.manager.component.character;

import com.google.common.base.MoreObjects;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.Objects;

/**
 * Represents a nation in a universe.
 */
public class Nation extends DatabaseObject implements IUniquelyNamedDataObject {

    @NotBlank
    @Indexed(unique = true)
    private final String name;

    @NotBlank
    private final String description;

    @NotNull
    private final List<@Valid ICharacterTrait> advantageTraits;

    @NotNull
    private final List<@Valid ICharacterTrait> disadvantageTraits;

    public Nation(ObjectId id, String name, String description, List<ICharacterTrait> advantageTraits,
                  List<ICharacterTrait> disadvantageTraits) {
        super(id);
        this.name = name;
        this.description = description;
        this.advantageTraits = advantageTraits;
        this.disadvantageTraits = disadvantageTraits;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ICharacterTrait> getAdvantageTraits() {
        return advantageTraits;
    }

    public List<ICharacterTrait> getDisadvantageTraits() {
        return disadvantageTraits;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Nation nation = (Nation) o;
        return Objects.equals(name, nation.name) && Objects.equals(description, nation.description)
                && Objects.equals(advantageTraits, nation.advantageTraits)
                && Objects.equals(disadvantageTraits, nation.disadvantageTraits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, advantageTraits, disadvantageTraits);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("description", description)
                .add("advantageTraits", advantageTraits)
                .add("disadvantageTraits", disadvantageTraits)
                .toString();
    }
}
