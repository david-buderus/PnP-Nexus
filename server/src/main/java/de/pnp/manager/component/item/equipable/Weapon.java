package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.interfaces.IOffensiveItem;

public class Weapon extends HandheldEquipableItem implements IOffensiveItem {

  protected final int damage;
  protected final String dice;

  public Weapon(String name, String type, String subtype, String requirement, String effect,
      ERarity rarity, int vendorPrice, int tier, String description, String note,
      Material material,
      int upgradeSlots, float initiative, int hit, int damage, String dice) {
    super(name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note,
        material, upgradeSlots, initiative, hit);
    this.damage = damage;
    this.dice = dice;
  }

  public int getDamage() {
    return damage;
  }

  public String getDice() {
    return dice;
  }

  @Override
  public int getMaxDurability() {
    return getDamage();
  }
}
