package model.member.generation.profession;

import model.member.generation.GenerationBase;
import model.member.generation.fightingtype.FightingType;

public abstract class Profession extends GenerationBase<FightingType> {

    protected Profession(String name) {
        super(name);
    }
}
