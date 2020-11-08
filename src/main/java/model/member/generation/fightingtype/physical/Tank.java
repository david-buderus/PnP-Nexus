package model.member.generation.fightingtype.physical;

import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.SpecificType;
import model.member.generation.fightingtype.FightingType;

import java.util.Collection;

public class Tank extends FightingType {

    protected Tank(String name) {
        super(name);
    }

    public Tank(){
        this("Tank");
    }

    @Override
    public boolean usesAlwaysShield() {
        return true;
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.oneHandBlade);
        collection.add(SpecificType.oneHandBlunt);
        collection.add(SpecificType.pole);
        return collection;
    }

    @Override
    public Collection<PrimaryAttribute> getPrimaryAttributes() {
        Collection<PrimaryAttribute> collection = super.getPrimaryAttributes();
        collection.add(PrimaryAttribute.strength);
        collection.add(PrimaryAttribute.endurance);
        collection.add(PrimaryAttribute.dexterity);
        collection.add(PrimaryAttribute.resilience);
        collection.add(PrimaryAttribute.maneuverability);
        return collection;
    }

    @Override
    public Collection<SecondaryAttribute> getSecondaryAttributes() {
        Collection<SecondaryAttribute> collection = super.getSecondaryAttributes();
        collection.add(SecondaryAttribute.defense);
        collection.add(SecondaryAttribute.health);
        return collection;
    }
}
