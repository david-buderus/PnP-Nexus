package de.pnp.manager.component.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.equipable.EArmorSlot;
import de.pnp.manager.component.item.equipable.Jewellery;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link Equipment} of a {@link PnPCharacter}.
 */
public class CharacterEquipment {

    @NotNull
    @JsonProperty("weaponEquipments")
    private final List<WeaponEquipment> weaponEquipments;

    @NotNull
    @JsonProperty("shieldEquipment")
    private final ShieldEquipment shieldEquipment;

    @NotNull
    @JsonProperty("armor")
    private final Map<EArmorSlot, ArmorEquipment> armor;

    @NotNull
    @JsonProperty("jewellery")
    private final Map<String, List<Equipment<Jewellery>>> jewellery;

    public CharacterEquipment(List<WeaponEquipment> weaponEquipments, ShieldEquipment shieldEquipment, Map<EArmorSlot, ArmorEquipment> armor,
                              Map<String, List<Equipment<Jewellery>>> jewellery) {
        this.weaponEquipments = weaponEquipments;
        this.shieldEquipment = shieldEquipment;
        this.armor = armor;
        this.jewellery = jewellery;
    }

    public List<WeaponEquipment> getWeaponEquipments() {
        return weaponEquipments;
    }

    public ShieldEquipment getShieldEquipment() {
        return shieldEquipment;
    }

    public Optional<ArmorEquipment> getArmor(EArmorSlot slot) {
        return Optional.ofNullable(armor.get(slot));
    }

    public List<Equipment<Jewellery>> getJewellery(String slot) {
        return jewellery.putIfAbsent(slot, new ArrayList<>());
    }
}
