package model.member.generation.race;

import model.member.generation.profession.BanditWithoutMagic;
import model.member.generation.profession.Profession;

import java.util.Collection;

public abstract class Civilized extends Race {

    protected Civilized(String name) {
        super(name);
    }

    @Override
    public Collection<Profession> getSubTypes() {
        Collection<Profession> collection = super.getSubTypes();
        collection.add(new BanditWithoutMagic());
        return collection;
    }
}
