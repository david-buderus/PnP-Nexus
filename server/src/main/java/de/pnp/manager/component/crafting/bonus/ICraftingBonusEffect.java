package de.pnp.manager.component.crafting.bonus;

import de.pnp.manager.component.inventory.ItemStack;

public interface ICraftingBonusEffect {

    void applyEffect(ItemStack<?> itemStack);
}
