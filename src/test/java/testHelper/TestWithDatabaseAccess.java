package testHelper;

import manager.DatabaseLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class TestWithDatabaseAccess {
    static final String databasePath = "src/test/resources/Welteninhalt.accdb";

    @BeforeAll
    @Test
    public static void setup() {
        Properties properties = new Properties();

        // TODO localize/configure
        properties.put("ConnSettings", "SET LOCALE TO de_DE");

        try(Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + databasePath, properties)) {
            DatabaseLoader.loadDatabase(connection, true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
