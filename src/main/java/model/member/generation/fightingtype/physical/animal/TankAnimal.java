package model.member.generation.fightingtype.physical.animal;

import manager.Utility;
import model.item.Weapon;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.SpecificType;

import java.util.Collection;

public class TankAnimal extends HeavyArmoredAnimal {

    protected TankAnimal(String name) {
        super(name);
    }

    public TankAnimal() {
        this("Tank");
    }

    @Override
    public Collection<SpecificType> getSubTypes() {
        Collection<SpecificType> collection = super.getSubTypes();
        collection.add(SpecificType.oneHandBlunt);
        return collection;
    }

    @Override
    public Collection<PrimaryAttribute> getPrimaryAttributes() {
        Collection<PrimaryAttribute> collection = super.getPrimaryAttributes();
        collection.add(PrimaryAttribute.strength);
        collection.add(PrimaryAttribute.endurance);
        collection.add(PrimaryAttribute.dexterity);
        collection.add(PrimaryAttribute.resilience);
        collection.add(PrimaryAttribute.maneuverability);
        return collection;
    }

    @Override
    public Collection<SecondaryAttribute> getSecondaryAttributes() {
        Collection<SecondaryAttribute> collection = super.getSecondaryAttributes();
        collection.add(SecondaryAttribute.defense);
        collection.add(SecondaryAttribute.health);
        return collection;
    }

    @Override
    public Collection<Weapon> getConcreteFirstWeapons() {
        Collection<Weapon> collection = super.getConcreteFirstWeapons();
        for (int tier = 0; tier < 5; tier++) {
            Weapon weapon = new Weapon();
            weapon.setName("Klaue");
            weapon.setDice("D6");
            weapon.setHit(0);
            weapon.setInitiative("-0.5");
            weapon.setTier(tier);
            weapon.setDamage(Utility.calculateWeaponDamage(tier, 1, 2));
            weapon.setRarity("gewöhnlich");
            collection.add(weapon);
        }
        return collection;
    }

    @Override
    public Collection<Weapon> getConcreteSecondWeapons() {
        return getConcreteFirstWeapons();
    }
}
