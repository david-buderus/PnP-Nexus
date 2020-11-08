package model.member.generation.fightingtype.magic;

import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.fightingtype.FightingType;

import java.util.Collection;

public abstract class Mage extends FightingType {

    protected Mage(String name) {
        super(name);
    }

    @Override
    public Collection<PrimaryAttribute> getPrimaryAttributes() {
        Collection<PrimaryAttribute> collection = super.getPrimaryAttributes();
        collection.add(PrimaryAttribute.intelligence);
        collection.add(PrimaryAttribute.charisma);
        collection.add(PrimaryAttribute.precision);
        return collection;
    }

    @Override
    public Collection<SecondaryAttribute> getSecondaryAttributes() {
        Collection<SecondaryAttribute> collection = super.getSecondaryAttributes();
        collection.add(SecondaryAttribute.magicPower);
        collection.add(SecondaryAttribute.mana);
        return collection;
    }

    @Override
    public boolean isMage() {
        return true;
    }
}
