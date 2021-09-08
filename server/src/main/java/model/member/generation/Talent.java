package model.member.generation;

public class Talent {

    protected String name;
    protected PrimaryAttribute[] attributes;
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

    public PrimaryAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(PrimaryAttribute[] attributes) {
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
