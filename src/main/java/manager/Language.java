package manager;

import java.util.Locale;

public enum Language {
    german("Deutsch"), english("English");

    private final String name;

    Language(String name) {
        this.name = name;
    }

    public Locale getLocale() {
        switch (this) {
            case german:
                return Locale.GERMAN;
            case english:
                return Locale.ENGLISH;
        }
        return Locale.ENGLISH;
    }

    @Override
    public String toString() {
        return name;
    }
}
