package model.member.generation.fightingtype;

import model.loot.LootTable;
import model.member.ExtendedBattleMember;

public class MagicFighter extends FightingType {
    protected MagicFighter(String name) {
        super(name);
    }

    @Override
    public LootTable getLootTable(ExtendedBattleMember member) {
        LootTable lootTable = super.getLootTable(member);
        switch (member.getTier()){
            case 4:
                lootTable.add("Starker Manatrank", 1, 0.05);
            case 3:
                lootTable.add("Manatrank", 1, 0.05);
            case 2:
                lootTable.add("Schwacher Manatrank", 1, 0.05);
                break;
        }
        return lootTable;
    }
}
