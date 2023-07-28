package de.pnp.manager.component.upgrade;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IRecipeEntry;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * A recipe to perform an {@link Upgrade}.
 */
public class UpgradeRecipe extends DatabaseObject {

    /**
     * The product of this {@link UpgradeRecipe}.
     */
    @DBRef
    private final Upgrade upgrade;

    /**
     * The {@link Upgrade upgrads} which already need to be present on the item to use this {@link UpgradeRecipe}.
     */
    @DBRef
    private final Collection<Upgrade> requiredUpgrades;

    /**
     * Additional requirements needed to use this {@link UpgradeRecipe}.
     */
    private final String requirement;

    /**
     * The materials needed to use this {@link UpgradeRecipe}
     */
    private final Collection<IRecipeEntry> materials;

    public UpgradeRecipe(ObjectId id, Upgrade upgrade, Collection<Upgrade> requiredUpgrades, String requirement,
        Collection<IRecipeEntry> materials) {
        super(id);
        this.upgrade = upgrade;
        this.requiredUpgrades = requiredUpgrades;
        this.requirement = requirement;
        this.materials = materials;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

    public Collection<Upgrade> getRequiredUpgrades() {
        return requiredUpgrades;
    }

    public String getRequirement() {
        return requirement;
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
        UpgradeRecipe that = (UpgradeRecipe) o;
        return Objects.equals(getUpgrade(), that.getUpgrade()) && Objects.equals(getRequiredUpgrades(),
            that.getRequiredUpgrades()) && Objects.equals(getRequirement(), that.getRequirement())
            && Objects.equals(getMaterials(), that.getMaterials());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUpgrade(), getRequiredUpgrades(), getRequirement(), getMaterials());
    }
}