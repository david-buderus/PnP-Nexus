package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import java.util.Objects;
import org.bson.types.ObjectId;

public abstract class EquipableItem extends Item implements IEquipableItem {

  protected final Material material;
  protected final int upgradeSlots;

  public EquipableItem(ObjectId id, String name, String type, String subtype, String requirement,
      String effect,
      ERarity rarity, int vendorPrice, int tier, String description,
      String note, Material material, int upgradeSlots) {
    super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description,
        note);
    this.material = material;
    this.upgradeSlots = upgradeSlots;
  }

  public Material getMaterial() {
    return material;
  }

  public int getUpgradeSlots() {
    return upgradeSlots;
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
    EquipableItem that = (EquipableItem) o;
    return getUpgradeSlots() == that.getUpgradeSlots() && Objects.equals(getMaterial(),
        that.getMaterial());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getMaterial(), getUpgradeSlots());
  }
}
