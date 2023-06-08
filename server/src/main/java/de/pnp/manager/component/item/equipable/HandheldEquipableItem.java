package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.interfaces.IHandheldItem;

public abstract class HandheldEquipableItem extends EquipableItem implements IHandheldItem {

  protected final float initiative;
  protected final int hit;

  public HandheldEquipableItem(String name, String type, String subtype, String requirement,
      String effect, ERarity rarity, int vendorPrice, int tier,
      String description, String note, Material material, int upgradeSlots, float initiative,
      int hit) {
    super(name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note,
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
}
