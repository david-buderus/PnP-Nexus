package de.pnp.manager.model.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.battle.Battle;
import de.pnp.manager.model.character.data.*;
import de.pnp.manager.model.character.part.SecondaryAttributeModifier;
import de.pnp.manager.model.character.state.*;
import de.pnp.manager.model.character.state.interfaces.IMemberStateImpl;
import de.pnp.manager.model.interfaces.ILootable;
import de.pnp.manager.model.item.*;
import de.pnp.manager.model.loot.ILootTable;
import de.pnp.manager.model.loot.LootTable;
import de.pnp.manager.model.other.ISpell;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.model.other.Talent;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@JsonSerialize(as = IPnPCharacter.class)
public class PnPCharacter implements IPnPCharacter, ILootable {

    protected StringProperty name;
    protected IntegerProperty level;
    protected Battle battle;
    protected final String characterID;

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

    protected IntegerProperty startValue;
    protected IntegerProperty counter;
    protected IntegerProperty turns;
    protected StringProperty notes;

    protected PnPCharacter(String characterID, Battle battle) {
        this(characterID, battle, new LootTable());
    }

    /**
     * Initializes everything except the modifier bindings and the properties (except name and level)
     */
    protected PnPCharacter(String characterID, Battle battle, ILootTable lootTable) {
        this.characterID = characterID;
        this.name = new SimpleStringProperty(LanguageUtility.getMessage("battleMember.defaultName"));
        this.level = new SimpleIntegerProperty(1);
        this.advantages = new ArrayList<>();
        this.disadvantages = new ArrayList<>();
        this.primaryAttributes = new HashMap<>();
        for (PrimaryAttribute attribute : PrimaryAttribute.getValuesWithoutDummy()) {
            this.primaryAttributes.put(attribute, 0);
        }
        this.secondaryAttributes = new HashMap<>();
        for (SecondaryAttribute attribute : SecondaryAttribute.values()) {
            this.secondaryAttributes.put(attribute, 0);
        }
        this.lootTable = lootTable;
        this.inventory = Inventory.createUnlimitedInventory();
        this.battle = battle;
        this.startValue = new SimpleIntegerProperty(Utility.getConfig().getInt("character.initiative.start"));
        this.counter = new SimpleIntegerProperty(startValue.get());
        this.turns = new SimpleIntegerProperty(1);
        this.memberStates = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.weapons = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.equippedWeapons = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.jewellery = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.equippedJewellery = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.armor = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.equippedArmor = new SimpleMapProperty<>(FXCollections.observableMap(new HashMap<>()));
        this.spells = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.talents = new SimpleMapProperty<>(FXCollections.observableMap(new HashMap<>()));
        this.notes = new SimpleStringProperty("");
        for (Talent talent : Database.talentList) {
            this.talents.put(talent, new SimpleIntegerProperty(0));
        }

        Consumer<Equipment> weaponListener = weapon -> {
            if (weapon instanceof Weapon) {
                getWeapons().remove(weapon);
                getEquippedWeapons().remove(weapon);
            }
        };

        Consumer<Equipment> armorListener = armor -> {
            if (armor instanceof Armor) {
                ArmorPosition position = ArmorPosition.getArmorPosition(armor.getSubtype());

                if (getEquippedArmor().get(position).equals(armor)) {
                    getEquippedArmor().put(position, null);
                }

                getArmor().remove(armor);
            }
        };

        //Prepare weapons
        this.weapons.addListener((ListChangeListener<? super IWeapon>) ob -> {
            while (ob.next()) {
                for (IWeapon weapon : ob.getRemoved()) {
                    if (weapon instanceof Weapon) {
                        ((Weapon) weapon).removeOnBreakListener(weaponListener);
                    }
                }

                for (IWeapon weapon : ob.getAddedSubList()) {
                    if (weapon instanceof Weapon) {
                        ((Weapon) weapon).addOnBreakListener(weaponListener);
                    }
                }
            }
        });
        this.equippedWeapons.addListener((ListChangeListener<? super IWeapon>) ob -> {
            while (ob.next()) {
                while (ob.next()) {
                    for (IWeapon weapon : ob.getRemoved()) {
                        if (weapon instanceof Weapon) {
                            ((Weapon) weapon).removeOnBreakListener(weaponListener);
                        }
                    }

                    for (IWeapon weapon : ob.getAddedSubList()) {
                        if (weapon instanceof Weapon) {
                            ((Weapon) weapon).addOnBreakListener(weaponListener);
                        }
                    }
                }

                ObservableList<IWeapon> list = getEquippedWeapons();

                // TODO add initiativeModifier from weapons
                try {
                    if (list.size() == 1) {
                        double init = Math.ceil(list.get(0).getInitiative());
                        this.secondaryAttributeModifiers.get(SecondaryAttribute.INITIATIVE).setModifier((int) init);
                    }
                    if (list.size() == 2) {
                        double init1 = list.get(0).getInitiative();
                        double init2 = list.get(1).getInitiative();
                        this.secondaryAttributeModifiers.get(SecondaryAttribute.INITIATIVE).setModifier(
                                (int) Math.min(Math.min(Math.floor(init1), Math.floor(init2)), Math.floor(init1 + init2))
                        );
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        //Prepare armor
        this.armor.addListener((ListChangeListener<? super IArmor>) ob -> {
            while (ob.next()) {
                for (IArmor armor : ob.getRemoved()) {
                    if (armor instanceof Armor) {
                        ((Armor) armor).removeOnBreakListener(armorListener);
                    }
                }

                for (IArmor armor : ob.getAddedSubList()) {
                    if (armor instanceof Armor) {
                        ((Armor) armor).addOnBreakListener(armorListener);
                    }
                }
            }
        });
        this.equippedArmor.addListener((MapChangeListener<IArmorPosition, IArmor>) change -> {
            if (change.wasRemoved()) {
                IArmor prevArmor = change.getValueRemoved();
                if (prevArmor instanceof Armor) {
                    ((Armor) prevArmor).removeOnBreakListener(armorListener);
                }
            }
            if (change.wasAdded()) {
                IArmor newArmor = change.getValueAdded();
                if (newArmor instanceof Armor) {
                    ((Armor) newArmor).addOnBreakListener(armorListener);
                }
            }
        });
    }

    public void nextTurn() {
        if (isDead()) {
            return;
        }

        this.memberStates.removeIf(state -> state.getDuration() < 1);


        this.turns.set(0);
        this.counter.set(getCounter() - getInitiative());

        while (getCounter() < 1) {
            turns.set(getTurns() + 1);
            this.counter.set(getCounter() + startValue.get());
        }

        for (IMemberState state : memberStates) {
            if (state instanceof IManipulatingMemberState) {
                ((IManipulatingMemberState) state).apply(this);
            }
            state.decreaseDuration(false);
            state.decreaseDuration(true, getTurns());
        }
    }

    public void heal(int amount, PnPCharacter source) {
        this.health.set(Math.min(getHealth() + amount, getMaxHealth()));
        battle.addToHealStatistic(source, amount);
    }

    public void takeDamage(int amount, AttackTypes type, boolean withShield, double penetration,
                    double block, PnPCharacter source) {
        for (IMemberState state : this.memberStates) {
            if (state instanceof IIncomingDamageMemberState) {
                amount = ((IIncomingDamageMemberState) state).apply(this, amount);
            }
        }

        int damage = Math.max(0, amount - calculateDefense(type, withShield, penetration, block));
        this.health.set(getHealth() - damage);
        battle.addToDamageStatistic(source, damage);

        if (withShield) {
            for (IWeapon shield : new ArrayList<>(getEquippedWeapons())) {
                if (shield.isShield()) {
                    shield.applyWear();
                }
            }

            if (getShieldProtection() < amount) {
                for (IArmor armor : new ArrayList<>(getEquippedArmor().values())) {
                    if (armor.getSubtype().equalsIgnoreCase(type.toStringProperty().get())) {
                        armor.applyWear();
                    }
                }
            }

        } else {
            for (IArmor armor : new ArrayList<>(getEquippedArmor().values())) {
                if (armor.getSubtype().equalsIgnoreCase(type.toStringProperty().get())) {
                    armor.applyWear();
                }
            }
        }
    }

    public void reset() {
        this.memberStates.clear();
        this.counter.set(getStartValue());
        this.turns.set(1);
    }

    public void applyWearOnWeapons() {
        for (IWeapon weapon : new ArrayList<>(getEquippedWeapons())) {
            if (!weapon.isShield()) {
                weapon.applyWear();
            }
        }
    }

    protected int calculateDefense(AttackTypes type, boolean withShield, double penetration, double block) {
        int armorDefense = 0;
        double reduction = 1 - penetration;

        switch (type) {
            case DIRECT:
                return 0;
            case HEAD:
                armorDefense += getProtection(ArmorPosition.HEAD);
                break;
            case UPPER_BODY:
                armorDefense += getProtection(ArmorPosition.UPPER_BODY);
                break;
            case ARM:
                armorDefense += getProtection(ArmorPosition.ARM);
                break;
            case LEGS:
                armorDefense += getProtection(ArmorPosition.LEGS);
                break;
        }
        if (withShield) {
            armorDefense += getShieldProtection() * block;
        }
        armorDefense *= reduction;

        return armorDefense + getBaseDefense();
    }

    @Override
    public int getInitiative() {
        int init = getStaticInitiative();

        for (IMemberState state : memberStates) {
            if (state instanceof IAbsolutInitiativeMemberState) {
                init = ((IAbsolutInitiativeMemberState) state).apply(this, init);
            }
        }

        float relativeChange = 1;

        for (IMemberState state : memberStates) {
            if (state instanceof IRelativeInitiativeMemberState) {
                relativeChange = ((IRelativeInitiativeMemberState) state).apply(this, relativeChange);
            }
        }

        init *= relativeChange;

        return Math.max(init, 0);
    }

    @Override
    public int getBaseDefense() {
        int defense = getStaticBaseDefense();

        for (IMemberState state : this.memberStates) {
            if (state instanceof IDefenseMemberState) {
                defense = ((IDefenseMemberState) state).apply(this, defense);
            }
        }

        return defense;
    }

    public PnPCharacterInfo asInfo() {
        return new PnPCharacterInfo(getCharacterID(), getName());
    }

    public PnPCharacter cloneCharacter(String characterID, Battle battle) {
        PnPCharacter character = new PnPCharacter(characterID, battle, lootTable);

        character.setName(getName());
        character.setLevel(getLevel());

        // Load stats
        for (IPrimaryAttribute attribute : getPrimaryAttributes().keySet()) {
            character.primaryAttributes.put(attribute, getPrimaryAttributes().get(attribute));
        }
        for (ISecondaryAttribute attribute : getSecondaryAttributes().keySet()) {
            character.secondaryAttributes.put(attribute, getSecondaryAttributes().get(attribute));
        }

        character.createModifierBindings();

        for (ISecondaryAttribute attribute : secondaryAttributeModifiers.keySet()) {
            character.getSecondaryAttributeModifier(attribute).setModifier(getSecondaryAttributeModifier(attribute).getModifier());
        }

        character.createResourceProperties();

        // Load info and co
        character.advantages = new ArrayList<>(getAdvantages());
        character.disadvantages = new ArrayList<>(getDisadvantages());
        character.notes.set(getNotes());
        character.inventory = new Inventory(getInventory().getMaxSize(), getInventory().getNumberOfSlots(), getInventory());

        // Load Equipment
        for (IWeapon weapon : getEquippedWeapons()) {
            character.equippedWeapons.add(weapon.copy());
        }
        for (IWeapon weapon : getWeapons()) {
            character.weapons.add(weapon.copy());
        }
        character.equippedArmor.put(ArmorPosition.HEAD, getEquippedArmor().get(ArmorPosition.HEAD));
        character.equippedArmor.put(ArmorPosition.UPPER_BODY, getEquippedArmor().get(ArmorPosition.UPPER_BODY));
        character.equippedArmor.put(ArmorPosition.ARM, getEquippedArmor().get(ArmorPosition.ARM));
        character.equippedArmor.put(ArmorPosition.LEGS, getEquippedArmor().get(ArmorPosition.LEGS));
        for (IArmor armor : getArmor()) {
            character.armor.add(armor.copy());
        }
        for (IJewellery jewellery : getEquippedJewellery()) {
            character.equippedJewellery.add(jewellery.copy());
        }
        for (IJewellery jewellery : getJewellery()) {
            character.jewellery.add(jewellery.copy());
        }

        //Load Talents and spells
        character.spells.addAll(getSpells());
        for (ITalent talent : getTalents().keySet()) {
            character.talents.put(talent, new SimpleIntegerProperty(getTalents().get(talent)));
        }

        return character;
    }

    /**
     * Needs to be called after the SecondaryAttributes have been set
     */
    protected void createModifierBindings() {
        this.secondaryAttributeModifiers = new HashMap<>();
        for (SecondaryAttribute attribute : SecondaryAttribute.values()) {
            this.secondaryAttributeModifiers.put(attribute, new SecondaryAttributeModifier(this.secondaryAttributes.get(attribute)));
        }
    }

    /**
     * Needs to be called after {@link PnPCharacter#createModifierBindings()}
     * and after the start values of the modifiers has been set.
     */
    protected void createResourceProperties() {
        this.health = new SimpleIntegerProperty(getMaxHealth());
        this.mana = new SimpleIntegerProperty(getMaxMana());
        this.mentalHealth = new SimpleIntegerProperty(getMaxMentalHealth());
        this.health.addListener((ob, o, n) -> {
            if (isDead()) {
                this.turns.set(0);
            }
        });
        this.mentalHealth.addListener((ob, o, n) -> {
            if (isDead()) {
                this.turns.set(0);
            }
        });
    }

    @Override
    public ILootTable getFinishedLootTable() {
        LootTable result = new LootTable();
        result.add(lootTable);

        for (IEquipment equipment : getWeapons()) {
            result.add(equipment, 1,1);
        }
        for (IEquipment equipment : getEquippedWeapons()) {
            result.add(equipment, 1,1);
        }
        for (IEquipment equipment : getArmor()) {
            result.add(equipment, 1,1);
        }
        for (IEquipment equipment : getEquippedArmor().values()) {
            result.add(equipment, 1,1);
        }
        for (IEquipment equipment : getJewellery()) {
            result.add(equipment, 1,1);
        }
        for (IEquipment equipment : getEquippedJewellery()) {
            result.add(equipment, 1,1);
        }
        for (IItem item : getInventory()) {
            result.add(item, 1, 1);
        }

        return result;
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
    @JsonIgnore
    public int getMaxHealth() {
        return secondaryAttributeModifiers.get(SecondaryAttribute.HEALTH).getResult();
    }

    public IntegerBinding maxHealthProperty() {
        return secondaryAttributeModifiers.get(SecondaryAttribute.HEALTH).resultProperty();
    }

    public int getMaxHealthModifier() {
        return secondaryAttributeModifiers.get(SecondaryAttribute.HEALTH).getModifier();
    }

    public IntegerProperty maxHealthModifierProperty() {
        return secondaryAttributeModifiers.get(SecondaryAttribute.HEALTH).modifierProperty();
    }

    public void setMaxHealthModifier(int maxHealthModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.HEALTH).setModifier(maxHealthModifier);
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

    public  void decreaseMana(int amount, PnPCharacter source) {
        this.manaProperty().set(Math.max(0, this.getMana() - amount));
    }

    public  void increaseMana(int amount, PnPCharacter source) {
        this.manaProperty().set(Math.min(this.getMaxMana(), this.getMana() + amount));
    }

    @Override
    @JsonIgnore
    public int getMaxMana() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.MANA).getResult();
    }

    public IntegerBinding maxManaProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.MANA).resultProperty();
    }

    public int getMaxManaModifier() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.MANA).getModifier();
    }

    public IntegerProperty maxManaModifierProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.MANA).modifierProperty();
    }

    public void setMaxManaModifier(int maxManaModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.MANA).setModifier(maxManaModifier);
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
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.MENTAL_HEALTH).getResult();
    }

    public IntegerBinding maxMentalHealthProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.MENTAL_HEALTH).resultProperty();
    }

    public int getMaxMentalHealthModifier() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.MENTAL_HEALTH).getModifier();
    }

    public IntegerProperty maxMentalHealthModifierProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.MENTAL_HEALTH).modifierProperty();
    }

    public void setMaxMentalHealthModifier(int maxMentalHealthModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.MENTAL_HEALTH).setModifier(maxMentalHealthModifier);
    }

    /**
     * The initiative of the character.
     * Includes weapons except memberstates
     */
    public int getStaticInitiative() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.INITIATIVE).getResult();
    }

    public IntegerBinding staticInitiativeProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.INITIATIVE).resultProperty();
    }

    public int getStaticInitiativeModifier() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.INITIATIVE).getModifier();
    }

    public IntegerProperty staticInitiativeModifierProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.INITIATIVE).modifierProperty();
    }

    public void setStaticInitiativeModifier(int staticInitiativeModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.INITIATIVE).setModifier(staticInitiativeModifier);
    }

    /**
     * The base defense of the character
     * used to calculate incoming damage.
     * Includes all modifier except memberstates
     */
    public int getStaticBaseDefense() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.DEFENSE).getResult();
    }

    public IntegerBinding staticBaseDefenseProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.DEFENSE).resultProperty();
    }

    public int getStaticBaseDefenseModifier() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.DEFENSE).getModifier();
    }

    public IntegerProperty staticBaseDefenseModifierProperty() {
        return this.secondaryAttributeModifiers.get(SecondaryAttribute.DEFENSE).modifierProperty();
    }

    public void setStaticBaseDefenseModifier(int staticBaseDefenseModifier) {
        this.secondaryAttributeModifiers.get(SecondaryAttribute.DEFENSE).setModifier(staticBaseDefenseModifier);
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


    public void addState(IMemberStateImpl state) {
        this.memberStates.add(state);
    }

    public void removeState(IMemberStateImpl state) {
        this.memberStates.remove(state);
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

    public ObservableList<IArmor> getEquippedArmorAsList() {
        ObservableList<IArmor> list = FXCollections.observableList(new ArrayList<>());

        list.add(getEquippedArmor().getOrDefault(ArmorPosition.HEAD, new Armor()));
        list.add(getEquippedArmor().getOrDefault(ArmorPosition.UPPER_BODY, new Armor()));
        list.add(getEquippedArmor().getOrDefault(ArmorPosition.ARM, new Armor()));
        list.add(getEquippedArmor().getOrDefault(ArmorPosition.LEGS, new Armor()));

        list.addListener((ListChangeListener<IArmor>) c -> {
            list.clear();
            list.add(getEquippedArmor().get(ArmorPosition.HEAD));
            list.add(getEquippedArmor().get(ArmorPosition.UPPER_BODY));
            list.add(getEquippedArmor().get(ArmorPosition.ARM));
            list.add(getEquippedArmor().get(ArmorPosition.LEGS));
        });

        return list;
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

    @JsonIgnore
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

    public int getStartValue() {
        return startValue.get();
    }

    public IntegerProperty startValueProperty() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue.set(startValue);
    }

    public int getCounter() {
        return counter.get();
    }

    public IntegerProperty counterProperty() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter.set(counter);
    }

    public int getTurns() {
        return turns.get();
    }

    public IntegerProperty turnsProperty() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns.set(turns);
    }

    public SecondaryAttributeModifier getSecondaryAttributeModifier(ISecondaryAttribute attribute) {
        return secondaryAttributeModifiers.get(attribute);
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    @Override
    public String getCharacterID() {
        return characterID;
    }
}
