package testHelper;

import manager.DatabaseLoader;
import net.ucanaccess.jdbc.UcanaccessDriver;
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
    public static void setup() throws SQLException {
        Properties properties = new Properties();

        // TODO localize/configure
        properties.put("ConnSettings", "SET LOCALE TO de_DE");

        DriverManager.registerDriver(new UcanaccessDriver());

        try(Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + databasePath, properties)) {
            DatabaseLoader.loadDatabase(connection, true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
