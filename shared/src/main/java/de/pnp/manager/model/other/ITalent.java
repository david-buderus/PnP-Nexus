package de.pnp.manager.model.other;

import de.pnp.manager.model.character.data.IPrimaryAttribute;

public interface ITalent {

    String getName();

    void setName(String name);

    IPrimaryAttribute[] getAttributes();

    void setAttributes(IPrimaryAttribute[] attributes);

    boolean isMagicTalent();

    void setMagicTalent(boolean magicTalent);

    boolean isWeaponTalent();

    void setWeaponTalent(boolean weaponTalent);
}
