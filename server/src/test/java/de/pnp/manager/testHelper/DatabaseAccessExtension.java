package de.pnp.manager.testHelper;

import de.pnp.manager.main.DatabaseLoader;
import de.pnp.manager.main.Language;
import de.pnp.manager.main.LanguageUtility;
import net.ucanaccess.jdbc.UcanaccessDriver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

public class DatabaseAccessExtension implements BeforeAllCallback {

    static final String databasePath = "src/test/resources/Welteninhalt.accdb";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        Properties properties = new Properties();

        // TODO localize/configure
        properties.put("ConnSettings", "SET LOCALE TO de_DE");

        Locale.setDefault(Locale.GERMAN);
        LanguageUtility.language.set(Language.german);

        DriverManager.registerDriver(new UcanaccessDriver());

        try(Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + databasePath, properties)) {
            DatabaseLoader.loadDatabase(connection, true);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
