package model.member;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import manager.LanguageUtility;
import manager.Utility;
import manager.WorkbookUtility;
import model.Battle;
import model.loot.LootTable;
import model.member.data.ArmorPiece;
import model.member.data.AttackTypes;
import model.member.interfaces.IBattleMember;
import model.member.state.interfaces.*;

import java.util.HashMap;
import org.apache.commons.configuration2.Configuration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class BattleMember extends Member implements IBattleMember {

    protected IntegerProperty life;
    protected IntegerProperty maxLife;
    protected IntegerProperty mana;
    protected IntegerProperty maxMana;
    protected IntegerProperty initiative;
    protected IntegerProperty startValue;
    protected IntegerProperty counter;
    protected IntegerProperty turns;
    protected IntegerProperty baseDefense;
    protected IntegerProperty level;

    protected LootTable lootTable;
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
        this.maxLife = new SimpleIntegerProperty(1);
        this.mana = new SimpleIntegerProperty(1);
        this.maxMana = new SimpleIntegerProperty(1);
        this.initiative = new SimpleIntegerProperty(1);
        this.startValue = new SimpleIntegerProperty(Utility.getConfig().getInt("character.initiative.start"));
        this.counter = new SimpleIntegerProperty(startValue.get());
        this.turns = new SimpleIntegerProperty(1);
        this.states = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.baseDefense = new SimpleIntegerProperty(0);
        this.level = new SimpleIntegerProperty(1);

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

    public BattleMember(Workbook wb) {
        LootTable lootTable = new LootTable();

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
        this.states = new SimpleListProperty<>(FXCollections.observableArrayList());
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

    public void applyWearOnWeapons() { }

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

        return defense + baseDefense.get();
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
        member.setDefense(baseDefense.get());
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
        this.baseDefense.set(defense);
    }

    protected void setMaxLife(int life) {
        this.maxLife.set(life);
        this.life.set(life);
    }

    protected void setMaxMana(int mana) {
        this.maxMana.set(mana);
        this.mana.set(mana);
    }

    private void setInitiative(int init) {
        this.initiative.set(init);
    }

    protected void setLife(int life) {
        this.life.set(life);
    }

    public IntegerProperty lifeProperty() {
        return life;
    }

    public IntegerProperty maxLifeProperty() {
        return maxLife;
    }

    public IntegerProperty maxManaProperty() {
        return maxMana;
    }

    protected void setMana(int mana) {
        this.mana.set(Math.max(0, Math.min(mana, getMana())));
    }

    public IntegerProperty manaProperty() {
        return mana;
    }

    public IntegerProperty initiativeProperty() {
        return initiative;
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
        return baseDefense;
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