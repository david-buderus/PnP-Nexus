package de.pnp.manager.model;

import de.pnp.manager.model.member.BattleMember;
import de.pnp.manager.model.member.data.ArmorPiece;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import de.pnp.manager.testHelper.TestWithDatabaseAccess;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleMemberParsingTest extends TestWithDatabaseAccess {

    protected static BattleMember member;
    protected static String wbPath = "src/test/resources/workbooks/Gegnerbogen.xlsx";

    @BeforeAll
    public static void parse() throws IOException {
        Workbook wb = WorkbookFactory.create(new File(wbPath));
        member = new BattleMember(wb);
    }

    @Test
    public void test() {
        assertEquals("MyName", member.getName());
        assertEquals(42, member.getLevel());
        assertEquals(9, member.getTier());
        assertEquals(14, member.getMaxLife());
        assertEquals(15, member.getMaxMana());
        assertEquals(13, member.getInitiative());

        assertEquals(3, member.getArmor(ArmorPiece.arm));
        assertEquals(4, member.getArmor(ArmorPiece.legs));
        assertEquals(5, member.getArmor(ArmorPiece.upperBody));
        assertEquals(4, member.getArmor(ArmorPiece.legs));

        assertEquals(8, member.getArmor(ArmorPiece.shield));
    }
}
