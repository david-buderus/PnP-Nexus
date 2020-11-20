package model.member.generation.race.animal;

import model.member.generation.Talent;
import model.member.generation.profession.Profession;
import model.member.generation.profession.animal.CasualTankAnimal;
import model.member.generation.race.Race;

import java.util.Collection;

public class Bear extends Race {

    protected Bear(String name) {
        super(name);
    }

    public Bear() {
        this("Bär");
    }

    @Override
    public Collection<Profession> getSubTypes() {
        Collection<Profession> collection = super.getSubTypes();
        collection.add(new CasualTankAnimal());
        return collection;
    }

    @Override
    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> collection = super.getForbiddenTalents();
        collection.add(Talent.fly);
        return collection;
    }
}
