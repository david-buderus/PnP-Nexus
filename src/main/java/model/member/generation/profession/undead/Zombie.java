package model.member.generation.profession.undead;

import model.member.generation.Talent;
import model.member.generation.fightingtype.FightingType;
import model.member.generation.fightingtype.physical.Tank;
import model.member.generation.fightingtype.physical.melee.WeaponMelee;
import model.member.generation.profession.Profession;

import java.util.Collection;

public class Zombie extends Profession {

    public Zombie() {
        super("Zombie");
    }

    @Override
    public Collection<FightingType> getSubTypes() {
        Collection<FightingType> collection = super.getSubTypes();
        collection.add(new WeaponMelee());
        collection.add(new Tank());
        return collection;
    }

    @Override
    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> collection = super.getForbiddenTalents();
        collection.addAll(Talent.getMagicTalents());
        return collection;
    }

    @Override
    public Collection<String> getAdvantage() {
        Collection<String> lines = super.getAdvantage();
        lines.add("Beim ersten Tod pro Tag fällt das Leben nur auf eins");
        return lines;
    }

    @Override
    public Collection<String> getDisadvantage() {
        Collection<String> lines = super.getDisadvantage();
        lines.add("Sind relativ lahm");
        return lines;
    }
}
