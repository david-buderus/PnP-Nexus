package model;

import model.member.BattleMember;
import model.member.data.ArmorPiece;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testHelper.TestWithDatabaseAccess;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleMemberParsingTest extends TestWithDatabaseAccess {

    static BattleMember member;
    static final String wbPath = "src/test/resources/workbooks/Gegnerbogen.xlsx";

    @BeforeAll
    public static void parse() throws IOException, SQLException {
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
