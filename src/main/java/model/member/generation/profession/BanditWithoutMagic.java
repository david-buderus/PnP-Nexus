package model.member.generation.profession;

import model.loot.LootTable;
import model.member.ExtendedBattleMember;
import model.member.generation.fightingtype.FightingType;
import model.member.generation.fightingtype.physical.Tank;
import model.member.generation.fightingtype.physical.melee.FullMelee;
import model.member.generation.fightingtype.physical.range.FullRange;

import java.util.Collection;

public class BanditWithoutMagic extends Profession {

    public BanditWithoutMagic() {
        super("Bandit ohne Magie");
    }

    protected BanditWithoutMagic(String name){
        super(name);
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
    public LootTable getLootTable(ExtendedBattleMember member) {
        LootTable lootTable = super.getLootTable(member);
        lootTable.add("Seil", 1, 0.2);
        lootTable.add("Messer", 1, 0.1);
        return lootTable;
    }
}
