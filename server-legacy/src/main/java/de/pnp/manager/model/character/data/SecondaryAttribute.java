package de.pnp.manager.model.character.data;

import de.pnp.manager.model.interfaces.WithUnlocalizedName;

import java.util.NoSuchElementException;

public enum SecondaryAttribute implements ISecondaryAttribute, WithUnlocalizedName {
    MELEE_DAMAGE("secondaryAttribute.meleeDamage"), RANGE_DAMAGE("secondaryAttribute.rangeDamage"),
    MAGIC_POWER("secondaryAttribute.magicPower"), DEFENSE("secondaryAttribute.defense"),
    INITIATIVE("secondaryAttribute.initiative"), HEALTH("secondaryAttribute.health"),
    MENTAL_HEALTH("secondaryAttribute.mentalHealth"), MANA("secondaryAttribute.mana");

    private final String unlocalizedName;

    SecondaryAttribute(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public static SecondaryAttribute getSecondaryAttribute(String name) {
        for (SecondaryAttribute attribute : values()) {
            if (attribute.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return attribute;
            }
        }
        throw new NoSuchElementException("The SecondaryAttribute with the name " + name + " does not exists.");
    }
}
