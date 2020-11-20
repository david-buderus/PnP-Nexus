package model.member.generation.profession;

import model.member.generation.fightingtype.FightingType;
import model.member.generation.fightingtype.magic.FullMage;

import java.util.Collection;

public class BanditWithMagic extends BanditWithoutMagic {

    public BanditWithMagic() {
        super("Bandit mit Magie");
    }

    @Override
    public Collection<FightingType> getSubTypes() {
        Collection<FightingType> collection = super.getSubTypes();
        collection.add(new FullMage());
        return collection;
    }
}
