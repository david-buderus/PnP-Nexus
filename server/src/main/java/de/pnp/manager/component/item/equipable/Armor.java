package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;

public class Armor extends EquipableItem implements IDefensiveItem {

  protected final int armor;
  protected final double weight;

  public Armor(String name, String type, String subtype, String requirement, String effect,
      ERarity rarity, int vendorPrice, int tier, String description,
      String note, Material material, int upgradeSlots, int armor, double weight) {
    super(name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note,
        material, upgradeSlots);
    this.armor = armor;
    this.weight = weight;
  }

  public int getArmor() {
    return armor;
  }

  public double getWeight() {
    return weight;
  }

  @Override
  public int getMaxDurability() {
    return getArmor();
  }

}
