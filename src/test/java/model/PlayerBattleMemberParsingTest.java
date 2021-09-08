package model;

import javafx.beans.property.SimpleIntegerProperty;
import manager.Database;
import manager.LanguageUtility;
import model.loot.LootTable;
import model.member.GeneratedExtendedBattleMember;
import model.member.PlayerBattleMember;
import model.member.data.ArmorPiece;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.interfaces.IExtendedBattleMember;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testHelper.TestWithDatabaseAccess;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerBattleMemberParsingTest extends TestWithDatabaseAccess {

    protected static IExtendedBattleMember member;
    protected static String wbPath = "src/test/resources/workbooks/Charakterbogen.xlsx";

    @BeforeAll
    public static void parse() throws IOException {
        Workbook wb = WorkbookFactory.create(new File(wbPath));
        member = new PlayerBattleMember(wb);
    }

    @Test
    public void test() {
        assertEquals("MyName", member.getName());
        assertEquals(42, member.getLevel());
        assertEquals(9, member.getTier());

        assertEquals(1, member.getAttribute(PrimaryAttribute.strength).get());
        assertEquals(2, member.getAttribute(PrimaryAttribute.endurance).get());
        assertEquals(3, member.getAttribute(PrimaryAttribute.dexterity).get());
        assertEquals(4, member.getAttribute(PrimaryAttribute.intelligence).get());
        assertEquals(5, member.getAttribute(PrimaryAttribute.charisma).get());
        assertEquals(6, member.getAttribute(PrimaryAttribute.resilience).get());
        assertEquals(7, member.getAttribute(PrimaryAttribute.agility).get());
        assertEquals(8, member.getAttribute(PrimaryAttribute.precision).get());

        assertEquals(9, member.getAttribute(SecondaryAttribute.meleeDamage).get());
        assertEquals(10, member.getAttribute(SecondaryAttribute.rangeDamage).get());
        assertEquals(11, member.getAttribute(SecondaryAttribute.magicPower).get());
        assertEquals(12, member.getAttribute(SecondaryAttribute.defense).get());
        assertEquals(13, member.getAttribute(SecondaryAttribute.initiative).get());
        assertEquals(13, member.getInitiative());
        assertEquals(14, member.getAttribute(SecondaryAttribute.health).get());
        assertEquals(14, member.getMaxLife());
        assertEquals(15, member.getAttribute(SecondaryAttribute.mentalHealth).get());
        assertEquals(16, member.getAttribute(SecondaryAttribute.mana).get());
        assertEquals(16, member.getMaxMana());

        assertEquals(5, member.getArmor(ArmorPiece.arm));
        assertEquals(6, member.getArmor(ArmorPiece.legs));
        assertEquals(7, member.getArmor(ArmorPiece.upperBody));
        assertEquals(6, member.getArmor(ArmorPiece.legs));

        assertEquals(16, member.getArmor(ArmorPiece.shield));
    }

    @Test
    public void testTalents() {
         int talentSum = Database.talentList.stream().map(talent -> Objects.requireNonNullElse(member.getTalent(talent), new SimpleIntegerProperty(0)).get()).reduce(Integer::sum).get();

         // instinct is counted twice here but only once on the excel sheet
         int skillInstinct = member.getTalent(Database.getTalent(LanguageUtility.getMessage("talent.instinctNormal"))).get();

         assertEquals(6328, talentSum - skillInstinct);
    }

    @Test
    public void testSpells() {
        assertEquals(5, member.getSpells().size());

        String[] spells = {"Arkane Geschosse", "Blenden", "Flux", "Frostnova", "Rufe Ent"};

        Arrays.stream(spells).forEach(spell -> assertTrue(member.getSpells().stream().anyMatch(s -> s.getName().equals(spell))));
    }

    @Test
    public void testInventory() {
        LootTable lootTable = member.getLootTable();

        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Bogen")));
        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Stahlhelm")));
        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Diebische Amulett")));
        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Stahlkeule")));
        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Gold")));
    }
}
