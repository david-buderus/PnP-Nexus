package de.pnp.manager.component.upgrade;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.equipable.HandheldEquipableItem;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.item.interfaces.IItem;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A restriction of an {@link Upgrade}.
 */
@Schema(enumAsRef = true)
public enum EUpgradeRestriction {
    ITEM(Item.class, "item"), EQUIPMENT(EquipableItem.class, "equipment"),
    JEWELLERY(Jewellery.class, "jewellery"), WEAPON(Weapon.class, "weapon"),
    DEFENSIVE_ITEM(IDefensiveItem.class, "defensive_item"), SHIELD(Shield.class, "shield"),
    HANDHELD(HandheldEquipableItem.class, "handheld_item"), ARMOR(Armor.class, "armor");

    private final Class<? extends IItem> correlatingClass;

    private final String messageTemplate;

    EUpgradeRestriction(Class<? extends IItem> correlatingClass, String messageTemplate) {
        this.correlatingClass = correlatingClass;
        this.messageTemplate = messageTemplate;
    }

    /**
     * Returns whether the restriction does not restrict on the given {@link Item}.
     */
    public boolean applicableOn(Item item) {
        return correlatingClass.isInstance(item);
    }

    /**
     * The message template to localize this enum for users.
     */
    public String getMessageTemplate() {
        return messageTemplate;
    }
}
