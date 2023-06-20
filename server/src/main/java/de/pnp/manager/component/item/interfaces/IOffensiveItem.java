package de.pnp.manager.component.item.interfaces;

/**
 * An item that can be used to deal damage.
 */
public interface IOffensiveItem extends IDamageableItem, IHandheldItem {

  String getDice();

  int getDamage();
}
