package de.pnp.manager.component.item.interfaces;

import de.pnp.manager.component.item.ERarity;

public interface IItem {

  String getName();

  String getType();

  String getSubtype();

  String getRequirement();

  String getEffect();

  ERarity getRarity();

  int getVendorPrice();

  int getTier();

  String getDescription();

  String getNote();
}

