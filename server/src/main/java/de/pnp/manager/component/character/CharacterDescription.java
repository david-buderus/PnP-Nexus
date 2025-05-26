package de.pnp.manager.component.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * The description of a {@link PnPCharacter}.
 */
public record CharacterDescription(@NotBlank String name, @Positive int age, @NotNull String profession,
                                   @NotNull String gender, @NotNull String backstory) {
    
}
