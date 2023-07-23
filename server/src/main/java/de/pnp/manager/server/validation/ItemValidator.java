package de.pnp.manager.server.validation;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.equipable.HandheldEquipableItem;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.item.interfaces.IHandheldItem;
import de.pnp.manager.component.item.interfaces.IOffensiveItem;
import de.pnp.manager.exception.ValidationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * A helper class to validate items.
 */
public class ItemValidator {

    /**
     * Validates the given items and throws a {@link ValidationException} if at least one of the items does not validate.
     */
    public static void validateItems(List<Item> items) {
        List<Set<String>> validationErrors = new ArrayList<>();

        for (Item item : items) {
            Set<String> notValidatingFields = new HashSet<>(validateItem(item));

            if (item instanceof EquipableItem equipableItem) {
                notValidatingFields.addAll(validateEquipableItem(equipableItem));
            }
            if (item instanceof Jewellery jewellery) {
                notValidatingFields.addAll(validateJewellery(jewellery));
            }
            if (item instanceof HandheldEquipableItem handheldItem) {
                notValidatingFields.addAll(validateHandheldItem(handheldItem));
            }
            if (item instanceof IOffensiveItem offensiveItem) {
                notValidatingFields.addAll(validateOffensiveItem(offensiveItem));
            }
            if (item instanceof IDefensiveItem defensiveItem) {
                notValidatingFields.addAll(validateDefensiveItem(defensiveItem));
            }

            if (!notValidatingFields.isEmpty()) {
                validationErrors.add(notValidatingFields);
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }
    }

    private static Collection<String> validateItem(Item item) {
        Set<String> notValidatingFields = new HashSet<>();

        if (StringUtils.isBlank(item.getName())) {
            notValidatingFields.add("name");
        }
        if (item.getVendorPrice() < 0) {
            notValidatingFields.add("vendorPrice");
        }
        if (item.getType() == null) {
            notValidatingFields.add("type");
        }
        if (item.getSubtype() == null) {
            notValidatingFields.add("subType");
        }
        if (item.getTier() < 0) {
            notValidatingFields.add("tier");
        }
        return notValidatingFields;
    }

    private static Collection<String> validateEquipableItem(EquipableItem item) {
        Set<String> notValidatingFields = new HashSet<>();

        if (item.getUpgradeSlots() < 0) {
            notValidatingFields.add("upgradeSlots");
        }
        if (item.getMaterial() == null) {
            notValidatingFields.add("material");
        }

        return notValidatingFields;
    }

    private static Collection<String> validateJewellery(@SuppressWarnings("unused") Jewellery jewellery) {
        // Nothing to do here at the moment
        return new HashSet<>();
    }

    private static Collection<String> validateDefensiveItem(IDefensiveItem item) {
        Set<String> notValidatingFields = new HashSet<>();

        if (item.getArmor() < 0) {
            notValidatingFields.add("armor");
        }
        if (item.getWeight() < 0) {
            notValidatingFields.add("weight");
        }

        return notValidatingFields;
    }

    private static Collection<String> validateOffensiveItem(IOffensiveItem item) {
        Set<String> notValidatingFields = new HashSet<>();

        if (StringUtils.isBlank(item.getDice())) {
            notValidatingFields.add("dice");
        }
        if (item.getDamage() < 0) {
            notValidatingFields.add("damage");
        }

        return notValidatingFields;
    }

    private static Collection<String> validateHandheldItem(@SuppressWarnings("unused") IHandheldItem item) {
        // Nothing to do here at the moment
        return new HashSet<>();
    }
}
