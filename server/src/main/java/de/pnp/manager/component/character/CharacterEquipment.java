package de.pnp.manager.component.character;

import de.pnp.manager.component.inventory.equipment.DefensiveEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.equipable.Jewellery;
import java.util.Set;

public class CharacterEquipment {

    IHandheldEquipment primary;
    IHandheldEquipment secondary;

    DefensiveEquipment helmet;
    DefensiveEquipment bodyArmor;
    DefensiveEquipment legs;
    DefensiveEquipment boots;

    Set<Equipment<Jewellery>> jewellerySet;
}
