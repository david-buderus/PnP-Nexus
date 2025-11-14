package de.pnp.manager.component.character;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.database.character.PnPCharacterSheetRepository;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a character sheet in a universe.
 */
@Document(PnPCharacterSheetRepository.REPOSITORY_NAME)
public class PnPCharacterSheet extends DatabaseObject implements IUniquelyNamedDataObject {

    @NotBlank
    @Indexed(unique = true)
    private final String name;

    @NotBlank
    private final String sheet;

    public PnPCharacterSheet(ObjectId id, String name, String sheet) {
        super(id);
        this.name = name;
        this.sheet = sheet;
    }

    public String getName() {
        return name;
    }

    public String getSheet() {
        return sheet;
    }
}
