package manager;

import javafx.beans.property.*;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.*;

public abstract class LanguageUtility {

    public static final ObjectProperty<Language> language = new SimpleObjectProperty<>(Language.german);
    private static final ObjectProperty<ResourceBundle> messages = new SimpleObjectProperty<>();
    private static final Map<String, StringProperty> messageBindings = new HashMap<>();

    static {
        reloadLanguage(language.get());
        language.addListener((ob, o, n) -> {
            reloadLanguage(n);
            for (String key : messageBindings.keySet()) {
                messageBindings.get(key).set(getMessage(key));
            }
        });
    }

    public static String getMessage(String key) {
        try {
            return messages.get().getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static ReadOnlyStringProperty getMessageProperty(String key) {
        if (messageBindings.containsKey(key)) {
            return messageBindings.get(key);
        } else {
            StringProperty property = new SimpleStringProperty();
            property.set(getMessage(key));
            messageBindings.put(key, property);
            return property;
        }
    }

    private static void reloadLanguage(Language language) {

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties().setFileName("Configuration.properties"));

        try {
            Utility.config.set(builder.getConfiguration());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        try {
            messages.set(ResourceBundle.getBundle("language/Messages", language.getLocale()));
        } catch (MissingResourceException e) {
            e.printStackTrace();
            messages.set(ResourceBundle.getBundle("language/Messages", Locale.ENGLISH));
        }
    }
}
