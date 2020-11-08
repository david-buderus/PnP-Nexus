package model.member.generation.profession.animal;

import model.member.generation.fightingtype.FightingType;
import model.member.generation.fightingtype.physical.animal.MeleeAnimal;
import model.member.generation.fightingtype.physical.animal.FastMeleeAnimal;
import model.member.generation.profession.Profession;

import java.util.Collection;

public class CasualAnimal extends Profession {

    protected CasualAnimal(String name) {
        super(name);
    }

    public CasualAnimal(){
        this("Normales Wildtier");
    }

    @Override
    public Collection<FightingType> getSubTypes() {
        Collection<FightingType> collection = super.getSubTypes();
        collection.add(new FastMeleeAnimal());
        collection.add(new MeleeAnimal());
        return collection;
    }
}
