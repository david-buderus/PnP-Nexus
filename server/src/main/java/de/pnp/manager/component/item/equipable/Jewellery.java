package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.Material;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 * A piece of jewellery in the universe.
 */
public class Jewellery extends EquipableItem {

    /**
     * The gem used in this jewellery.
     */
    @NotNull
    private final String gem;

    public Jewellery(ObjectId id, String name, ItemType type, ItemType subtype, String requirement,
        String effect,
        ERarity rarity, int vendorPrice,
        int tier, String description, String note, Material material,
        int upgradeSlots, String gem) {
        super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description,
            note,
            material, upgradeSlots);
        this.gem = gem;
    }

    public String getGem() {
        return gem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Jewellery jewellery = (Jewellery) o;
        return Objects.equals(getGem(), jewellery.getGem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGem());
    }
}
