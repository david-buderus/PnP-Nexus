package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.interfaces.IHandheldItem;
import java.util.Objects;
import org.bson.types.ObjectId;

public abstract class HandheldEquipableItem extends EquipableItem implements IHandheldItem {

  protected final float initiative;
  protected final int hit;

  public HandheldEquipableItem(ObjectId id, String name, String type, String subtype,
      String requirement,
      String effect, ERarity rarity, int vendorPrice, int tier,
      String description, String note, Material material, int upgradeSlots, float initiative,
      int hit) {
    super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description,
        note,
        material, upgradeSlots);
    this.initiative = initiative;
    this.hit = hit;
  }

  public float getInitiative() {
    return initiative;
  }

  public int getHit() {
    return hit;
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
    HandheldEquipableItem that = (HandheldEquipableItem) o;
    return Float.compare(that.getInitiative(), getInitiative()) == 0
        && getHit() == that.getHit();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getInitiative(), getHit());
  }
}
