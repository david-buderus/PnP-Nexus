package model.member.generation.profession.animal;

import model.member.generation.fightingtype.FightingType;
import model.member.generation.fightingtype.physical.animal.TankAnimal;
import model.member.generation.profession.Profession;

import java.util.Collection;

public class CasualTankAnimal extends Profession {

    protected CasualTankAnimal(String name) {
        super(name);
    }

    public CasualTankAnimal() {
        this("Tank Wildtier");
    }

    @Override
    public Collection<FightingType> getSubTypes() {
        Collection<FightingType> collection = super.getSubTypes();
        collection.add(new TankAnimal());
        return collection;
    }
}
