package model.member.generation.fightingtype;

import model.loot.LootTable;
import model.member.ExtendedBattleMember;

public class PhysicalFighter extends FightingType {
    protected PhysicalFighter(String name) {
        super(name);
    }

    @Override
    public LootTable getLootTable(ExtendedBattleMember member) {
        LootTable lootTable = super.getLootTable(member);
        switch (member.getTier()){
            case 4:
                lootTable.add("Starker Heiltrank", 1, 0.05);
            case 3:
                lootTable.add("Heiltrank", 1, 0.05);
            case 2:
                lootTable.add("Schwacher Heiltrank", 1, 0.05);
                break;
        }
        return lootTable;
    }
}
