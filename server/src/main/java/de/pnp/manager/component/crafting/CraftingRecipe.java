package de.pnp.manager.component.crafting;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.crafting.IRecipeEntry.ItemRecipeEntry;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;

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
    private final ItemRecipeEntry product;

    /**
     * The side-product of this {@link CraftingRecipe}
     */
    private final ItemRecipeEntry sideProduct;

    /**
     * The materials needed to use this {@link CraftingRecipe}
     */
    private final Collection<IRecipeEntry> materials;

    public CraftingRecipe(ObjectId id, String profession, String requirement,
        String otherCircumstances, ItemRecipeEntry product, ItemRecipeEntry sideProduct,
        Collection<IRecipeEntry> materials) {
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

    public ItemRecipeEntry getProduct() {
        return product;
    }

    public ItemRecipeEntry getSideProduct() {
        return sideProduct;
    }

    public Collection<IRecipeEntry> getMaterials() {
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
}
