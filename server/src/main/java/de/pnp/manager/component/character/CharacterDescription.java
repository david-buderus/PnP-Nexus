package de.pnp.manager.component.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The description of a {@link PnPCharacter}.
 */
public record CharacterDescription(
        @NotBlank String name,
        @NotNull String profession,
        @NotNull String gender,
        @NotNull String backstory,
        @NotNull String appearance,
        @NotNull String personality,
        @NotNull String goals,
        @NotNull String deficits,
        @NotNull String affiliations
) {

}
