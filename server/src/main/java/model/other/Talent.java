package model.other;

import model.attribute.IPrimaryAttribute;
import model.member.generation.PrimaryAttribute;

public class Talent implements ITalent {

    protected String name;
    protected IPrimaryAttribute[] attributes;
    protected boolean magicTalent;
    protected boolean weaponTalent;

    public Talent() {
        this.name = "";
        this.attributes = new PrimaryAttribute[0];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IPrimaryAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(IPrimaryAttribute[] attributes) {
        this.attributes = attributes;
    }

    public boolean isMagicTalent() {
        return magicTalent;
    }

    public void setMagicTalent(boolean magicTalent) {
        this.magicTalent = magicTalent;
    }

    public boolean isWeaponTalent() {
        return weaponTalent;
    }

    public void setWeaponTalent(boolean weaponTalent) {
        this.weaponTalent = weaponTalent;
    }

    @Override
    public String toString() {
        return name;
    }
}