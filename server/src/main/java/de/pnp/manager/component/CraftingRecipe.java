package de.pnp.manager.component;

import de.pnp.manager.component.IRecipeEntry.ItemRecipeEntry;
import de.pnp.manager.server.database.CraftingRecipeRepository;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A crafting recipe in the universe.
 */
@Document(CraftingRecipeRepository.REPOSITORY_NAME)
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
    @Nullable
    private final ItemRecipeEntry sideProduct;

    /**
     * The materials needed to use this {@link CraftingRecipe}
     */
    private final Collection<IRecipeEntry> materials;

    public CraftingRecipe(ObjectId id, String profession, String requirement,
        String otherCircumstances, ItemRecipeEntry product, @Nullable ItemRecipeEntry sideProduct,
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

    @Nullable
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
