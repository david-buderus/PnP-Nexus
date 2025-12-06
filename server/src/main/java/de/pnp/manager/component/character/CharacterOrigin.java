package de.pnp.manager.component.character;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Bundles the {@link Species} and {@link Nation} of a {@link PnPCharacter}
 */
public record CharacterOrigin(
        @DBRef @NotNull Species species,
        @DBRef Nation nation
) {
}
