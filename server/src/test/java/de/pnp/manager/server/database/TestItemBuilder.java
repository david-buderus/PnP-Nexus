package de.pnp.manager.server.database;

import de.pnp.manager.component.Universe;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.equipable.HandheldEquipableItem;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class to create {@link Item items}.
 */
public class TestItemBuilder {

    /**
     * Component wrapper for the {@link TestItemBuilder}.
     */
    @Component
    public static class TestItemBuilderFactory {

        @Autowired
        private ItemTypeRepository typeRepository;

        /**
         * Builder with default values.
         */
        public TestItemBuilder createItemBuilder(String universe) {
            return new TestItemBuilder(universe, typeRepository);
        }
    }

    /**
     * Returns a builder without a link to a {@link Universe} or database.
     */
    public static TestItemBuilder createItemBuilder() {
        return new TestItemBuilder(null, null);
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
    private float weight;
    private float initiativeModifier;
    private int hit;
    private int damage;
    private String dice;
    private int maximumStackSize;
    private int minimumStackSize;

    private TestItemBuilder(String universe, ItemTypeRepository typeRepository) {
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
        maximumStackSize = 100;
        minimumStackSize = 0;
    }

    /**
     * @see Item#getName()
     */
    public TestItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see Item#getType()
     */
    public TestItemBuilder withType(String type) {
        this.type = getType(type);
        return this;
    }

    /**
     * @see Item#getType()
     */
    public TestItemBuilder withType(ItemType type) {
        this.type = type;
        return this;
    }

    /**
     * @see Item#getSubtype()
     */
    public TestItemBuilder withSubtype(String subtype) {
        this.subtype = getType(subtype);
        return this;
    }

    /**
     * @see Item#getSubtype()
     */
    public TestItemBuilder withSubtype(ItemType subtype) {
        this.subtype = subtype;
        return this;
    }

    /**
     * @see EquipableItem#getMaterial()
     */
    public TestItemBuilder withMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * @see Item#getMaximumStackSize()
     */
    public TestItemBuilder withMaximumStackSize(int maximumStackSize) {
        this.maximumStackSize = maximumStackSize;
        return this;
    }

    /**
     * @see Item#getMinimumStackSize()
     */
    public TestItemBuilder withMinimumStackSize(int minimumStackSize) {
        this.minimumStackSize = minimumStackSize;
        return this;
    }

    /**
     * @see EquipableItem#getUpgradeSlots()
     */
    public TestItemBuilder withUpgradeSlots(int upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
        return this;
    }

    /**
     * @see Weapon#getDamage()
     */
    public TestItemBuilder withDamage(int damage) {
        this.damage = damage;
        return this;
    }

    /**
     * @see HandheldEquipableItem#getHit()
     */
    public TestItemBuilder withHit(int hit) {
        this.hit = hit;
        return this;
    }

    /**
     * @see HandheldEquipableItem#getInitiative()
     */
    public TestItemBuilder withInitiative(int initiative) {
        this.initiativeModifier = initiative;
        return this;
    }

    /**
     * @see IDefensiveItem#getArmor()
     */
    public TestItemBuilder withArmor(int armor) {
        this.armor = armor;
        return this;
    }

    /**
     * @see IDefensiveItem#getWeight()
     */
    public TestItemBuilder withWeight(int weight) {
        this.weight = weight;
        return this;
    }

    /**
     * Creates an item matching this builder.
     */
    public Item buildItem() {
        return new Item(null, name, type, subtype, requirement, effect, rarity, vendorPrice, tier,
            description, note, maximumStackSize, minimumStackSize);
    }

    /**
     * Creates armor matching this builder.
     */
    public Armor buildArmor() {
        return new Armor(null, name, type, subtype, requirement, effect, rarity, vendorPrice, tier,
            description, note, material, upgradeSlots, armor, weight, 1, 1);
    }

    /**
     * Creates weapon matching this builder.
     */
    public Weapon buildWeapon() {
        return new Weapon(null, name, type, subtype, requirement, effect, rarity, vendorPrice, tier,
            description, note, material, upgradeSlots, initiativeModifier, hit, damage, dice, 1, 1);
    }

    /**
     * Creates shield matching this builder.
     */
    public Shield buildShield() {
        return new Shield(null, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note,
            material, upgradeSlots, initiativeModifier, hit, weight, armor, 1, 1);
    }

    /**
     * Creates jewellery matching this builder.
     */
    public Jewellery buildJewellery() {
        return new Jewellery(null, name, type, subtype, requirement, effect, rarity, vendorPrice, tier,
            description, note, material, upgradeSlots, 1, 1);
    }

    private ItemType getType(String typeName) {
        if (typeRepository == null) {
            return new ItemType(null, typeName, ETypeRestriction.ITEM);
        }
        return typeRepository.get(universe, typeName).orElse(
            typeRepository.insert(universe, new ItemType(null, typeName, ETypeRestriction.ITEM)));
    }
}
