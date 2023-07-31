package de.pnp.manager.component.crafting.bonus;

import de.pnp.manager.component.inventory.ItemStack;

public class AmountMultiplierBonus implements ICraftingBonusEffect {

    private final float factor;

    public AmountMultiplierBonus(float factor) {
        this.factor = factor;
    }

    @Override
    public void applyEffect(ItemStack<?> itemStack) {
        
    }
}
