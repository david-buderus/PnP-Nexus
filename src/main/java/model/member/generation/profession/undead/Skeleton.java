package model.member.generation.profession.undead;

import model.member.generation.Talent;
import model.member.generation.fightingtype.FightingType;
import model.member.generation.fightingtype.physical.Tank;
import model.member.generation.fightingtype.physical.melee.FullMelee;
import model.member.generation.fightingtype.physical.range.FullRange;
import model.member.generation.profession.Profession;

import java.util.Collection;

public class Skeleton extends Profession {

    public Skeleton() {
        super("Skeleton");
    }

    @Override
    public Collection<FightingType> getSubTypes() {
        Collection<FightingType> collection = super.getSubTypes();
        collection.add(new FullMelee());
        collection.add(new Tank());
        collection.add(new FullRange());
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
        lines.add("Können abgetrennte Körperteile weiterhin kontrollieren und wieder ansetzen");
        return lines;
    }

    @Override
    public Collection<String> getDisadvantage() {
        Collection<String> lines = super.getDisadvantage();
        lines.add("Direkt getroffene Knochen können zerbrechen (Wahrscheinlichkeit: Schaden in Prozent)");
        return lines;
    }
}
