package model.member.generation.fightingtype.physical.range;

import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.fightingtype.FightingType;

import java.util.Collection;

public abstract class Range extends FightingType {

    protected Range(String name) {
        super(name);
    }

    @Override
    public Collection<PrimaryAttribute> getPrimaryAttributes() {
        Collection<PrimaryAttribute> collection = super.getPrimaryAttributes();
        collection.add(PrimaryAttribute.endurance);
        collection.add(PrimaryAttribute.dexterity);
        collection.add(PrimaryAttribute.maneuverability);
        collection.add(PrimaryAttribute.precision);
        return collection;
    }

    @Override
    public Collection<SecondaryAttribute> getSecondaryAttributes() {
        Collection<SecondaryAttribute> collection = super.getSecondaryAttributes();
        collection.add(SecondaryAttribute.rangeDamage);
        collection.add(SecondaryAttribute.health);
        return collection;
    }
}
