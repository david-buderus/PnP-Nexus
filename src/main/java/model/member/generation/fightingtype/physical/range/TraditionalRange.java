package model.member.generation.fightingtype.physical.range;

import model.member.generation.SpecificType;

import java.util.Collection;

public class TraditionalRange extends Range {

    public TraditionalRange() {
        super("Traditioneller Fernkämpfer");
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.bow);
        collection.add(SpecificType.crossbow);
        return collection;
    }
}
