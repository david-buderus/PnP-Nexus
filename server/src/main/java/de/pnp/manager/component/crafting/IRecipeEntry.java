package de.pnp.manager.component.crafting;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * The product or an entry in the material list of a {@link CraftingRecipe} or an {@link UpgradeRecipe}.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = IRecipeEntry.ItemRecipeEntry.class),
    @JsonSubTypes.Type(value = IRecipeEntry.MaterialRecipeEntry.class),
    @JsonSubTypes.Type(value = IRecipeEntry.CharacterResourceRecipeEntry.class)
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
    record MaterialRecipeEntry(float amountOfRequiredUnits, @DBRef Material material) implements IRecipeEntry {

    }

    /**
     * An {@link IRecipeEntry} which uses a {@link CharacterResource} of a player.
     */
    record CharacterResourceRecipeEntry(float amountOfRequiredUnits, CharacterResource resource) implements
        IRecipeEntry {

    }

    /**
     * The different character resources which can be used to craft something.
     */
    enum CharacterResource {
        HEALTH, MANA, MENTAL_HEALTH
    }
}
