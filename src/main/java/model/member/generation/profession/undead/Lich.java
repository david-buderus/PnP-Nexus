package model.member.generation.profession.undead;

import model.member.generation.Talent;
import model.member.generation.fightingtype.magic.DarkMage;
import model.member.generation.fightingtype.FightingType;
import model.member.generation.profession.Profession;

import java.util.Collection;

public class Lich extends Profession {

    public Lich() {
        super("Lich");
    }

    @Override
    public Collection<FightingType> getSubTypes() {
        Collection<FightingType> collection = super.getSubTypes();
        collection.add(new DarkMage());
        return collection;
    }

    @Override
    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> collection = super.getForbiddenTalents();
        collection.add(Talent.arkan);
        collection.add(Talent.illusion);
        collection.add(Talent.light);
        collection.add(Talent.fire);
        collection.add(Talent.water);
        collection.add(Talent.earth);
        collection.add(Talent.air);
        collection.add(Talent.storm);
        collection.add(Talent.nature);
        return collection;
    }

    @Override
    public Collection<String> getAdvantage() {
        Collection<String> lines = super.getAdvantage();
        lines.add("Erleichterung bei Frost-/Finster-/Totenmagie");
        return lines;
    }

    @Override
    public Collection<String> getDisadvantage() {
        Collection<String> lines = super.getDisadvantage();
        lines.add("Kann keine andere Magie benutzen");
        return lines;
    }
}
