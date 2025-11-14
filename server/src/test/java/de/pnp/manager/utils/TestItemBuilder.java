package de.pnp.manager.utils;

import de.pnp.manager.Tag;
import de.pnp.manager.component.Dice;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.*;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        private ItemRepository itemRepository;

        @Autowired
        private MaterialRepository materialRepository;

        /**
         * Builder with default values.
         */
        public TestItemBuilder createItemBuilder(ObjectId universe) {
            return new TestItemBuilder(universe, itemRepository, materialRepository);
        }
    }

    /**
     * Returns a builder without a link to a {@link Universe} or database.
     */
    public static TestItemBuilder createItemBuilder() {
        return new TestItemBuilder(null, null, null);
    }

    private final ObjectId universe;

    private String name;
    private final Set<Tag> tags;
    private String requirement;
    private String effect;
    private ERarity rarity;
    private int vendorPrice;
    private int tier;
    private String description;
    private String note;
    private Material material;
    private int upgradeSlots;
    private EArmorSlot armorSlot;
    private int armor;
    private int protection;
    private float weight;
    private float initiativeModifier;
    private int hit;
    private int damage;
    private Dice dice;
    private int maximumStackSize;
    private int minimumStackSize;

    private boolean shouldGetPersisted;

    private final ItemRepository itemRepository;
    private final MaterialRepository materialRepository;

    private TestItemBuilder(ObjectId universe, ItemRepository itemRepository,
                            MaterialRepository materialRepository) {
        this.universe = universe;
        this.itemRepository = itemRepository;
        this.materialRepository = materialRepository;
        name = "name";
        tags = new HashSet<de.pnp.manager.@NotNull Tag>();
        requirement = "requirement";
        effect = "effect";
        rarity = ERarity.COMMON;
        vendorPrice = 10;
        tier = 1;
        description = "description";
        note = "note";
        material = null;
        upgradeSlots = 0;
        armorSlot = EArmorSlot.BODY;
        armor = 1;
        protection = 0;
        weight = 0;
        initiativeModifier = 0;
        hit = 0;
        damage = 0;
        dice = Dice.simpleDice(6);
        maximumStackSize = 100;
        minimumStackSize = 0;
        shouldGetPersisted = false;
    }

    /**
     * @see Item#getName()
     */
    public TestItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see Item#getTags()
     */
    public TestItemBuilder withTags(String... tags) {
        this.tags.addAll(Arrays.stream(tags).map(Tag::from).collect(Collectors.toSet()));
        return this;
    }

    /**
     * @see EquipableItem#getMaterial()
     */
    public TestItemBuilder withMaterial(String material) {
        this.material = getMaterial(material);
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
     * @see Item#getTier()
     */
    public TestItemBuilder withTier(int tier) {
        this.tier = tier;
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
     * @see Weapon#getDice()
     */
    public TestItemBuilder withDice(Dice dice) {
        this.dice = dice;
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
     * @see Armor#getArmorSlot()
     */
    public TestItemBuilder withArmorSlot(EArmorSlot armorSlot) {
        this.armorSlot = armorSlot;
        return this;
    }

    /**
     * @see Item#getEffect()
     */
    public TestItemBuilder withEffect(String effect) {
        this.effect = effect;
        return this;
    }

    /**
     * @see Item#getDescription()
     */
    public TestItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @see Item#getRarity()
     */
    public TestItemBuilder withRarity(ERarity rarity) {
        this.rarity = rarity;
        return this;
    }

    /**
     * @see Item#getRequirement()
     */
    public TestItemBuilder withRequirement(String requirement) {
        this.requirement = requirement;
        return this;
    }

    /**
     * @see Item#getVendorPrice()
     */
    public TestItemBuilder withVendorPrice(int vendorPrice) {
        this.vendorPrice = vendorPrice;
        return this;
    }

    /**
     * @see Item#getNote()
     */
    public TestItemBuilder withNote(String note) {
        this.note = note;
        return this;
    }

    /**
     * Sets that the resulting {@link Item} will be persisted.
     */
    public TestItemBuilder persist() {
        this.shouldGetPersisted = true;
        return this;
    }

    /**
     * Creates an item matching this builder.
     */
    public Item buildItem() {
        Item item = new Item(null, name, tags, requirement, effect, rarity, vendorPrice, tier,
                description, note, maximumStackSize, minimumStackSize);
        if (shouldGetPersisted) {
            return itemRepository.insert(universe, item);
        }
        return item;
    }

    /**
     * Creates armor matching this builder.
     */
    public Armor buildArmor() {
        Armor armorItem = new Armor(null, name, tags, requirement, effect, rarity, vendorPrice, tier,
                description, note, material, upgradeSlots, armorSlot, armor, protection, weight, 1, 1);
        if (shouldGetPersisted) {
            return (Armor) itemRepository.insert(universe, armorItem);
        }
        return armorItem;
    }

    /**
     * Creates weapon matching this builder.
     */
    public Weapon buildWeapon() {
        Weapon weapon = new Weapon(null, name, tags, requirement, effect, rarity, vendorPrice, tier,
                description, note, material, upgradeSlots, initiativeModifier, hit, damage, dice, 1, 1);
        if (shouldGetPersisted) {
            return (Weapon) itemRepository.insert(universe, weapon);
        }
        return weapon;
    }

    /**
     * Creates shield matching this builder.
     */
    public Shield buildShield() {
        Shield shield = new Shield(null, name, tags, requirement, effect, rarity, vendorPrice, tier,
                description, note, material, upgradeSlots, initiativeModifier, hit, dice, weight, armor, protection, 1, 1);
        if (shouldGetPersisted) {
            return (Shield) itemRepository.insert(universe, shield);
        }
        return shield;
    }

    /**
     * Creates jewellery matching this builder.
     */
    public Jewellery buildJewellery() {
        Jewellery jewellery = new Jewellery(null, name, tags, requirement, effect, rarity, vendorPrice, tier,
                description, note, material, upgradeSlots, 1, 1);
        if (shouldGetPersisted) {
            return (Jewellery) itemRepository.insert(universe, jewellery);
        }
        return jewellery;
    }

    private Material getMaterial(String materialName) {
        if (materialRepository == null) {
            return new Material(null, materialName, List.of());
        }
        return materialRepository.get(universe, materialName).orElseGet(() ->
                materialRepository.insert(universe, new Material(null, materialName, List.of())));
    }
}
