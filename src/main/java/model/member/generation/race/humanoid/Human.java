package model.member.generation.race.humanoid;

import model.loot.LootTable;
import model.member.ExtendedBattleMember;
import model.member.generation.Talent;
import model.member.generation.profession.Profession;
import model.member.generation.race.Civilized;

import java.util.Collection;

public class Human extends Civilized {

    public Human() {
        super("Mensch");
    }

    @Override
    public Collection<Profession> getSubTypes() {
        return super.getSubTypes();
    }

    @Override
    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> collection = super.getForbiddenTalents();
        collection.add(Talent.fly);
        return collection;
    }

    @Override
    public LootTable getLootTable(ExtendedBattleMember member) {
        LootTable lootTable = super.getLootTable(member);
        lootTable.add("Fleisch", 1, 0.05);
        lootTable.add("Dörrfleisch", 3, 0.1);
        return lootTable;
    }
}
