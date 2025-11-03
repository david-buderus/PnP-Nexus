package de.pnp.manager.component.inventory.equipment.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;

/**
 * Represents {@link IEquipment}.
 *
 * @see de.pnp.manager.component.inventory.equipment.Equipment
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = Equipment.class, name = "Equipment"),
        @JsonSubTypes.Type(value = ShieldEquipment.class, name = "ShieldEquipment"),
        @JsonSubTypes.Type(value = ArmorEquipment.class, name = "ArmorEquipment"),
        @JsonSubTypes.Type(value = WeaponEquipment.class, name = "WeaponEquipment"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface IEquipment {

}
