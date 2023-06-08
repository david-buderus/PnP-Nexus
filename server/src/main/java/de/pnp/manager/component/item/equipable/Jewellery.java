package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.ERarity;

public class Jewellery extends EquipableItem {

  private final String gem;

  public Jewellery(String name, String type, String subtype, String requirement, String effect,
      ERarity rarity, int vendorPrice,
      int tier, String description, String note, Material material,
      int upgradeSlots, String gem) {
    super(name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note,
        material, upgradeSlots);
    this.gem = gem;
  }

  public String getGem() {
    return gem;
  }
}
