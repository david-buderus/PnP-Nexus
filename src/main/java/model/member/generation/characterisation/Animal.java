package model.member.generation.characterisation;

import model.member.generation.Talent;
import model.member.generation.race.Race;
import model.member.generation.race.animal.Bear;
import model.member.generation.race.animal.Wolf;

import java.util.Collection;

public class Animal extends Characterisation {

    public Animal() {
        super("Wildtier");
    }

    @Override
    public Collection<Race> getSubTypes() {
        Collection<Race> collection = super.getSubTypes();
        collection.add(new Wolf());
        collection.add(new Bear());
        return collection;
    }

    @Override
    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> collection = super.getForbiddenTalents();
        collection.addAll(Talent.getMagicTalents());
        collection.addAll(Talent.getPureWeaponTalents());
        collection.add(Talent.ride);
        return collection;
    }

    @Override
    public boolean dropsWeapons() {
        return false;
    }

    @Override
    public boolean dropsArmor() {
        return false;
    }

    @Override
    public boolean usesJewellery(String position) {
        return false;
    }
}
