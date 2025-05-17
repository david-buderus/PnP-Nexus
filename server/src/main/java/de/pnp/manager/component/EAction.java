package de.pnp.manager.component;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Determines which actions can be done in combat.
 */
@Schema(enumAsRef = true)
public enum EAction {
    ACTION, BONUS_ACTION, MOVEMENT_ACTION, REACTION
}
