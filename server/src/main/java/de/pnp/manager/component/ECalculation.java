package de.pnp.manager.component;

import de.pnp.manager.component.upgrade.effect.ItemEffect;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Determines how the attribute gets manipulated by the {@link ItemEffect}.
 */
@Schema(enumAsRef = true)
public enum ECalculation {
    ADDITIVE, MULTIPLICATIVE
}
