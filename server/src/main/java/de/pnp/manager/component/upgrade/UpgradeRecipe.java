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
    private final Collection<Upgrade> necessaryUpgrades;

    /**
     * Additional requirements needed to use this {@link UpgradeRecipe}.
     */
    private final String requirement;

    /**
     * Other circumstances needed to use this {@link UpgradeRecipe}.
     */
    private final String otherCircumstances;

    /**
     * The materials needed to use this {@link UpgradeRecipe}
     */
    private final Collection<IRecipeEntry> materials;

    public UpgradeRecipe(ObjectId id, Upgrade upgrade, Collection<Upgrade> necessaryUpgrades, String requirement,
        String otherCircumstances, Collection<IRecipeEntry> materials) {
        super(id);
        this.upgrade = upgrade;
        this.necessaryUpgrades = necessaryUpgrades;
        this.requirement = requirement;
        this.otherCircumstances = otherCircumstances;
        this.materials = materials;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

    public Collection<Upgrade> getNecessaryUpgrades() {
        return necessaryUpgrades;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getOtherCircumstances() {
        return otherCircumstances;
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
        return Objects.equals(getUpgrade(), that.getUpgrade()) && Objects.equals(getNecessaryUpgrades(),
            that.getNecessaryUpgrades()) && Objects.equals(getRequirement(), that.getRequirement())
            && Objects.equals(getOtherCircumstances(), that.getOtherCircumstances()) && Objects.equals(
            getMaterials(), that.getMaterials());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUpgrade(), getNecessaryUpgrades(), getRequirement(), getOtherCircumstances(),
            getMaterials());
    }
}
