package model.member.generation.race.animal;

import model.member.generation.Talent;
import model.member.generation.profession.Profession;
import model.member.generation.profession.animal.CasualAnimal;
import model.member.generation.race.Race;

import java.util.Collection;

public class Scorpion extends Race {

    protected Scorpion(String name) {
        super(name);
    }

    public Scorpion() {
        this("Skorpion");
    }

    @Override
    public Collection<Profession> getSubTypes() {
        Collection<Profession> collection = super.getSubTypes();
        collection.add(new CasualAnimal());
        return collection;
    }

    @Override
    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> collection = super.getForbiddenTalents();
        collection.add(Talent.fly);
        return collection;
    }
}
