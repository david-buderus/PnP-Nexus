package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import java.util.Objects;
import org.bson.types.ObjectId;

public class Shield extends HandheldEquipableItem implements IDefensiveItem {

  protected final double weight;
  protected final int armor;

  public Shield(ObjectId id, String name, String type, String subtype, String requirement,
      String effect,
      ERarity rarity, int vendorPrice, int tier, String description,
      String note, Material material, int upgradeSlots, float initiative,
      int hit, double weight, int armor) {
    super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description,
        note,
        material, upgradeSlots, initiative, hit);
    this.weight = weight;
    this.armor = armor;
  }

  public double getWeight() {
    return weight;
  }

  public int getArmor() {
    return armor;
  }

  @Override
  public int getMaxDurability() {
    return getArmor();
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
    Shield that = (Shield) o;
    return Double.compare(that.getWeight(), getWeight()) == 0
        && getArmor() == that.getArmor();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getWeight(), getArmor());
  }
}
