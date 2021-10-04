package de.pnp.manager.impl;

import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.data.IArmorPosition;
import de.pnp.manager.model.character.data.IPrimaryAttribute;
import de.pnp.manager.model.character.data.ISecondaryAttribute;
import de.pnp.manager.model.character.state.IMemberState;
import de.pnp.manager.model.item.IArmor;
import de.pnp.manager.model.item.IJewellery;
import de.pnp.manager.model.item.IWeapon;
import de.pnp.manager.model.loot.ILootTable;
import de.pnp.manager.model.other.ISpell;
import de.pnp.manager.model.other.ITalent;

import java.util.Collection;
import java.util.Map;

public class PnPCharacterImpl implements IPnPCharacter {
    
    protected String name;
    protected String characterID;
    protected int level;
    protected Collection<String> advantages;
    protected Collection<String> disadvantages;
    protected int health;
    protected int maxHealth;
    protected int mana;
    protected int maxMana;
    protected int mentalHealth;
    protected int maxMentalHealth;
    protected int initiative;
    protected int staticInitiative;
    protected int baseDefense;
    protected int staticBaseDefense;
    protected Map<IPrimaryAttribute, Integer> primaryAttributes;
    protected Map<ISecondaryAttribute, Integer> secondaryAttributes;
    protected Collection<IMemberState> memberStates;
    protected Collection<IWeapon> weapons;
    protected Collection<IWeapon> equippedWeapons;
    protected Collection<IJewellery> jewellery;
    protected Collection<IJewellery> equippedJewellery;
    protected Collection<IArmor> armor;
    protected Map<IArmorPosition, IArmor> equippedArmor;
    protected Collection<ISpell> spells;
    protected Map<ITalent, Integer> talents;
    protected IInventory inventory;
    protected ILootTable lootTable;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCharacterID() {
        return characterID;
    }

    public void setCharacterID(String characterID) {
        this.characterID = characterID;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public Collection<String> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(Collection<String> advantages) {
        this.advantages = advantages;
    }

    @Override
    public Collection<String> getDisadvantages() {
        return disadvantages;
    }

    public void setDisadvantages(Collection<String> disadvantages) {
        this.disadvantages = disadvantages;
    }

    @Override
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    @Override
    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    @Override
    public int getMentalHealth() {
        return mentalHealth;
    }

    public void setMentalHealth(int mentalHealth) {
        this.mentalHealth = mentalHealth;
    }

    @Override
    public int getMaxMentalHealth() {
        return maxMentalHealth;
    }

    public void setMaxMentalHealth(int maxMentalHealth) {
        this.maxMentalHealth = maxMentalHealth;
    }

    @Override
    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    @Override
    public int getStaticInitiative() {
        return staticInitiative;
    }

    public void setStaticInitiative(int staticInitiative) {
        this.staticInitiative = staticInitiative;
    }

    @Override
    public int getBaseDefense() {
        return baseDefense;
    }

    public void setBaseDefense(int baseDefense) {
        this.baseDefense = baseDefense;
    }

    @Override
    public int getStaticBaseDefense() {
        return staticBaseDefense;
    }

    public void setStaticBaseDefense(int staticBaseDefense) {
        this.staticBaseDefense = staticBaseDefense;
    }

    @Override
    public Map<IPrimaryAttribute, Integer> getPrimaryAttributes() {
        return primaryAttributes;
    }

    public void setPrimaryAttributes(Map<IPrimaryAttribute, Integer> primaryAttributes) {
        this.primaryAttributes = primaryAttributes;
    }

    @Override
    public Map<ISecondaryAttribute, Integer> getSecondaryAttributes() {
        return secondaryAttributes;
    }

    public void setSecondaryAttributes(Map<ISecondaryAttribute, Integer> secondaryAttributes) {
        this.secondaryAttributes = secondaryAttributes;
    }

    @Override
    public Collection<IMemberState> getMemberStates() {
        return memberStates;
    }

    public void setMemberStates(Collection<IMemberState> memberStates) {
        this.memberStates = memberStates;
    }

    @Override
    public Collection<IWeapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(Collection<IWeapon> weapons) {
        this.weapons = weapons;
    }

    @Override
    public Collection<IWeapon> getEquippedWeapons() {
        return equippedWeapons;
    }

    public void setEquippedWeapons(Collection<IWeapon> equippedWeapons) {
        this.equippedWeapons = equippedWeapons;
    }

    @Override
    public Collection<IJewellery> getJewellery() {
        return jewellery;
    }

    public void setJewellery(Collection<IJewellery> jewellery) {
        this.jewellery = jewellery;
    }

    @Override
    public Collection<IJewellery> getEquippedJewellery() {
        return equippedJewellery;
    }

    public void setEquippedJewellery(Collection<IJewellery> equippedJewellery) {
        this.equippedJewellery = equippedJewellery;
    }

    @Override
    public Collection<IArmor> getArmor() {
        return armor;
    }

    public void setArmor(Collection<IArmor> armor) {
        this.armor = armor;
    }

    @Override
    public Map<IArmorPosition, IArmor> getEquippedArmor() {
        return equippedArmor;
    }

    public void setEquippedArmor(Map<IArmorPosition, IArmor> equippedArmor) {
        this.equippedArmor = equippedArmor;
    }

    @Override
    public Collection<ISpell> getSpells() {
        return spells;
    }

    public void setSpells(Collection<ISpell> spells) {
        this.spells = spells;
    }

    @Override
    public Map<ITalent, Integer> getTalents() {
        return talents;
    }

    public void setTalents(Map<ITalent, Integer> talents) {
        this.talents = talents;
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    public void setInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public ILootTable getLootTable() {
        return lootTable;
    }

    public void setLootTable(ILootTable lootTable) {
        this.lootTable = lootTable;
    }
}
