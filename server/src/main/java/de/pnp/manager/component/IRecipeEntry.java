package de.pnp.manager.component;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.validation.IsConsumable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * The product or an entry in the material list of a {@link CraftingRecipe} or an {@link UpgradeRecipe}.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = IRecipeEntry.ItemRecipeEntry.class, name = "ItemRecipeEntry"),
    @JsonSubTypes.Type(value = IRecipeEntry.MaterialRecipeEntry.class, name = "MaterialRecipeEntry"),
    @JsonSubTypes.Type(value = IRecipeEntry.CharacterResourceRecipeEntry.class, name = "CharacterResourceRecipeEntry")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public sealed interface IRecipeEntry<T> {

    /**
     * The amount needed for this {@link IRecipeEntry}.
     */
    float amount();

    /**
     * The resource needed for this {@link IRecipeEntry}.
     */
    T resource();

    /**
     * An {@link IRecipeEntry} which uses a specific {@link Item}.
     */
    record ItemRecipeEntry(@Positive float amount, @DBRef @NotNull Item resource) implements
        IRecipeEntry<Item> {

    }

    /**
     * An {@link IRecipeEntry} which uses a {@link Material}.
     */
    record MaterialRecipeEntry(@Positive float amount, @DBRef @NotNull Material resource) implements
        IRecipeEntry<Material> {

    }

    /**
     * An {@link IRecipeEntry} which uses a {@link SecondaryAttribute} of a player.
     */
    record CharacterResourceRecipeEntry(@Positive float amount,
                                        @DBRef @NotNull @IsConsumable SecondaryAttribute resource) implements
        IRecipeEntry<SecondaryAttribute> {

    }
}
