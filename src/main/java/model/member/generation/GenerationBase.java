package model.member.generation;

import model.item.Armor;
import model.item.Jewellery;
import model.item.Weapon;
import model.loot.LootTable;
import model.member.ExtendedBattleMember;

import java.util.*;

public abstract class GenerationBase<SubType> {

    private static final Random rand = new Random();

    protected final String name;

    protected GenerationBase(String name) {
        this.name = name;
    }

    public Collection<SubType> getSubTypes(){
        return new HashSet<>();
    }

    public SubType getSubType(){
        Collection<SubType> subTypes = getSubTypes();
        return subTypes.stream().skip(rand.nextInt(subTypes.size())).findFirst().orElse(null);
    }

    public Collection<Talent> getForbiddenTalents(){
        return new HashSet<>();
    }

    public LootTable getLootTable(ExtendedBattleMember member) {
        return new LootTable();
    }

    public boolean dropsWeapons(){
        return true;
    }

    public boolean dropsArmor(){
        return true;
    }

    public boolean dropsJewellery(){
        return true;
    }

    public boolean usesFirstWeapon(){
        return true;
    }

    public boolean usesSecondWeapon(){
        return true;
    }

    public boolean usesArmor(String position){
        return true;
    }

    public boolean usesJewellery(String position){
        return true;
    }

    public Collection<Weapon> getConcreteFirstWeapons(){
        return new ArrayList<>();
    }

    public Collection<Weapon> getConcreteSecondWeapons(){
        return new ArrayList<>();
    }

    public Collection<Armor> getConcreteArmor(){
        return new ArrayList<>();
    }

    public Collection<Jewellery> getConcreteJewellery(){
        return new ArrayList<>();
    }

    public Collection<String> getAdvantage(){
        return new ArrayList<>();
    }

    public Collection<String> getDisadvantage(){
        return new ArrayList<>();
    }

    @Override
    public String toString(){
        return name;
    }
}
