package model.member;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import manager.CharacterSheetParameterMap;
import manager.LanguageUtility;
import model.Battle;
import model.Spell;
import model.item.Armor;
import model.item.Jewellery;
import model.item.Weapon;
import model.loot.LootTable;
import model.member.data.ArmorPiece;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.Talent;
import model.member.interfaces.IExtendedBattleMember;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExtendedBattleMember extends BattleMember implements IExtendedBattleMember {

    protected final Map<PrimaryAttribute, IntegerProperty> primaryAttributes = new HashMap<>();
    protected final Map<SecondaryAttribute, IntegerProperty> secondaryAttributeModifier = new HashMap<>();

    protected final HashMap<Talent, IntegerProperty> talents = new HashMap<>();;
    protected final ObservableList<Weapon> weapons = FXCollections.observableArrayList();;
    protected final ObservableList<Armor> armors = FXCollections.observableArrayList();
    protected final ObservableList<Jewellery> jewellery = FXCollections.observableArrayList();
    protected final ObservableList<Spell> spells = FXCollections.observableArrayList();

    protected final StringProperty notes = new SimpleStringProperty("");

    public ExtendedBattleMember(Battle battle) {
        super(battle);
    }

    public ExtendedBattleMember(Battle battle, LootTable lootTable, HashMap<ArmorPiece, IntegerProperty> armor) {
        super(battle, lootTable, armor);
    }

    public ExtendedBattleMember(Battle battle, LootTable lootTable) {
        super(battle, lootTable);
    }

    public ExtendedBattleMember(CharacterSheetParameterMap parameterMap) {
        super(parameterMap);
    }

    public ExtendedBattleMember(Workbook wb) {
        super(wb);
    }

    @Override
    public IntegerProperty getTalent(Talent talent) {
        System.out.println("->");
        System.out.println(talents);
        return talents.get(talent);
    }

    @Override
    public Collection<Talent> getTalents() {
        return talents.keySet();
    }

    @Override
    public IntegerProperty getAttribute(PrimaryAttribute attribute) {
        return primaryAttributes.get(attribute);
    }

    @Override
    public ReadOnlyIntegerProperty getAttribute(SecondaryAttribute attribute) {
        return secondaryAttributes.get(attribute);
    }

    @Override
    public IntegerProperty getModifier(SecondaryAttribute attribute) {
        return secondaryAttributeModifier.get(attribute);
    }

    @Override
    public ObservableList<Weapon> getWeapons() {
        return weapons;
    }

    @Override
    public ObservableList<Armor> getArmor() {
        return armors;
    }

    @Override
    public ObservableList<Jewellery> getJewelleries() {
        return jewellery;
    }

    @Override
    public ObservableList<Spell> getSpells() {
        return spells;
    }

    @Override
    public IntegerProperty strengthProperty() {
        return primaryAttributes.get(PrimaryAttribute.strength);
    }

    @Override
    public IntegerProperty enduranceProperty() {
        return primaryAttributes.get(PrimaryAttribute.endurance);
    }

    @Override
    public IntegerProperty dexterityProperty() {
        return primaryAttributes.get(PrimaryAttribute.dexterity);
    }

    @Override
    public IntegerProperty intelligenceProperty() {
        return primaryAttributes.get(PrimaryAttribute.intelligence);
    }

    @Override
    public IntegerProperty charismaProperty() {
        return primaryAttributes.get(PrimaryAttribute.charisma);
    }

    @Override
    public IntegerProperty resilienceProperty() {
        return primaryAttributes.get(PrimaryAttribute.resilience);
    }

    @Override
    public IntegerProperty agilityProperty() {
        return primaryAttributes.get(PrimaryAttribute.agility);
    }

    @Override
    public IntegerProperty precisionProperty() {
        return primaryAttributes.get(PrimaryAttribute.precision);
    }

    @Override
    public StringProperty notesProperty() {
        return notes;
    }

    protected void addDescription(String headerKey, Collection<String> lines) {
        if (!lines.isEmpty()) {
            notes.set(notes.get() + LanguageUtility.getMessage(headerKey) + "\n");
            for (String line : lines) {
                String[] parts = line.split("\\r?\\n");
                for (int i = 0; i < parts.length; i++) {
                    if (i == 0) {
                        notes.set(notes.get() + "  - " + parts[i] + "\n");
                    } else {
                        notes.set(notes.get() + "    " + parts[i] + "\n");
                    }
                }

            }
        }
    }
}
