package de.pnp.manager.component.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.JewelleryEquipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.equipable.EArmorSlot;
import jakarta.validation.constraints.NotNull;

import java.util.*;

/**
 * The equipment of a {@link PnPCharacter}.
 */
public class CharacterEquipment {

    @NotNull
    @JsonProperty("weapons")
    private final List<WeaponEquipment> weapons;


    @NotNull
    @JsonProperty("fallbackWeapons")
    private final List<WeaponEquipment> fallbackWeapons;

    @NotNull
    @JsonProperty("shields")
    private final List<ShieldEquipment> shields;

    @NotNull
    @JsonProperty("fallbackShields")
    private final List<ShieldEquipment> fallbackShields;

    @NotNull
    @JsonProperty("armor")
    private final Map<EArmorSlot, ArmorEquipment> armor;

    @NotNull
    @JsonProperty("jewellery")
    private final Map<String, List<JewelleryEquipment>> jewellery;

    public CharacterEquipment(List<WeaponEquipment> weapons, List<WeaponEquipment> fallbackWeapons,
                              List<ShieldEquipment> shields, List<ShieldEquipment> fallbackShields,
                              Map<EArmorSlot, ArmorEquipment> armor, Map<String, List<JewelleryEquipment>> jewellery) {
        this.weapons = weapons;
        this.fallbackWeapons = fallbackWeapons;
        this.shields = shields;
        this.fallbackShields = fallbackShields;
        this.armor = armor;
        this.jewellery = jewellery;
    }

    public List<WeaponEquipment> getWeapons() {
        return weapons;
    }

    public List<WeaponEquipment> getFallbackWeapons() {
        return fallbackWeapons;
    }

    public List<ShieldEquipment> getShields() {
        return shields;
    }

    public List<ShieldEquipment> getFallbackShields() {
        return fallbackShields;
    }

    /**
     * Returns the equipped armor for the given slot.
     */
    public Optional<ArmorEquipment> getArmor(EArmorSlot slot) {
        return Optional.ofNullable(armor.get(slot));
    }

    /**
     * Returns the equipped jewellery for the given slot.
     */
    public List<JewelleryEquipment> getJewellery(String slot) {
        return jewellery.putIfAbsent(slot, new ArrayList<>());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterEquipment that = (CharacterEquipment) o;
        return Objects.equals(weapons, that.weapons) && Objects.equals(fallbackWeapons, that.fallbackWeapons)
                && Objects.equals(shields, that.shields) && Objects.equals(fallbackShields, that.fallbackShields)
                && Objects.equals(armor, that.armor) && Objects.equals(jewellery, that.jewellery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weapons, fallbackWeapons, shields, fallbackShields, armor, jewellery);
    }
}
