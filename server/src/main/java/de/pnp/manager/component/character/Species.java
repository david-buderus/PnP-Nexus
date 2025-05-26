package de.pnp.manager.component.character;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * Represents a species in a universe.
 */
public class Species extends DatabaseObject implements IUniquelyNamedDataObject {

    @NotBlank
    @Indexed(unique = true)
    private final String name;

    @NotBlank
    private final String description;

    /**
     * If the species can be played by players.
     */
    private final boolean playable;

    @NotNull
    private final List<@Valid ICharacterTrait> advantageTraits;

    @NotNull
    private final List<@Valid ICharacterTrait> disadvantageTraits;

    @DBRef
    @NotNull
    private final List<Nation> nations;

    public Species(ObjectId id, String name, String description, boolean playable, List<ICharacterTrait> advantageTraits,
                   List<ICharacterTrait> disadvantageTraits, List<Nation> nations) {
        super(id);
        this.name = name;
        this.description = description;
        this.playable = playable;
        this.advantageTraits = advantageTraits;
        this.disadvantageTraits = disadvantageTraits;
        this.nations = nations;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPlayable() {
        return playable;
    }

    public List<ICharacterTrait> getAdvantageTraits() {
        return advantageTraits;
    }

    public List<ICharacterTrait> getDisadvantageTraits() {
        return disadvantageTraits;
    }

    public List<Nation> getNations() {
        return nations;
    }
}
