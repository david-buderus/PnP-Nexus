package model.member.generation.fightingtype;

import model.member.generation.GenerationBase;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.SpecificType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public abstract class FightingType extends GenerationBase<SpecificType> {

    private static final Random rand = new Random();

    protected FightingType(String name) {
        super(name);
    }

    public boolean usesAlwaysShield(){
        return false;
    }

    public Collection<PrimaryAttribute> getPrimaryAttributes(){
        return new HashSet<>();
    }

    public Collection<SecondaryAttribute> getSecondaryAttributes(){
        return new HashSet<>();
    }

    public boolean isMage(){
        return false;
    }
}
