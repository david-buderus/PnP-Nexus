package de.pnp.manager.model.character.data;

import de.pnp.manager.model.interfaces.WithUnlocalizedName;

import java.util.NoSuchElementException;

public enum ArmorPosition implements IArmorPosition, WithUnlocalizedName {

    HEAD("armorPosition.head"), UPPER_BODY("armorPosition.upperBody"),
    ARM("armorPosition.arm"),
    LEGS("armorPosition.legs");

    private final String unlocalizedName;

    ArmorPosition(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public static ArmorPosition getArmorPosition(String name) {
        for (ArmorPosition pos : values()) {
            if (pos.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return pos;
            }
        }
        throw new NoSuchElementException("The ArmorPosition with the name " + name + " does not exists.");
    }
}
