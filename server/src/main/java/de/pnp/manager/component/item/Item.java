package de.pnp.manager.component.item;

import de.pnp.manager.component.item.interfaces.IItem;

public class Item implements IItem {

  protected final String name;
  protected final String type;
  protected final String subtype;
  protected final String requirement;
  protected final String effect;
  protected final ERarity rarity;
  protected final int vendorPrice;
  protected final int tier;
  protected final String description;
  protected final String note;


  public Item(String name, String type, String subtype, String requirement, String effect,
      ERarity rarity, int vendorPrice, int tier, String description, String note) {
    this.name = name;
    this.type = type;
    this.subtype = subtype;
    this.requirement = requirement;
    this.effect = effect;
    this.rarity = rarity;
    this.vendorPrice = vendorPrice;
    this.tier = tier;
    this.description = description;
    this.note = note;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getSubtype() {
    return subtype;
  }

  @Override
  public String getRequirement() {
    return requirement;
  }

  @Override
  public String getEffect() {
    return effect;
  }

  @Override
  public ERarity getRarity() {
    return rarity;
  }

  @Override
  public int getVendorPrice() {
    return vendorPrice;
  }

  @Override
  public int getTier() {
    return tier;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getNote() {
    return note;
  }
}
