package de.pnp.manager.server.contoller;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.interfaces.IDamageableEquipment;
import de.pnp.manager.component.universe.ItemSettings;
import de.pnp.manager.server.database.universe.UniverseSettingsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A controller to create and manipulate {@link ItemStack ItemStacks}.
 */
@Component
public class ItemStackController {

    @Autowired
    private UniverseSettingsRepository settingsRepository;

    /**
     * Wears down the given {@link IDamageableEquipment equipment} by the amount of usages of the equipment.
     *
     * @return whether the {@link IDamageableEquipment equipment} is not broken and can be used.
     */
    public boolean applyWearByUsage(ObjectId universe, IDamageableEquipment equipment, int usages) {
        int wearFactor = settingsRepository.getSettings(universe, ItemSettings.class).getWearFactor();
        if (wearFactor < 1) {
            return true;
        }
        equipment.applyWear(usages / (float) wearFactor);

        return equipment.getRelativeDurability() > 0;
    }
}
