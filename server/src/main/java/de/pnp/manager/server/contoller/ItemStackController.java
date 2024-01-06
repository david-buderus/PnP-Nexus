package de.pnp.manager.server.contoller;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.interfaces.IDamageableEquipment;
import de.pnp.manager.server.database.UniverseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A controller to create and manipulate {@link ItemStack ItemStacks}.
 */
@Component
public class ItemStackController {

    @Autowired
    private UniverseRepository universeRepository;

    /**
     * Wears down the given {@link IDamageableEquipment equipment} by the amount of usages of the equipment.
     *
     * @return whether the {@link IDamageableEquipment equipment} is not broken and can be used.
     */
    public boolean applyWearByUsage(String universe, IDamageableEquipment equipment, int usages) {
        int wearFactor = universeRepository.getSetting(universe).getWearFactor();
        if (wearFactor == -1) {
            return true;
        }
        equipment.applyWear(usages / (float) wearFactor);

        return equipment.getRelativeDurability() > 0;
    }
}
