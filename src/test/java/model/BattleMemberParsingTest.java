package model;

import javafx.application.Application;
import javafx.stage.Stage;
import manager.DatabaseLoader;
import manager.WorkbookService;
import model.member.BattleMember;
import model.member.data.ArmorPiece;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BattleMemberParsingTest {

    static BattleMember member;

    static final String wbPath = "src/test/resources/workbooks/Gegnerbogen.xlsx";
    static final String databasePath = "src/test/resources/Welteninhalt.accdb";

    @BeforeAll
    public static void parse() throws IOException, SQLException {
        Workbook wb = WorkbookFactory.create(new File(wbPath));

        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + databasePath);
        DatabaseLoader.loadDatabase(connection, true);

        member = new BattleMember(wb);
    }

    @Test
    public void test() {
        System.out.println(member.getName());

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
