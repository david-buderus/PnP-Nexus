package de.pnp.manager.model.character.data;

import de.pnp.manager.model.interfaces.WithUnlocalizedName;

public enum AttackTypes implements IAttackTypes, WithUnlocalizedName {
    HEAD("attackTypes.head"), UPPER_BODY("attackTypes.upperBody"),
    LEGS("attackTypes.legs"), ARM("attackTypes.arm"),
    IGNORE_ARMOR("attackTypes.ignoreArmor"), DIRECT("attackTypes.direct");

    private final String unlocalizedName;

    AttackTypes(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }
}
