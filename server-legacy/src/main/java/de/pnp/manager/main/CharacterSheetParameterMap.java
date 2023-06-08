package de.pnp.manager.main;

import java.util.HashMap;
import java.util.Objects;

public class CharacterSheetParameterMap extends HashMap<String, Object> {

    public int getValueAsInteger(String key) {
        try {
            return ((Number) get(key)).intValue();
        } catch (ClassCastException e) {
            if (get(key) instanceof String) {
                return Integer.parseInt(key);
            }
            return 0;
        }
    }

    public int getValueAsIntegerOrElse(String key, int fallback) {
        return containsKey(key) ? getValueAsInteger(key) : fallback;
    }

    public String getValueAsString(String key) {
        return Objects.requireNonNullElse(get(key), "").toString();
    }

    public String getValueAsStringOrElse(String key, String fallback) {
        if (!containsKey(key))
            return fallback;

        if (getValueAsString(key).equals("0") || getValueAsString(key).isBlank())
            return fallback;

        return getValueAsString(key);
    }
}
