package de.pnp.manager.component.crafting.bonus;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.UpgradeEffect;

/**
 * An {@link ICraftingBonusEffect} which acts like an {@link Upgrade} which needs no slots.
 */
public class UpgradeCraftingBonus implements ICraftingBonusEffect {

    private final CraftingUpgrade craftingUpgrade;

    public UpgradeCraftingBonus(CraftingUpgrade craftingUpgrade) {
        this.craftingUpgrade = craftingUpgrade;
    }

    @Override
    public void applyEffect(ItemStack<?> itemStack) {
        itemStack.addCraftingUpgrade(getCraftingUpgrade());
    }

    public CraftingUpgrade getCraftingUpgrade() {
        return craftingUpgrade;
    }

    /**
     * The {@link UpgradeEffect effect} with its name.
     */
    public record CraftingUpgrade(String name, UpgradeEffect upgradeEffect) {

    }
}
