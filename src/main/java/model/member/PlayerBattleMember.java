package model.member;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import manager.*;
import model.Currency;
import model.Spell;
import model.interfaces.ILootable;
import model.item.Item;
import model.item.Jewellery;
import model.loot.LootTable;
import model.member.generation.SecondaryAttribute;
import model.member.generation.Talent;
import model.member.interfaces.IExtendedBattleMember;
import model.member.interfaces.IPlayerBattleMember;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

public class PlayerBattleMember extends ExtendedBattleMember implements IPlayerBattleMember, ILootable {

    private final StringProperty occupation = new SimpleStringProperty("");
    private final IntegerProperty age = new SimpleIntegerProperty(0);
    private final StringProperty history = new SimpleStringProperty("");

    private final ObservableList<Item> inventory = FXCollections.observableArrayList();

    public PlayerBattleMember(Workbook wb) {
        this(CharacterSheetParser.parseCharacterSheet(wb));
    }

    public PlayerBattleMember(CharacterSheetParameterMap parameterMap) {
        super(parameterMap);

        // load occupation
        occupation.set(parameterMap.getValueAsStringOrElse("character.occupation", ""));

        // load age
        age.set(parameterMap.getValueAsIntegerOrElse("character.age", 0));

        // load inventory
        for (int i = 1; i <= 21; i++) {
            String nameX = parameterMap.getValueAsStringOrElse("character.inventory." + i, "");

            if (nameX.isBlank()) continue;

            Item itemX = Database.getItemOrElse(nameX, null);
            // TODO if null create DB entry
            if (itemX != null) {
                inventory.add(itemX);
            }
        }

        // load history
        history.set(parameterMap.getValueAsStringOrElse("character.history", ""));

        // load rings
        int amountOfRings = Utility.getConfig().getInt("character.jewellery.amount.ring");
        for (int i = 1; i <= amountOfRings; i++) {
            String ringx = parameterMap.getValueAsStringOrElse("character.armor.ring." + i, "");
            Item ringItemx = Database.getItemOrElse(ringx, null);
            // TODO if null create DB entry
            if (ringItemx != null) {
                jewellery.add((Jewellery) ringItemx);
            }
        }
        // load amulet
        String amulet = parameterMap.getValueAsStringOrElse("character.armor.amulet", "");
        Item amuletItem = Database.getItemOrElse(amulet, null);
        if (amuletItem != null) {
            System.out.println(amuletItem);
            jewellery.add((Jewellery) amuletItem);
        }

        // load bracelets
        String bracelet1 = parameterMap.getValueAsStringOrElse("character.armor.bracelet.1", "");
        String bracelet2 = parameterMap.getValueAsStringOrElse("character.armor.bracelet.2", "");
        Item bracelet1Item = Database.getItemOrElse(bracelet1, null);
        Item bracelet2Item = Database.getItemOrElse(bracelet2, null);
        if (bracelet1Item != null) {
            jewellery.add((Jewellery) bracelet1Item);
        }
        if (bracelet2Item != null) {
            jewellery.add((Jewellery) bracelet2Item);
        }

        // load misc
        for (int i = 1; i <= 4; i++) {
            String miscX = parameterMap.getValueAsStringOrElse("character.armor.misc." + i, "");
            Item miscItemX = Database.getItemOrElse(miscX, null);
            // TODO if null create DB entry
            if (miscItemX != null) {
                jewellery.add((Jewellery) miscItemX);
            }
        }

        // load currency
        int cp = parameterMap.getValueAsIntegerOrElse("character.money.copper", 0);
        int sp = parameterMap.getValueAsIntegerOrElse("character.money.silver", 0);
        int gp = parameterMap.getValueAsIntegerOrElse("character.money.gold", 0);

        Currency currency = new Currency(cp, sp, gp);
        this.lootTable.add(currency);

        // load advantages/disadvantages
        addDescription("character.advantage", Collections.singleton(parameterMap.getValueAsStringOrElse("character.advantages", "")));
        addDescription("character.disadvantage", Collections.singleton(parameterMap.getValueAsStringOrElse("character.disadvantages", "")));

        inventory.stream().filter(Objects::nonNull).forEach(item -> lootTable.add(item, 1, 1));
    }

    @Override
    public boolean dropsWeapons() {
        return true;
    }

    @Override
    public boolean dropsArmor() {
        return true;
    }

    @Override
    public boolean dropsJewellery() {
        return true;
    }

    @Override
    public StringProperty occupationProperty() {
        return occupation;
    }

    @Override
    public IntegerProperty ageProperty() {
        return age;
    }

    @Override
    public StringProperty historyProperty() {
        return history;
    }
}
