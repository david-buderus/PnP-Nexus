package model.member.generation.characterisation;

import model.member.generation.Talent;
import model.member.generation.race.Race;

import java.util.Collection;

public class Monster extends Characterisation {

    public Monster() {
        super("Monster");
    }

    @Override
    public Collection<Race> getSubTypes() {
        return super.getSubTypes();
    }

    @Override
    public Collection<Talent> getForbiddenTalents(){
        Collection<Talent> collection = super.getForbiddenTalents();

        collection.add(Talent.oneHandBlunt);
        collection.add(Talent.oneHandBlunt);
        collection.add(Talent.twoHandBlade);
        collection.add(Talent.twoHandBlunt);
        collection.add(Talent.bow_crossbow);
        collection.add(Talent.gun);
        collection.add(Talent.big_throwing);
        collection.add(Talent.small_throwing);
        collection.add(Talent.pole);

        return collection;
    }
}
