package de.pnp.manager.component.character;

import com.google.common.collect.ListMultimap;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.equipable.Jewellery;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link Equipment} of a {@link PnPCharacter}.
 */
public class CharacterEquipment {

    @NotNull
    private final List<? extends IHandheldEquipment> handheldEquipments;

    @NotNull
    private final Map<String, ArmorEquipment> armor;

    @NotNull
    private final ListMultimap<String, Equipment<Jewellery>> jewellery;

    public CharacterEquipment(List<? extends IHandheldEquipment> handheldEquipments, Map<String, ArmorEquipment> armor,
        ListMultimap<String, Equipment<Jewellery>> jewellery) {
        this.handheldEquipments = handheldEquipments;
        this.armor = armor;
        this.jewellery = jewellery;
    }

    public List<? extends IHandheldEquipment> getHandheldEquipments() {
        return handheldEquipments;
    }

    public Optional<ArmorEquipment> getArmor(String slot) {
        return Optional.ofNullable(armor.get(slot));
    }

    public List<Equipment<Jewellery>> getJewellery(String slot) {
        return jewellery.get(slot);
    }
}
