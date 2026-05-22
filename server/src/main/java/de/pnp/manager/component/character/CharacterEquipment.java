package de.pnp.manager.component.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.JewelleryEquipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.equipable.EArmorSlot;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The equipment of a {@link PnPCharacter}.
 */
public class CharacterEquipment {

    @NotNull
    @JsonProperty("weapons")
    private final List<WeaponEquipment> weapons;

    @NotNull
    @JsonProperty("shields")
    private final List<ShieldEquipment> shields;

    @NotNull
    @JsonProperty("armor")
    private final Map<EArmorSlot, ArmorEquipment> armor;

    @NotNull
    @JsonProperty("jewellery")
    private final Map<String, List<JewelleryEquipment>> jewellery;

    public CharacterEquipment(List<WeaponEquipment> weapons, List<ShieldEquipment> shields, Map<EArmorSlot,
            ArmorEquipment> armor, Map<String, List<JewelleryEquipment>> jewellery) {
        this.weapons = weapons;
        this.shields = shields;
        this.armor = armor;
        this.jewellery = jewellery;
    }

    public List<WeaponEquipment> getWeapons() {
        return weapons;
    }

    public List<ShieldEquipment> getShields() {
        return shields;
    }

    public Optional<ArmorEquipment> getArmor(EArmorSlot slot) {
        return Optional.ofNullable(armor.get(slot));
    }

    public List<JewelleryEquipment> getJewellery(String slot) {
        return jewellery.putIfAbsent(slot, new ArrayList<>());
    }
}
