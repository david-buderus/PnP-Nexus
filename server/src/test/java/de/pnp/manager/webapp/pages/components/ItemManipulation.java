package de.pnp.manager.webapp.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.equipable.HandheldEquipableItem;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.item.interfaces.IHandheldItem;
import de.pnp.manager.webapp.utils.WebTestUtils;

/**
 * Represents the ItemManipulation component.
 */
public abstract class ItemManipulation {

    /**
     * The locator of the component
     */
    protected final Locator locator;

    protected ItemManipulation(Locator locator) {
        this.locator = locator;
    }

    /**
     * @see Item#getName()
     */
    public void setName(String name) {
        locator.getByTestId("name").getByRole(AriaRole.TEXTBOX).fill(name);
    }

    /**
     * Returns the error text of the name input field if it exists.
     */
    public String getNameError() {
        return locator.getByTestId("name").locator("[id=name-helper-text]").textContent();
    }

    /**
     * @see Item#getType()
     */
    public void setType(ItemType type) {
        setType(type.getName());
    }

    /**
     * @see Item#getType()
     */
    public void setType(String type) {
        WebTestUtils.selectAutoComplete(locator.getByTestId("type"), type, true);
    }

    /**
     * @see Item#getSubtype()
     */
    public void setSubtype(ItemType subtype) {
        setSubtype(subtype.getName());
    }

    /**
     * @see Item#getSubtype()
     */
    public void setSubtype(String subtype) {
        WebTestUtils.selectAutoComplete(locator.getByTestId("subtype"), subtype, true);
    }

    /**
     * @see EquipableItem#getMaterial()
     */
    public void setMaterial(Material material) {
        setMaterial(material.getName());
    }

    /**
     * @see EquipableItem#getMaterial()
     */
    public void setMaterial(String material) {
        WebTestUtils.selectAutoComplete(locator.getByTestId("material"), material, true);
    }

    /**
     * @see IDefensiveItem#getWeight()
     */
    public void setWeight(float weight) {
        locator.getByTestId("weight").getByRole(AriaRole.TEXTBOX).fill(String.valueOf(weight));
    }

    /**
     * @see IDefensiveItem#getArmor()
     */
    public void setArmor(int armor) {
        locator.getByTestId("armor").getByRole(AriaRole.TEXTBOX).fill(String.valueOf(armor));
    }

    /**
     * @see Weapon#getDamage()
     */
    public void setDamage(int damage) {
        locator.getByTestId("damage").getByRole(AriaRole.TEXTBOX).fill(String.valueOf(damage));
    }

    /**
     * @see Weapon#getDice()
     */
    public void setDice(String dice) {
        locator.getByTestId("dice").getByRole(AriaRole.TEXTBOX).fill(dice);
    }

    /**
     * @see HandheldEquipableItem#getInitiative()
     */
    public void setInitiative(float initiative) {
        locator.getByTestId("initiative").getByRole(AriaRole.TEXTBOX).fill(String.valueOf(initiative));
    }

    /**
     * @see HandheldEquipableItem#getHit()
     */
    public void setHit(int hit) {
        locator.getByTestId("hit").getByRole(AriaRole.TEXTBOX).fill(String.valueOf(hit));
    }

    /**
     * @see Item#getEffect()
     */
    public void setEffect(String effect) {
        locator.getByTestId("effect").getByRole(AriaRole.TEXTBOX).fill(effect);
    }

    /**
     * @see Item#getDescription()
     */
    public void setDescription(String description) {
        locator.getByTestId("description").getByRole(AriaRole.TEXTBOX).fill(description);
    }

    /**
     * @see EquipableItem#getUpgradeSlots()
     */
    public void setUpgradeSlots(int upgradeSlots) {
        locator.getByTestId("upgradeSlots").getByRole(AriaRole.TEXTBOX).fill(String.valueOf(upgradeSlots));
    }

    /**
     * @see Item#getRarity()
     */
    public void setRarity(ERarity rarity) {
        WebTestUtils.select(locator.getByTestId("rarity"), rarity.name());
    }

    /**
     * @see Item#getTier()
     */
    public void setTier(int tier) {
        locator.getByTestId("tier").getByRole(AriaRole.TEXTBOX).fill(String.valueOf(tier));
    }

    /**
     * Returns the error text of the tier input field if it exists.
     */
    public String getTierError() {
        return locator.getByTestId("tier").locator("[id=tier-helper-text]").textContent();
    }

    /**
     * @see Item#getRequirement()
     */
    public void setRequirement(String requirement) {
        locator.getByTestId("requirement").getByRole(AriaRole.TEXTBOX).fill(requirement);
    }

    /**
     * @see Item#getVendorPrice()
     */
    public void setVendorPrice(int vendorPrice) {
        locator.getByTestId("vendorPrice").getByRole(AriaRole.TEXTBOX).fill(String.valueOf(vendorPrice));
    }

    /**
     * Returns the human-readable string representing the vendor price
     */
    public String getResultingPrice() {
        return locator.getByTestId("resultingPrice").getByRole(AriaRole.TEXTBOX).inputValue();
    }

    /**
     * @see Item#getMinimumStackSize()
     */
    public void setMinimumStackSize(int minimumStackSize) {
        Locator textbox = locator.getByTestId("minimumStackSize").getByRole(AriaRole.TEXTBOX);
        if (textbox.isVisible()) {
            textbox.fill(String.valueOf(minimumStackSize));
        }
    }

    /**
     * @see Item#getMaximumStackSize()
     */
    public void setMaximumStackSize(int maximumStackSize) {
        Locator textbox = locator.getByTestId("maximumStackSize").getByRole(AriaRole.TEXTBOX);
        if (textbox.isVisible()) {
            textbox.fill(String.valueOf(maximumStackSize));
        }
    }

    /**
     * @see Item#getNote()
     */
    public void setNote(String note) {
        locator.getByTestId("note").getByRole(AriaRole.TEXTBOX).fill(note);
    }

    /**
     * Sets all input field corresponding to the attributes of the given {@link Item}.
     */
    public void setItem(Item item) {
        setName(item.getName());
        setType(item.getType());
        setSubtype(item.getSubtype());
        setRequirement(item.getRequirement());
        setEffect(item.getEffect());
        setRarity(item.getRarity());
        setVendorPrice(item.getVendorPrice());
        setTier(item.getTier());
        setDescription(item.getDescription());
        setNote(item.getNote());
        setMinimumStackSize(item.getMinimumStackSize());
        setMaximumStackSize(item.getMaximumStackSize());
        if (item instanceof EquipableItem equipableItem) {
            setMaterial(equipableItem.getMaterial());
            setUpgradeSlots(equipableItem.getUpgradeSlots());
        }
        if (item instanceof IDefensiveItem defensiveItem) {
            setArmor(defensiveItem.getArmor());
            setWeight(defensiveItem.getWeight());
        }
        if (item instanceof IHandheldItem handheldItem) {
            setInitiative(handheldItem.getInitiative());
            setHit(handheldItem.getHit());
        }
        if (item instanceof Weapon weapon) {
            setDamage(weapon.getDamage());
            setDice(weapon.getDice());
        }
    }
}
