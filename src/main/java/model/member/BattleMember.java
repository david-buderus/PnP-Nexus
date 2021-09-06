package model.member;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import manager.*;
import model.Battle;
import model.item.Armor;
import model.item.Item;
import model.item.Weapon;
import model.loot.LootTable;
import model.member.data.ArmorPiece;
import model.member.data.AttackTypes;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.interfaces.IBattleMember;
import model.member.state.interfaces.*;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

public class BattleMember extends Member implements IBattleMember {

    protected final Map<SecondaryAttribute, IntegerProperty> secondaryAttributes = new HashMap<>();

    protected IntegerProperty life;
    protected IntegerProperty mana;
    protected IntegerProperty startValue;
    protected IntegerProperty counter;
    protected IntegerProperty turns;
    protected IntegerProperty level;

    protected LootTable lootTable = new LootTable();
    protected HashMap<ArmorPiece, IntegerProperty> armor;

    protected Battle battle;

    private final ListProperty<IMemberState> states;

    /**
     * Creates a BattleMember with default stats
     * and an empty LootTable
     *
     * @param battle the BattleMember is part of
     */
    public BattleMember(Battle battle) {
        this(battle, new LootTable());
    }

    /**
     * Copies values of the armor map into the own property.
     * These properties won't be linked!
     *
     * @param battle    the BattleMember is part of
     * @param lootTable of the given BattleMember
     * @param armor     which will be used as Armor
     */
    public BattleMember(Battle battle, LootTable lootTable, HashMap<ArmorPiece, IntegerProperty> armor) {
        this(battle, lootTable);
        for (ArmorPiece piece : armor.keySet()) {
            this.armor.get(piece).set(armor.get(piece).get());
        }
    }

    /**
     * Creates a BattleMember with default stats
     *
     * @param battle    the BattleMember is part of
     * @param lootTable of the given BattleMember
     */
    public BattleMember(Battle battle, LootTable lootTable) {
        super();
        this.name.set(LanguageUtility.getMessage("battleMember.defaultName"));
        this.life = new SimpleIntegerProperty(1);
        this.mana = new SimpleIntegerProperty(1);
        this.startValue = new SimpleIntegerProperty(Utility.getConfig().getInt("character.initiative.start"));
        this.counter = new SimpleIntegerProperty(startValue.get());
        this.turns = new SimpleIntegerProperty(1);
        this.states = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.level = new SimpleIntegerProperty(1);

        secondaryAttributes.put(SecondaryAttribute.health, new SimpleIntegerProperty(1));
        secondaryAttributes.put(SecondaryAttribute.mana, new SimpleIntegerProperty(1));
        secondaryAttributes.put(SecondaryAttribute.initiative, new SimpleIntegerProperty(1));
        secondaryAttributes.put(SecondaryAttribute.defense, new SimpleIntegerProperty(0));


        this.life.addListener((ob, o, n) -> {
            if (isDead()) {
                this.turns.set(0);
            }
        });

        this.battle = battle;
        this.lootTable = lootTable;
        this.armor = new HashMap<>();

        for (ArmorPiece piece : ArmorPiece.values()) {
            this.armor.put(piece, new SimpleIntegerProperty(0));
        }

    }

    public BattleMember(CharacterSheetParameterMap parameterMap) {
        this.name = new SimpleStringProperty(parameterMap.getValueAsStringOrElse("character.name", LanguageUtility.getMessage("battleMember.defaultName")));
        this.life = new SimpleIntegerProperty(1);
        this.mana = new SimpleIntegerProperty(1);
        secondaryAttributes.put(SecondaryAttribute.health, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.health", 1)));
        secondaryAttributes.put(SecondaryAttribute.mana, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.mana", 1)));
        secondaryAttributes.put(SecondaryAttribute.initiative, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.initiative", 1)));
        secondaryAttributes.put(SecondaryAttribute.defense, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.defense", 1)));
        this.startValue = new SimpleIntegerProperty(Utility.getConfig().getInt("character.initiative.start"));
        this.counter = new SimpleIntegerProperty(startValue.get());
        this.turns = new SimpleIntegerProperty(1);
        this.states = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.level = new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("character.level", 1));

        this.armor = new HashMap<>();

        Collection<Item> allItems = new ArrayList<>();

        // TODO if null, we need to create database entry
        String armorHead = parameterMap.getValueAsStringOrElse("character.armor.head", "");
        if (!armorHead.isBlank()) {
            Item itemHead = Database.getItemOrElse(armorHead, null);
            this.armor.put(ArmorPiece.head, itemHead == null ? new SimpleIntegerProperty(0) : ((Armor) itemHead).protectionProperty());
            allItems.add(itemHead);
        }

        String armorChest = parameterMap.getValueAsStringOrElse("character.armor.upperBody", "");
        if (!armorChest.isBlank()) {
            Item itemChest = Database.getItemOrElse(armorChest, null);
            this.armor.put(ArmorPiece.upperBody, itemChest == null ? new SimpleIntegerProperty(0) : ((Armor) itemChest).protectionProperty());
            allItems.add(itemChest);
        }

        String armorLegs = parameterMap.getValueAsStringOrElse("character.armor.legs", "");
        if (!armorLegs.isBlank()) {
            Item itemLegs = Database.getItemOrElse(armorLegs, null);
            this.armor.put(ArmorPiece.legs, itemLegs == null ? new SimpleIntegerProperty(0) : ((Armor) itemLegs).protectionProperty());
            allItems.add(itemLegs);
        }

        String armorArms = parameterMap.getValueAsStringOrElse("character.armor.arm", "");
        if (!armorArms.isBlank()) {
            Item itemArms = Database.getItemOrElse(armorArms, null);
            this.armor.put(ArmorPiece.arm, itemArms == null ? new SimpleIntegerProperty(0) : ((Armor) itemArms).protectionProperty());
            allItems.add(itemArms);
        }

        // TODO currently assume that weapons 1 and 2 are equipped
        boolean hasShield = false;

        // TODO correct weapon loading
        String weapon1 = parameterMap.getValueAsStringOrElse("character.weapon.1", "");
        if (!weapon1.isBlank()) {
            Item weaponItem1 = Database.getItemOrElse(weapon1, null);
            allItems.add(weaponItem1);

            if (weaponItem1 != null) {
                if (weaponItem1 instanceof Weapon) {
                    if (((Weapon) weaponItem1).isShield()) {
                        hasShield = true;
                        this.armor.put(ArmorPiece.shield, ((Weapon) weaponItem1).damageProperty());
                    }
                }
            }
        }

        String weapon2 = parameterMap.getValueAsStringOrElse("character.weapon.2", "");
        if (!weapon2.isBlank()) {
            Item weaponItem2 = Database.getItemOrElse(weapon2, null);
            allItems.add(weaponItem2);

            // TODO currently only wearing one shield is supported
            if (!hasShield && weaponItem2 != null) {
                if (weaponItem2 instanceof Weapon) {
                    if (((Weapon) weaponItem2).isShield()) {
                        this.armor.put(ArmorPiece.shield, ((Weapon) weaponItem2).damageProperty());
                    }
                }
            }
        }
        String weapon3 = parameterMap.getValueAsStringOrElse("character.weapon.3", "");
        if (!weapon3.isBlank()) {
            Item weaponItem3 = Database.getItemOrElse(weapon3, null);
            allItems.add(weaponItem3);
        }
        String weapon4 = parameterMap.getValueAsStringOrElse("character.weapon.4", "");
        if (!weapon4.isBlank()) {
            Item weaponItem4 = Database.getItemOrElse(weapon4, null);
            allItems.add(weaponItem4);
        }

        // Loot
        allItems.stream().filter(Objects::nonNull).forEach(item -> lootTable.add(item, 1, 1));
    }

    public BattleMember(Workbook wb) {
       /* LootTable lootTable = new LootTable();

        String lootName = Utility.getConfig().getString("character.sheet.loot");
        if (LanguageUtility.hasMessage("character.sheet." + lootName)) {
            lootName = LanguageUtility.getMessage("character.sheet." + lootName);
        }

        Sheet loot = wb.getSheet(lootName);

        for (Row row : loot) {
            if (row.getRowNum() > 0) {
                String name = WorkbookUtility.getValue(row, 0);

                if (name.isEmpty() || name.equals("0")) {
                    continue;
                }

                int amount = (int) row.getCell(1).getNumericCellValue();
                double chance = row.getCell(2).getNumericCellValue();

                lootTable.add(name, amount, chance);
            }
        }

        Configuration config = Utility.getConfig();
        String charName = config.getString("character.sheet.enemy");
        if (LanguageUtility.hasMessage("character.sheet." + charName)) {
            charName = LanguageUtility.getMessage("character.sheet." + charName);
        }

        Sheet character = wb.getSheet(charName);

        this.setName(WorkbookUtility.getStringInCell(character, config.getString("character.cell.name")));
        this.setMaxLife(WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.maxLife")));
        this.setMaxMana(WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.maxMana")));
        this.setInitiative(WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.initiative")));
        this.setLevel(WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.level")));

        // Protection
        this.setArmor(ArmorPiece.head, WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.armor.head")));
        this.setArmor(ArmorPiece.upperBody, WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.armor.upperBody")));
        this.setArmor(ArmorPiece.legs, WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.armor.legs")));
        this.setArmor(ArmorPiece.arm, WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.armor.arm")));

        if (WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.hasShield")) == 2) {
            this.setArmor(ArmorPiece.shield, WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.armor.shield")));
        }

        this.setDefense(WorkbookUtility.getIntegerInCell(character, config.getString("character.cell.defense")));
        this.states = new SimpleListProperty<>(FXCollections.observableArrayList());*/

        this(CharacterSheetParser.parseCharacterSheet(wb));
    }

    public void nextTurn() {
        if (isDead()) {
            return;
        }

        this.states.removeIf(state -> state.getDuration() < 1);


        this.turns.set(0);
        this.counter.set(getCounter() - calculateInitiative());

        while (getCounter() < 1) {
            turns.set(getTurns() + 1);
            this.counter.set(getCounter() + startValue.get());
        }

        for (IMemberState state : states) {
            if (state instanceof IManipulatingMemberState) {
                ((IManipulatingMemberState) state).apply(this);
            }
            state.decreaseDuration(false);
            state.decreaseDuration(true, getTurns());
        }
    }

    public void applyWearOnWeapons() {
    }

    private int calculateInitiative() {

        int init = getInitiative();

        for (IMemberState state : states) {
            if (state instanceof IAbsolutInitiativeMemberState) {
                init = ((IAbsolutInitiativeMemberState) state).apply(this, init);
            }
        }

        float relativeChange = 1;

        for (IMemberState state : states) {
            if (state instanceof IRelativeInitiativeMemberState) {
                relativeChange = ((IRelativeInitiativeMemberState) state).apply(this, relativeChange);
            }
        }

        init *= relativeChange;

        return Math.max(init, 0);
    }

    public void heal(int amount, IBattleMember source) {
        this.life.set(Math.min(getLife() + amount, getMaxLife()));
        battle.addToHealStatistic(source, amount);
    }

    public void takeDamage(int amount, AttackTypes type, boolean withShield, double penetration, double block, IBattleMember source) {
        for (IMemberState state : this.states) {
            if (state instanceof IIncomingDamageMemberState) {
                amount = ((IIncomingDamageMemberState) state).apply(this, amount);
            }
        }

        int damage = Math.max(0, amount - calculateDefense(type, withShield, penetration, block));
        this.life.set(getLife() - damage);
        battle.addToDamageStatistic(source, damage);
    }

    private int calculateDefense(AttackTypes type, boolean withShield, double penetration, double block) {
        int defense = 0;
        double reduction = 1 - penetration;

        switch (type) {
            case direct:
                return 0;
            case head:
                defense += getArmor(ArmorPiece.head);
                break;
            case upperBody:
                defense += getArmor(ArmorPiece.upperBody);
                break;
            case arm:
                defense += getArmor(ArmorPiece.arm);
                break;
            case legs:
                defense += getArmor(ArmorPiece.legs);
                break;
        }
        if (withShield) {
            defense += getArmor(ArmorPiece.shield) * block;
        }
        defense *= reduction;

        for (IMemberState state : this.states) {
            if (state instanceof IDefenseMemberState) {
                defense = ((IDefenseMemberState) state).apply(this, defense);
            }
        }

        return defense + baseDefenseProperty().get();
    }

    public void reset() {
        states.clear();
        this.counter.set(getStartValue());
        this.turns.set(1);
    }

    public void addState(IMemberState state) {
        this.states.add(state);
    }

    public void removeState(IMemberState state) {
        this.states.remove(state);
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    protected void setArmor(ArmorPiece target, int defense) {
        this.armor.get(target).unbind();
        this.armor.get(target).set(defense);
    }

    /**
     * Creates a new IntegerProperty and binds it
     * to the ObservableValue
     *
     * @param target  the specific ArmorPiece
     * @param defense ObservableValue which will get binded
     */
    protected void setArmor(ArmorPiece target, ObservableValue<Number> defense) {
        IntegerProperty property = new SimpleIntegerProperty(defense.getValue().intValue());
        property.bind(defense);
        this.armor.put(target, property);
    }

    public BattleMember cloneMember() {
        BattleMember member = new BattleMember(battle, lootTable, armor);
        member.setDefense(baseDefenseProperty().get());
        member.setName(getName());
        member.setMaxLife(getMaxLife());
        member.setLife(getLife());
        member.setMaxMana(getMaxMana());
        member.setMana(getMana());
        member.setInitiative(getInitiative());
        member.startValue.set(getStartValue());
        member.counter.set(getCounter());
        member.turns.set(getTurns());

        return member;
    }

    protected void setDefense(int defense) {
        this.baseDefenseProperty().set(defense);
    }

    protected void setMaxLife(int life) {
        this.maxLifeProperty().set(life);
        this.life.set(life);
    }

    protected void setMaxMana(int mana) {
        this.maxManaProperty().set(mana);
        this.mana.set(mana);
    }

    private void setInitiative(int init) {
        this.initiativeProperty().set(init);
    }

    protected void setLife(int life) {
        this.life.set(Math.max(0, Math.min(life, getMaxLife())));
    }

    public IntegerProperty lifeProperty() {
        return life;
    }

    @Override
    public IntegerProperty maxLifeProperty() {
        return secondaryAttributes.get(SecondaryAttribute.health);
    }

    @Override
    public IntegerProperty maxManaProperty() {
        return secondaryAttributes.get(SecondaryAttribute.mana);
    }

    protected void setMana(int mana) {
        this.mana.set(Math.max(0, Math.min(mana, getMaxMana())));
    }

    public IntegerProperty manaProperty() {
        return mana;
    }

    public IntegerProperty initiativeProperty() {
        return secondaryAttributes.get(SecondaryAttribute.initiative);
    }

    public IntegerProperty startValueProperty() {
        return startValue;
    }

    public IntegerProperty counterProperty() {
        return counter;
    }

    public IntegerProperty turnsProperty() {
        return turns;
    }

    public ListProperty<IMemberState> statesProperty() {
        return states;
    }

    public IntegerProperty baseDefenseProperty() {
        return secondaryAttributes.get(SecondaryAttribute.defense);
    }

    protected void setLevel(int level) {
        this.level.set(level);
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public IntegerProperty armorProperty(ArmorPiece piece) {
        return armor.get(piece);
    }
}