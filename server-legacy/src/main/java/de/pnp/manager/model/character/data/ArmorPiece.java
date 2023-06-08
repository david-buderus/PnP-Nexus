package de.pnp.manager.model.character.data;

import de.pnp.manager.model.interfaces.WithUnlocalizedName;

import java.util.NoSuchElementException;

public enum ArmorPiece implements IArmorPiece, WithUnlocalizedName {

    HEAD("armorPiece.head"), UPPER_BODY("armorPiece.upperBody"),
    LEGS("armorPiece.legs"), ARM("armorPiece.arm"),
    SHIELD("armorPiece.shield");

    private final String unlocalizedName;

    ArmorPiece(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public static ArmorPiece getArmorPiece(String name) {
        for (ArmorPiece piece : values()) {
            if (piece.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return piece;
            }
        }
        throw new NoSuchElementException("The ArmorPiece with the name " + name + " does not exists.");
    }
}