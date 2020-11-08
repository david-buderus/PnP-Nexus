package model.member.generation.fightingtype.physical.melee;

import model.member.generation.SpecificType;

import java.util.Collection;

public class MartialArtsMelee extends Melee {

    public MartialArtsMelee() {
        super("Waffenloser Nahkämpfer");
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.fist);
        return collection;
    }
}
