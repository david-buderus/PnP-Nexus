package model.member.generation.fightingtype.physical.melee;

import model.member.generation.SpecificType;

import java.util.Collection;

public class FullMelee extends Melee {

    public FullMelee() {
        super("Nahkämpfer");
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.fist);
        collection.add(SpecificType.oneHandBlade);
        collection.add(SpecificType.oneHandBlunt);
        collection.add(SpecificType.pole);
        collection.add(SpecificType.twoHandBlade);
        collection.add(SpecificType.twoHandBlunt);
        return collection;
    }
}
