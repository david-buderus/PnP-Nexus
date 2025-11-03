package de.pnp.manager.component.character;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * The level information of a {@link PnPCharacter}.
 */
public record CharacterLevel(@Positive int level, @PositiveOrZero int experience, @PositiveOrZero int skillPoints) {

}
