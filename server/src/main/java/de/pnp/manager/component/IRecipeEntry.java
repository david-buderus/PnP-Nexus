package de.pnp.manager.component;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.pnp.manager.component.IRecipeEntry.ItemRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.MaterialRecipeEntry;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * The product or an entry in the material list of a {@link CraftingRecipe} or an {@link UpgradeRecipe}.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = ItemRecipeEntry.class),
    @JsonSubTypes.Type(value = MaterialRecipeEntry.class)
})
public sealed interface IRecipeEntry {

    /**
     * The amount needed for this {@link IRecipeEntry}.
     */
    float amountOfRequiredUnits();


    /**
     * An {@link IRecipeEntry} which uses a specific {@link Item}.
     */
    record ItemRecipeEntry(float amountOfRequiredUnits, @DBRef Item item) implements IRecipeEntry {

    }

    /**
     * An {@link IRecipeEntry} which uses a {@link Material}.
     */
    record MaterialRecipeEntry(float amountOfRequiredUnits, @DBRef Material material) implements
        IRecipeEntry {

    }
}
