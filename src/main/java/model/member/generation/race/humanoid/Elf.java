package model.member.generation.race.humanoid;

import model.loot.LootTable;
import model.member.ExtendedBattleMember;
import model.member.generation.Talent;
import model.member.generation.profession.BanditWithMagic;
import model.member.generation.profession.Profession;
import model.member.generation.race.Civilized;

import java.util.Collection;

public class Elf extends Civilized {

    public Elf() {
        super("Elfe");
    }

    @Override
    public Collection<Profession> getSubTypes() {
        Collection<Profession> collection = super.getSubTypes();
        collection.add(new BanditWithMagic());
        return collection;
    }

    @Override
    public Collection<Talent> getForbiddenTalents() {
        Collection<Talent> collection =  super.getForbiddenTalents();
        collection.add(Talent.fly);
        collection.add(Talent.gun);
        return collection;
    }

    @Override
    public LootTable getLootTable(ExtendedBattleMember member) {
        LootTable lootTable = super.getLootTable(member);
        lootTable.add("Brot", 4, 0.2);
        lootTable.add("Apfel", 2, 0.13);
        return lootTable;
    }
}
