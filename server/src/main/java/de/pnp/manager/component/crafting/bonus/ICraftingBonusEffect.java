package de.pnp.manager.component.crafting.bonus;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.Item;

public interface ICraftingBonusEffect<I extends Item, Stack extends ItemStack<I>> {

    void applyEffect(Stack itemStack);
}
