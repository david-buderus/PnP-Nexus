package model.member.generation.fightingtype.physical.range;

import model.member.generation.SpecificType;

import java.util.Collection;

public class GunRange extends Range {

    public GunRange() {
        super("Gewehrträger");
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.gun);
        return collection;
    }
}
