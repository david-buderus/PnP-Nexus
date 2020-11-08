package model.member.generation.fightingtype.physical.animal;

import manager.Utility;
import model.item.Armor;
import model.member.generation.fightingtype.FightingType;

import java.util.Collection;

public abstract class LightArmoredAnimal extends FightingType {

    protected LightArmoredAnimal(String name) {
        super(name);
    }

    @Override
    public Collection<Armor> getConcreteArmor() {
        Collection<Armor> collection = super.getConcreteArmor();
        for (int tier = 0; tier < 5; tier++) {
            collection.add(Utility.generateCommonLightArmor(tier, "Fell", "Kopf"));
            collection.add(Utility.generateCommonLightArmor(tier, "Fell", "Arme"));
            collection.add(Utility.generateCommonLightArmor(tier, "Fell", "Oberkörper"));
            collection.add(Utility.generateCommonLightArmor(tier, "Fell", "Beine"));
        }
        return collection;
    }
}
