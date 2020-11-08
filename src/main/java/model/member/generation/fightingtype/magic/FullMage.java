package model.member.generation.fightingtype.magic;

import model.member.generation.SpecificType;

import java.util.Collection;

public class FullMage extends Mage {

    public FullMage() {
        super("Magier");
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.arkan);
        collection.add(SpecificType.illusion);
        collection.add(SpecificType.light);
        collection.add(SpecificType.darkness);
        collection.add(SpecificType.fire);
        collection.add(SpecificType.water);
        collection.add(SpecificType.earth);
        collection.add(SpecificType.air);
        collection.add(SpecificType.storm);
        collection.add(SpecificType.ice);
        collection.add(SpecificType.nature);
        collection.add(SpecificType.death);
        return collection;
    }
}
