package model.other;

import model.attribute.IPrimaryAttribute;

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
