package de.pnp.manager.component.crafting;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * A crafting recipe in the universe.
 */
public class CraftingRecipe extends DatabaseObject {

    /**
     * The profession needed to use this {@link CraftingRecipe}.
     */
    private final String profession;

    /**
     * Additional requirements needed to use this {@link CraftingRecipe}.
     */
    private final String requirement;

    /**
     * Other circumstances needed to use this {@link CraftingRecipe}.
     */
    private final String otherCircumstances;

    /**
     * The product of this {@link CraftingRecipe}
     */
    private final CraftingItem product;

    /**
     * The side-product of this {@link CraftingRecipe}
     */
    private final CraftingItem sideProduct;

    /**
     * The materials needed to use this {@link CraftingRecipe}
     */
    private final Collection<ICraftingEntry> materials;

    public CraftingRecipe(ObjectId id, String profession, String requirement,
        String otherCircumstances, CraftingItem product, CraftingItem sideProduct,
        Collection<ICraftingEntry> materials) {
        super(id);
        this.profession = profession;
        this.requirement = requirement;
        this.otherCircumstances = otherCircumstances;
        this.product = Objects.requireNonNull(product);
        this.sideProduct = sideProduct;
        this.materials = materials;
    }


    public String getProfession() {
        return profession;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getOtherCircumstances() {
        return otherCircumstances;
    }

    public CraftingItem getProduct() {
        return product;
    }

    public CraftingItem getSideProduct() {
        return sideProduct;
    }

    public Collection<ICraftingEntry> getMaterials() {
        return materials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CraftingRecipe that = (CraftingRecipe) o;
        return Objects.equals(getProfession(), that.getProfession())
            && Objects.equals(getRequirement(), that.getRequirement())
            && Objects.equals(getOtherCircumstances(), that.getOtherCircumstances())
            && getProduct().equals(that.getProduct()) && Objects.equals(getSideProduct(),
            that.getSideProduct()) && Objects.equals(getMaterials(), that.getMaterials());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProfession(), getRequirement(), getOtherCircumstances(), getProduct(),
            getSideProduct(), getMaterials());
    }

    /**
     * An entry in the {@link CraftingRecipe#getMaterials() material list} of a {@link CraftingRecipe}.
     */
    public sealed interface ICraftingEntry {

        /**
         * The amount needed for this {@link ICraftingEntry}
         */
        float amount();
    }

    /**
     * A {@link ICraftingEntry} which uses a specific {@link Item}.
     */
    public record CraftingItem(float amount, @DBRef Item item) implements ICraftingEntry {

    }

    /**
     * A {@link ICraftingEntry} which uses a {@link Material}.
     */
    public record CraftingMaterial(float amount, @DBRef Material material) implements
        ICraftingEntry {

    }
}
