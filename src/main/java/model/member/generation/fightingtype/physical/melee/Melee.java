package model.member.generation.fightingtype.physical.melee;

import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.fightingtype.PhysicalFighter;

import java.util.Collection;

public abstract class Melee extends PhysicalFighter {

    protected Melee(String name) {
        super(name);
    }

    @Override
    public Collection<PrimaryAttribute> getPrimaryAttributes() {
        Collection<PrimaryAttribute> collection = super.getPrimaryAttributes();
        collection.add(PrimaryAttribute.strength);
        collection.add(PrimaryAttribute.endurance);
        collection.add(PrimaryAttribute.dexterity);
        collection.add(PrimaryAttribute.maneuverability);
        return collection;
    }

    @Override
    public Collection<SecondaryAttribute> getSecondaryAttributes() {
        Collection<SecondaryAttribute> collection = super.getSecondaryAttributes();
        collection.add(SecondaryAttribute.meleeDamage);
        collection.add(SecondaryAttribute.defense);
        collection.add(SecondaryAttribute.health);
        return collection;
    }
}
