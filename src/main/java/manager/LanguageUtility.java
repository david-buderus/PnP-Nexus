package manager;

import javafx.beans.property.*;

import java.util.*;

public abstract class LanguageUtility {

    public static final ObjectProperty<Language> language = new SimpleObjectProperty<>(Language.system);
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

    public static boolean hasMessage(String key) {
        return messages.get().containsKey(key);
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
        try {
            messages.set(ResourceBundle.getBundle("language/Messages", language.getLocale()));
        } catch (MissingResourceException e) {
            e.printStackTrace();
            messages.set(ResourceBundle.getBundle("language/Messages", Locale.ENGLISH));
        }
    }
}
