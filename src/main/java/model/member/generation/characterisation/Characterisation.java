package model.member.generation.characterisation;

import model.member.generation.GenerationBase;
import model.member.generation.race.Race;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public abstract class Characterisation extends GenerationBase<Race> {

    private static final Random rand = new Random();

    public static Collection<Characterisation> getCharacterisations(){
        Collection<Characterisation> collection = new HashSet<>();
        collection.add(new Humanoid());
        collection.add(new Monster());
        collection.add(new Animal());
        return collection;
    }
    public static Characterisation getCharacterisation(){
        Collection<Characterisation> characterisations = getCharacterisations();
        return characterisations.stream().skip(rand.nextInt(characterisations.size())).findFirst().orElse(null);
    }

    protected Characterisation(String name) {
        super(name);
    }
}
