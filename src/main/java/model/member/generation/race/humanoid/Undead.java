package model.member.generation.race.humanoid;

import model.member.generation.Talent;
import model.member.generation.profession.Profession;
import model.member.generation.profession.undead.Lich;
import model.member.generation.profession.undead.Skeleton;
import model.member.generation.profession.undead.Zombie;
import model.member.generation.race.Race;

import java.util.Collection;

public class Undead extends Race {

    public Undead() {
        super("Untot");
    }

    @Override
    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> collection = super.getForbiddenTalents();
        collection.add(Talent.fly);
        return collection;
    }

    @Override
    public Collection<Profession> getSubTypes() {
        Collection<Profession> collection = super.getSubTypes();
        collection.add(new Zombie());
        collection.add(new Skeleton());
        collection.add(new Lich());
        return collection;
    }

    @Override
    public Collection<String> getAdvantage() {
        Collection<String> lines = super.getAdvantage();
        lines.add("Werden nicht müde oder hungrig");
        lines.add("Müssen nicht atmen");
        lines.add("Sind immun gegenüber herkömmlichen Gift");
        lines.add("Können nicht bluten");
        return lines;
    }

    @Override
    public Collection<String> getDisadvantage() {
        Collection<String> lines = super.getDisadvantage();
        lines.add("Erhalten von Heilzaubern Schaden");
        lines.add("Erhöhter Lichtschaden (150% Schaden)");
        return lines;
    }
}
