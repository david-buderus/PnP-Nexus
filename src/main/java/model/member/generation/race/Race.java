package model.member.generation.race;

import model.member.generation.GenerationBase;
import model.member.generation.profession.Profession;

import java.util.Random;

public abstract class Race extends GenerationBase<Profession> {

    private static final Random rand = new Random();

    protected Race(String name) {
        super(name);
    }
}
