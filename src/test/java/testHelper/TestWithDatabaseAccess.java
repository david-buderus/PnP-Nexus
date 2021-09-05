package testHelper;

import manager.DatabaseLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class TestWithDatabaseAccess {
    static final String databasePath = "src/test/resources/Welteninhalt.accdb";

    @BeforeAll
    @Test
    public static void setup() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + databasePath);
            DatabaseLoader.loadDatabase(connection, true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
