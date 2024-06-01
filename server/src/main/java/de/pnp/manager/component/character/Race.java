package de.pnp.manager.component.character;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Race extends DatabaseObject implements IUniquelyNamedDataObject {

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    @NotNull
    private final List<@Valid ICharacterTrait> advantageTraits;

    @NotNull
    private final List<@Valid ICharacterTrait> disadvantageTraits;

    @DBRef
    @NotNull
    private final List<Nation> nations;

    public Race(ObjectId id, String name, String description, List<ICharacterTrait> advantageTraits,
        List<ICharacterTrait> disadvantageTraits, List<Nation> nations) {
        super(id);
        this.name = name;
        this.description = description;
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
