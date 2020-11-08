package model.member.generation.fightingtype.magic;

import model.member.generation.PrimaryAttribute;
import model.member.generation.SpecificType;

import java.util.Collection;

public class DarkMage extends Mage {

    public DarkMage() {
        super("Dunkler Magier");
    }

    @Override
    public Collection<PrimaryAttribute> getPrimaryAttributes() {
        Collection<PrimaryAttribute> collection = super.getPrimaryAttributes();
        collection.add(PrimaryAttribute.resilience);
        return collection;
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.darkness);
        collection.add(SpecificType.ice);
        collection.add(SpecificType.death);
        return collection;
    }
}
