package de.pnp.manager.component.upgrade.effect;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Determines how the attribute gets manipulated by the {@link UpgradeEffect}.
 */
@Schema(enumAsRef = true)
public enum EUpgradeEffectCalculation {
    ADDITIVE, MULTIPLICATIVE
}
