package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.ERarity;
import java.util.Objects;

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
