package de.pnp.manager.component.item.equipable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The slots where {@link Armor} can be worn.
 */
@Schema(enumAsRef = true)
public enum EArmorSlot {
    HEAD, BODY, ARMS, LEGS;
}
