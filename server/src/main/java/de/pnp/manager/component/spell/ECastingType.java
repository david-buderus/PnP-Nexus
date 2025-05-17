package de.pnp.manager.component.spell;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Determines how a spell needs to be cast.
 */
@Schema(enumAsRef = true)
public enum ECastingType {
    VERBAL, SOMATIC
}
