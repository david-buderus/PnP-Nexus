package model.member.generation.fightingtype.physical.range;

import model.member.generation.SpecificType;

import java.util.Collection;

public class FullRange extends Range {

    public FullRange() {
        super("Fernkämpfer");
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.bow);
        collection.add(SpecificType.crossbow);
        collection.add(SpecificType.gun);
        return collection;
    }
}
