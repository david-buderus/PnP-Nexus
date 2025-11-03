package de.pnp.manager;

import de.pnp.manager.server.database.TagRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A tag of some item/spell/... in the universe.
 *
 * @param name The unique name of the tag.
 */
@Document(TagRepository.REPOSITORY_NAME)
public record Tag(@Id @NotBlank String name) {

    public Tag(String name) {
        this.name = name;
    }

    /**
     * Creates a {@link Tag} from the given string
     */
    public static Tag from(String s) {
        return new Tag(s);
    }
}
