package de.pnp.manager.model.member;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.Battle;
import de.pnp.manager.model.item.IArmor;
import de.pnp.manager.model.item.IJewellery;
import de.pnp.manager.model.item.IWeapon;
import de.pnp.manager.model.loot.ILootTable;
import de.pnp.manager.model.member.data.*;
import de.pnp.manager.model.member.part.SecondaryAttributeModifier;
import de.pnp.manager.model.member.state.IMemberState;
import de.pnp.manager.model.other.ISpell;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.model.other.Talent;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@JsonSerialize(as = IPnPCharacter.class)
public class PnPCharacter implements IPnPCharacter {

    protected StringProperty name;
    protected IntegerProperty level;
    protected Battle battle;

    protected IntegerProperty health;
    protected IntegerProperty mana;
    protected IntegerProperty mentalHealth;
    protected ListProperty<IMemberState> memberStates;
    protected ListProperty<IWeapon> weapons;
    protected ListProperty<IWeapon> equippedWeapons;
    protected ListProperty<IJewellery> jewellery;
    protected ListProperty<IJewellery> equippedJewellery;
    protected ListProperty<IArmor> armor;
    protected MapProperty<IArmorPosition, IArmor> equippedArmor;
    protected ListProperty<ISpell> spells;
    protected MapProperty<ITalent, IntegerProperty> talents;

    protected Map<ISecondaryAttribute, SecondaryAttributeModifier> secondaryAttributeModifiers;

    protected Collection<String> advantages;
    protected Collection<String> disadvantages;
    protected Map<IPrimaryAttribute, Integer> primaryAttributes;
    protected Map<ISecondaryAttribute, Integer> secondaryAttributes;
    protected ILootTable lootTable;
    protected IInventory inventory;

    public PnPCharacter(Battle battle, ILootTable lootTable) {
        this.name = new SimpleStringProperty(LanguageUtility.getMessage("battleMember.defaultName"));
        this.level = new SimpleIntegerProperty(1);
        this.advantages = Collections.emptyList();
        this.disadvantages = Collections.emptyList();
        this.primaryAttributes = new HashMap<>();
        for (PrimaryAttribute attribute : PrimaryAttribute.getValuesWithoutDummy()) {
            this.primaryAttributes.put(attribute, 0);
        }
        this.secondaryAttributes = new HashMap<>();
        for (SecondaryAttribute attribute : SecondaryAttribute.values()) {
            this.secondaryAttributes.put(attribute, 0);
        }
        this.lootTable = lootTable;
        this.inventory = Inventory.EMPTY_INVENTORY;
        this.battle = battle;

        this.createModifierBindings();
        this.secondaryAttributeModifiers.get(SecondaryAttribute.health).setModifier(1);
        this.secondaryAttributeModifiers.get(SecondaryAttribute.mana).setModifier(1);
        this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).setModifier(1);
        this.secondaryAttributeModifiers.get(SecondaryAttribute.initiative).setModifier(1);

        this.createProperties();
    }

    @Override
    public int getInitiative() {
        return 0;
    }

    @Override
    public int getBaseDefense() {
        return 0;
    }

    protected void createModifierBindings() {
        this.secondaryAttributeModifiers = new HashMap<>();
        for (SecondaryAttribute attribute : SecondaryAttribute.values()) {
            this.secondaryAttributeModifiers.put(attribute, new SecondaryAttributeModifier(this.secondaryAttributes.get(attribute)));
        }
    }

    protected void createProperties() {
        this.health = new SimpleIntegerProperty(getMaxHealth());
        this.mana = new SimpleIntegerProperty(getMaxMana());
        this.mentalHealth = new SimpleIntegerProperty(getMaxMentalHealth());
        this.memberStates = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.weapons = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.equippedWeapons = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.jewellery = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.equippedJewellery = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.armor = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.equippedArmor = new SimpleMapProperty<>(FXCollections.observableMap(new HashMap<>()));
        this.spells = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.talents = new SimpleMapProperty<>(FXCollections.observableMap(new HashMap<>()));
        for (Talent talent : Database.talentList) {
            this.talents.put(talent, new SimpleIntegerProperty(0));
        }
    }

    @Override
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public void setLevel(int level) {
        this.level.set(level);
    }

    @Override
    public Collection<String> getAdvantages() {
        return advantages;
    }

    @Override
    public Collection<String> getDisadvantages() {
        return disadvantages;
    }

    @Override
    public int getHealth() {
        return health.get();
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

    @Override
    public int getMaxHealth() {
        return secondaryAttributeModifiers.get(SecondaryAttribute.health).getResult();
    }

    public IntegerBinding maxHealthProperty() {
        return secondaryAttributeModifiers.get(SecondaryAttribute.health).resultProperty();
    }

    public int getMaxHealthModifier() {
        return secondaryAttributeModifiers.get(SecondaryAttribute.health).getModifier();
    }

    public IntegerProperty maxHealthModifierProperty() {
        return secondaryAttributeModifiers.get(SecondaryAttribute.health).modifierProperty();
    }

    public void setMaxHealthModifier(int maxHealthModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.health).setModifier(maxHealthModifier);
    }

    @Override
    public int getMana() {
        return mana.get();
    }

    public IntegerProperty manaProperty() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana.set(mana);
    }

    @Override
    public int getMaxMana() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mana).getResult();
    }

    public IntegerBinding maxManaProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mana).resultProperty();
    }

    public int getMaxManaModifier() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mana).getModifier();
    }

    public IntegerProperty maxManaModifierProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mana).modifierProperty();
    }

    public void setMaxManaModifier(int maxManaModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.mana).setModifier(maxManaModifier);
    }

    @Override
    public int getMentalHealth() {
        return mentalHealth.get();
    }

    public IntegerProperty mentalHealthProperty() {
        return mentalHealth;
    }

    public void setMentalHealth(int mentalHealth) {
        this.mentalHealth.set(mentalHealth);
    }

    @Override
    public int getMaxMentalHealth() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).getResult();
    }

    public IntegerBinding maxMentalHealthProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).resultProperty();
    }

    public int getMaxMentalHealthModifier() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).getModifier();
    }

    public IntegerProperty maxMentalHealthModifierProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).modifierProperty();
    }

    public void setMaxMentalHealthModifier(int maxMentalHealthModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).setModifier(maxMentalHealthModifier);
    }

    @Override
    public int getStaticInitiative() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.initiative).getResult();
    }

    public IntegerBinding staticInitiativeProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).resultProperty();
    }

    public int getStaticInitiativeModifier() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).getModifier();
    }

    public IntegerProperty staticInitiativeModifierProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).modifierProperty();
    }

    public void setStaticInitiativeModifier(int staticInitiativeModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.mentalHealth).setModifier(staticInitiativeModifier);
    }

    @Override
    public int getStaticBaseDefense() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.defense).getResult();
    }

    public IntegerBinding staticBaseDefenseProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.defense).resultProperty();
    }

    public int getStaticBaseDefenseModifier() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.defense).getModifier();
    }

    public IntegerProperty staticBaseDefenseModifierProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.defense).modifierProperty();
    }

    public void setStaticBaseDefenseModifier(int staticBaseDefenseModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.defense).setModifier(staticBaseDefenseModifier);
    }

    @Override
    public Map<IPrimaryAttribute, Integer> getPrimaryAttributes() {
        return primaryAttributes;
    }

    @Override
    public Map<ISecondaryAttribute, Integer> getSecondaryAttributes() {
        return secondaryAttributes;
    }

    @Override
    public ObservableList<IMemberState> getMemberStates() {
        return memberStates.get();
    }

    public ListProperty<IMemberState> memberStatesProperty() {
        return memberStates;
    }

    public void setMemberStates(ObservableList<IMemberState> memberStates) {
        this.memberStates.set(memberStates);
    }

    @Override
    public ObservableList<IWeapon> getWeapons() {
        return weapons.get();
    }

    public ListProperty<IWeapon> weaponsProperty() {
        return weapons;
    }

    public void setWeapons(ObservableList<IWeapon> weapons) {
        this.weapons.set(weapons);
    }

    @Override
    public ObservableList<IWeapon> getEquippedWeapons() {
        return equippedWeapons.get();
    }

    public ListProperty<IWeapon> equippedWeaponsProperty() {
        return equippedWeapons;
    }

    public void setEquippedWeapons(ObservableList<IWeapon> equippedWeapons) {
        this.equippedWeapons.set(equippedWeapons);
    }

    @Override
    public ObservableList<IJewellery> getJewellery() {
        return jewellery.get();
    }

    public ListProperty<IJewellery> jewelleryProperty() {
        return jewellery;
    }

    public void setJewellery(ObservableList<IJewellery> jewellery) {
        this.jewellery.set(jewellery);
    }

    @Override
    public ObservableList<IJewellery> getEquippedJewellery() {
        return equippedJewellery.get();
    }

    public ListProperty<IJewellery> equippedJewelleryProperty() {
        return equippedJewellery;
    }

    public void setEquippedJewellery(ObservableList<IJewellery> equippedJewellery) {
        this.equippedJewellery.set(equippedJewellery);
    }

    @Override
    public ObservableList<IArmor> getArmor() {
        return armor.get();
    }

    public ListProperty<IArmor> armorProperty() {
        return armor;
    }

    public void setArmor(ObservableList<IArmor> armor) {
        this.armor.set(armor);
    }

    @Override
    public ObservableMap<IArmorPosition, IArmor> getEquippedArmor() {
        return equippedArmor.get();
    }

    public MapProperty<IArmorPosition, IArmor> equippedArmorProperty() {
        return equippedArmor;
    }

    public void setEquippedArmor(ObservableMap<IArmorPosition, IArmor> equippedArmor) {
        this.equippedArmor.set(equippedArmor);
    }

    @Override
    public ObservableList<ISpell> getSpells() {
        return spells.get();
    }

    public ListProperty<ISpell> spellsProperty() {
        return spells;
    }

    public void setSpells(ObservableList<ISpell> spells) {
        this.spells.set(spells);
    }

    @Override
    public Map<ITalent, Integer> getTalents() {
        return talents.get().entrySet()
                .stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get()
                ));
    }

    public ObservableMap<ITalent, IntegerProperty> getObservableTalents() {
        return talents.get();
    }

    public void setTalents(Map<ITalent, IntegerProperty> talents) {
        for (ITalent talent : talents.keySet()) {
            this.talents.put(talent, talents.get(talent));
        }
    }

    public MapProperty<ITalent, IntegerProperty> talentsProperty() {
        return talents;
    }

    public void setObservableTalents(ObservableMap<ITalent, IntegerProperty> talents) {
        this.talents.set(talents);
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public ILootTable getLootTable() {
        return lootTable;
    }
}
