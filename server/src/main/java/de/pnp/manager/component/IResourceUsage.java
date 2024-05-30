package de.pnp.manager.component;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.IResourceUsage.ItemUsage;
import de.pnp.manager.component.IResourceUsage.MaterialUsage;
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
    @JsonSubTypes.Type(value = ItemUsage.class, name = "ItemUsage"),
    @JsonSubTypes.Type(value = MaterialUsage.class, name = "MaterialUsage"),
    @JsonSubTypes.Type(value = CharacterResourceUsage.class, name = "CharacterResourceUsage")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public sealed interface IResourceUsage<T> {

    /**
     * The amount needed for this {@link IResourceUsage}.
     */
    float amount();

    /**
     * The resource needed for this {@link IResourceUsage}.
     */
    T resource();

    /**
     * An {@link IResourceUsage} which uses a specific {@link Item}.
     */
    record ItemUsage(@Positive float amount, @DBRef @NotNull Item resource) implements
        IResourceUsage<Item> {

    }

    /**
     * An {@link IResourceUsage} which uses a {@link Material}.
     */
    record MaterialUsage(@Positive float amount, @DBRef @NotNull Material resource) implements
        IResourceUsage<Material> {

    }

    /**
     * An {@link IResourceUsage} which uses a {@link SecondaryAttribute} of a player.
     */
    record CharacterResourceUsage(@Positive float amount,
                                  @DBRef @NotNull @IsConsumable SecondaryAttribute resource) implements
        IResourceUsage<SecondaryAttribute> {

    }
}
