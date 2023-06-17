package de.pnp.manager.server.component;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.EquipableItem;

/**
 * Helper class to create {@link Item items}.
 */
public class ItemBuilder {

  protected String name;
  protected String type;
  protected String subtype;
  protected String requirement;
  protected String effect;
  protected ERarity rarity;
  protected int vendorPrice;
  protected int tier;
  protected String description;
  protected String note;
  protected Material material;
  protected int upgradeSlots;
  protected int armor;
  protected double weight;

  public static ItemBuilder someItem() {
    return new ItemBuilder();
  }

  protected ItemBuilder() {
    name = "name";
    type = "type";
    subtype = "subtype";
    requirement = "requirement";
    effect = "effect";
    rarity = ERarity.COMMON;
    vendorPrice = 10;
    tier = 1;
    description = "description";
    note = "note";
    material = null;
    upgradeSlots = 0;
    armor = 1;
    weight = 0;
  }

  /**
   * @see Item#getName()
   */
  public ItemBuilder withName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @see EquipableItem#getMaterial()
   */
  public ItemBuilder withMaterial(Material material) {
    this.material = material;
    return this;
  }

  /**
   * Creates an item matching this builder.
   */
  public Item buildItem() {
    return new Item(null, name, type, subtype, requirement, effect, rarity, vendorPrice, tier,
        description, note);
  }

  /**
   * Creates armor matching this builder.
   */
  public Armor buildArmor() {
    return new Armor(null, name, type, subtype, requirement, effect, rarity, vendorPrice, tier,
        description, note, material, upgradeSlots, armor, weight);
  }
}
