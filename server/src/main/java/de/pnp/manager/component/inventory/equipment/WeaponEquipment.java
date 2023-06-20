package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.interfaces.IOffensiveItem;

public class WeaponEquipment extends DamageableEquipment<IOffensiveItem> implements
    IHandheldEquipment {

  public WeaponEquipment(IOffensiveItem item, int durability) {
    super(item, durability);
  }

  public int getDamage() {
    return Math.round(getItem().getDamage() * getRelativeDurability());
  }

  @Override
  public int getHit() {
    return getItem().getHit();
  }

  @Override
  public float getInitiative() {
    return getItem().getInitiative();
  }
}
