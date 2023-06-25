package de.pnp.manager.server.component;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.TypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.server.database.ItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class to create {@link Item items}.
 */
public class ItemBuilder {

  /**
   * Component wrapper for the {@link ItemBuilder}.
   */
  @Component
  public static class ItemBuilderFactory {

    @Autowired
    private ItemTypeRepository typeRepository;

    /**
     * Builder with default values.
     */
    public ItemBuilder someItem(String universe) {
      return new ItemBuilder(universe, typeRepository);
    }

  }

  private final String universe;
  private final ItemTypeRepository typeRepository;

  private String name;
  private ItemType type;
  private ItemType subtype;
  private String requirement;
  private String effect;
  private ERarity rarity;
  private int vendorPrice;
  private int tier;
  private String description;
  private String note;
  private Material material;
  private int upgradeSlots;
  private int armor;
  private double weight;
  private float initiativeModifier;
  private int hit;
  private int damage;
  private String dice;

  private ItemBuilder(String universe, ItemTypeRepository typeRepository) {
    this.universe = universe;
    this.typeRepository = typeRepository;
    name = "name";
    type = getType("Type");
    subtype = getType("Subtype");
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
    initiativeModifier = 0;
    hit = 0;
    damage = 0;
    dice = "D6";
  }

  /**
   * @see Item#getName()
   */
  public ItemBuilder withName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @see Item#getType()
   */
  public ItemBuilder withType(String type) {
    this.type = getType(type);
    return this;
  }

  /**
   * @see Item#getType()
   */
  public ItemBuilder withType(ItemType type) {
    this.type = type;
    return this;
  }

  /**
   * @see Item#getSubtype()
   */
  public ItemBuilder withSubtype(String subtype) {
    this.subtype = getType(subtype);
    return this;
  }

  /**
   * @see Item#getSubtype()
   */
  public ItemBuilder withSubtype(ItemType subtype) {
    this.subtype = subtype;
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

  /**
   * Creates weapon matching this builder.
   */
  public Weapon buildWeapon() {
    return new Weapon(null, name, type, subtype, requirement, effect, rarity, vendorPrice, tier,
        description, note, material, upgradeSlots, initiativeModifier, hit, damage, dice);
  }

  private ItemType getType(String typeName) {
    return typeRepository.get(universe, typeName).orElse(
        typeRepository.insert(universe, new ItemType(null, typeName, TypeRestriction.ITEM)));
  }
}
