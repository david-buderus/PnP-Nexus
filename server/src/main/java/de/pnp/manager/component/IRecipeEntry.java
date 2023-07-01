package de.pnp.manager.component;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * An entry in the material list of a {@link CraftingRecipe} or an {@link UpgradeRecipe}.
 */
public sealed interface IRecipeEntry {

    /**
     * The amount needed for this {@link IRecipeEntry}
     */
    float amount();


    /**
     * A {@link IRecipeEntry} which uses a specific {@link Item}.
     */
    record ItemRecipeEntry(float amount, @DBRef Item item) implements IRecipeEntry {

    }

    /**
     * A {@link IRecipeEntry} which uses a {@link Material}.
     */
    record CraftingRecipeEntry(float amount, @DBRef Material material) implements
        IRecipeEntry {

    }
}
