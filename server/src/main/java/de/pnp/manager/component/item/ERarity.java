package de.pnp.manager.component.item;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The rarity of things in a universe.
 */
@Schema(enumAsRef = true)
public enum ERarity {
    COMMON, UNCOMMON, RARE,
    EPIC, LEGENDARY, GODLIKE
}
