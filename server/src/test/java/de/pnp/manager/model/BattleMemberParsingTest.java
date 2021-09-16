package de.pnp.manager.model;

import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.PnPCharacterFactory;
import de.pnp.manager.model.character.data.ArmorPosition;
import de.pnp.manager.testHelper.TestWithDatabaseAccess;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleMemberParsingTest extends TestWithDatabaseAccess {

    protected static PnPCharacter character;
    protected static String wbPath = "src/test/resources/workbooks/Gegnerbogen.xlsx";

    @BeforeAll
    public static void parse() throws IOException {
        Workbook wb = WorkbookFactory.create(new File(wbPath));
        character = PnPCharacterFactory.createCharacter("ID", null, wb);
    }

    @Test
    public void test() {
        assertEquals("MyName", character.getName());
        assertEquals(42, character.getLevel());
        assertEquals(9, character.getTier());
        assertEquals(14, character.getMaxHealth());
        assertEquals(15, character.getMaxMana());
        assertEquals(13, character.getInitiative());

        assertEquals(3, character.getProtection(ArmorPosition.arm));
        assertEquals(4, character.getProtection(ArmorPosition.legs));
        assertEquals(5, character.getProtection(ArmorPosition.upperBody));
        assertEquals(4, character.getProtection(ArmorPosition.legs));

        assertEquals(8, character.getShieldProtection());
    }
}
