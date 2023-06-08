package de.pnp.manager.component.item.interfaces;

public interface IOffensiveItem extends IDamageableItem, IHandheldItem {

  String getDice();

  int getDamage();
}
