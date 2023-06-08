package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.interfaces.IEquipableItem;

public abstract class EquipableItem extends Item implements IEquipableItem {

  protected final Material material;
  protected final int upgradeSlots;

  public EquipableItem(String name, String type, String subtype, String requirement, String effect,
      ERarity rarity, int vendorPrice, int tier, String description,
      String note, Material material, int upgradeSlots) {
    super(name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note);
    this.material = material;
    this.upgradeSlots = upgradeSlots;
  }

  public Material getMaterial() {
    return material;
  }

  public int getUpgradeSlots() {
    return upgradeSlots;
  }
}
