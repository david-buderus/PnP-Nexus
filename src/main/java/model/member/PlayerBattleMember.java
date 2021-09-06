package model.member;

import javafx.beans.property.SimpleIntegerProperty;
import manager.CharacterSheetParameterMap;
import manager.CharacterSheetParser;
import manager.Database;
import manager.LanguageUtility;
import model.Currency;
import model.item.Item;
import model.item.Jewellery;
import model.member.generation.SecondaryAttribute;
import model.member.generation.Talent;
import model.member.interfaces.IExtendedBattleMember;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collections;
import java.util.stream.Stream;

public class PlayerBattleMember extends ExtendedBattleMember implements IExtendedBattleMember {

    public PlayerBattleMember(Workbook wb) {
        this(CharacterSheetParser.parseCharacterSheet(wb));
    }

    public PlayerBattleMember(CharacterSheetParameterMap parameterMap) {
        super(parameterMap);
        // load occupation
        // load age

        // load inventory
        // load history

        // load rings
        for (int i = 1; i <= 8; i++) {
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


        // load mental health
        this.secondaryAttributes.put(SecondaryAttribute.mentalHealth, new SimpleIntegerProperty(parameterMap.getValueAsIntegerOrElse("secondaryAttribute.mentalHealth", 0)));

        // load advantages/disadvantages
        addDescription("character.advantage", Collections.singleton(parameterMap.getValueAsStringOrElse("character.advantages", "")));
        addDescription("character.disadvantage", Collections.singleton(parameterMap.getValueAsStringOrElse("character.disadvantages", "")));

        // load talents
        Stream<String> talentKeys = parameterMap.keySet().stream().filter(key -> key.startsWith("talent."));

        talentKeys.forEach(key -> {
            // TODO imrpovised weapon needs primary attributes

            String talentName = LanguageUtility.getMessageProperty(key).get();

            if (talentName.equals(key)) {
                System.out.println(talentName);
            }

            Talent talent = Database.getTalent(talentName);

            this.talents.put(talent, new SimpleIntegerProperty(parameterMap.getValueAsInteger(key)));
        });

        // TODO
        // load spells

    }

    // todo set loot dirferent
}
