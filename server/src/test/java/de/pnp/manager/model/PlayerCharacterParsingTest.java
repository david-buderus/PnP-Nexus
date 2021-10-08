package de.pnp.manager.model;

import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.loot.ILootTable;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.PnPCharacterFactory;
import de.pnp.manager.model.character.data.ArmorPosition;
import de.pnp.manager.model.character.data.PrimaryAttribute;
import de.pnp.manager.model.character.data.SecondaryAttribute;
import de.pnp.manager.testHelper.TestWithDatabaseAccess;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerCharacterParsingTest extends TestWithDatabaseAccess {

    protected static PlayerCharacter character;
    protected static String wbPath = "src/test/resources/workbooks/Charakterbogen.xlsx";

    @BeforeAll
    public static void parse() throws IOException {
        Workbook wb = WorkbookFactory.create(new File(wbPath));
        character = PnPCharacterFactory.createPlayer("Test", null, wb);
        wb.close();
    }

    @Test
    public void test() {
        assertEquals("MyName", character.getName());
        assertEquals(42, character.getLevel());
        assertEquals(9, character.getTier());
        assertEquals("MyGender", character.getGender());
        assertEquals("MyRace", character.getRace());

        assertEquals(1, (int) character.getPrimaryAttributes().get(PrimaryAttribute.strength));
        assertEquals(2, (int) character.getPrimaryAttributes().get(PrimaryAttribute.endurance));
        assertEquals(3, (int) character.getPrimaryAttributes().get(PrimaryAttribute.dexterity));
        assertEquals(4, (int) character.getPrimaryAttributes().get(PrimaryAttribute.intelligence));
        assertEquals(5, (int) character.getPrimaryAttributes().get(PrimaryAttribute.charisma));
        assertEquals(6, (int) character.getPrimaryAttributes().get(PrimaryAttribute.resilience));
        assertEquals(7, (int) character.getPrimaryAttributes().get(PrimaryAttribute.agility));
        assertEquals(8, (int) character.getPrimaryAttributes().get(PrimaryAttribute.precision));

        assertEquals(9, (int) character.getSecondaryAttributes().get(SecondaryAttribute.meleeDamage));
        assertEquals(10, (int) character.getSecondaryAttributes().get(SecondaryAttribute.rangeDamage));
        assertEquals(11, (int) character.getSecondaryAttributes().get(SecondaryAttribute.magicPower));
        assertEquals(12, (int) character.getSecondaryAttributes().get(SecondaryAttribute.defense));
        assertEquals(13, (int) character.getSecondaryAttributes().get(SecondaryAttribute.initiative));
        assertEquals(11, character.getStaticInitiative());
        assertEquals(14, (int) character.getSecondaryAttributes().get(SecondaryAttribute.health));
        assertEquals(14, character.getMaxHealth());
        assertEquals(15, (int) character.getSecondaryAttributes().get(SecondaryAttribute.mentalHealth));
        assertEquals(16, (int) character.getSecondaryAttributes().get(SecondaryAttribute.mana));
        assertEquals(16, character.getMaxMana());

        assertEquals(5, character.getProtection(ArmorPosition.ARM));
        assertEquals(6, character.getProtection(ArmorPosition.LEGS));
        assertEquals(7, character.getProtection(ArmorPosition.UPPER_BODY));
        assertEquals(6, character.getProtection(ArmorPosition.LEGS));

        assertEquals(16, character.getShieldProtection());
    }

    @Test
    public void testTalents() {
         int talentSum = Database.talentList.stream().mapToInt(talent -> character.getTalents().get(talent)).sum();

         // instinct is counted twice here but only once on the excel sheet
         int skillInstinct = character.getTalents().get(Database.getTalent(LanguageUtility.getMessage("talent.instinctNormal")));

         assertEquals(6328, talentSum - skillInstinct);
    }

    @Test
    public void testSpells() {
        assertEquals(5, character.getSpells().size());

        String[] spells = {"Arkane Geschosse", "Blenden", "Flux", "Frostnova", "Rufe Ent"};

        Arrays.stream(spells).forEach(spell -> assertTrue(character.getSpells().stream().anyMatch(s -> s.getName().equals(spell))));
    }

    @Test
    public void testInventory() {
        ILootTable lootTable = character.getFinishedLootTable();

        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Bogen")));
        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Stahlhelm")));
        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Diebische Amulett")));
        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Stahlkeule")));
        assertTrue(lootTable.getLoot().stream().anyMatch(loot -> loot.getName().equals("Gold")));
    }
}
