package model.member.generation.characterisation;

import model.loot.LootTable;
import model.member.ExtendedBattleMember;
import model.member.generation.race.Race;
import model.member.generation.race.humanoid.Elf;
import model.member.generation.race.humanoid.Human;
import model.member.generation.race.humanoid.Undead;

import java.util.Collection;

public class Humanoid extends Characterisation {

    public Humanoid() {
        super("Humanoid");
    }

    @Override
    public Collection<Race> getSubTypes() {
        Collection<Race> collection = super.getSubTypes();
        collection.add(new Human());
        collection.add(new Elf());
        collection.add(new Undead());
        return collection;
    }

    @Override
    public LootTable getLootTable(ExtendedBattleMember member) {
        LootTable lootTable = super.getLootTable(member);

        lootTable.add("Kupfer", member.getTier(), 0.8);
        lootTable.add("Silber", Math.round(0.5f * member.getLevel()), 0.7);
        lootTable.add("Gold", Math.max(0, Math.round(0.25f * (member.getTier() - 3))), 0.6);

        return lootTable;
    }
}
