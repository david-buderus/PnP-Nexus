package de.pnp.manager.component.inventory.equipment.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;

/**
 * Represents {@link IEquipment} that can be held in hand.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = ShieldEquipment.class, name = "ShieldEquipment"),
        @JsonSubTypes.Type(value = WeaponEquipment.class, name = "WeaponEquipment"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface IHandheldEquipment extends IDamageableEquipment {

    /**
     * The hit modifier of the underlying item.
     */
    int getHit();

    /**
     * The initiative modifier of the underlying item.
     */
    float getInitiative();
}
