package de.pnp.manager.component.item.interfaces;

public interface IDefensiveItem extends IDamageableItem, IEquipableItem {

  double getWeight();

  int getArmor();
}
