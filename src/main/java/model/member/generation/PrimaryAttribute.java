package model.member.generation;

import java.util.NoSuchElementException;

public enum PrimaryAttribute {
    strength, endurance, dexterity, intelligence, charisma, resilience, maneuverability, precision;

    @Override
    public String toString() {
        switch (this) {
            case strength:
                return "Körperkraft";
            case endurance:
                return "Ausdauer";
            case dexterity:
                return "Geschicklichkeit";
            case intelligence:
                return "Intelligenz";
            case charisma:
                return "Charisma";
            case resilience:
                return "Belastbarkeit";
            case maneuverability:
                return "Beweglichkeit";
            case precision:
                return "Genauigkeit";
        }
        return "Sonstiges";
    }

    public String toShortString() {
        switch (this) {
            case strength:
                return "KK";
            case endurance:
                return "AU";
            case dexterity:
                return "GE";
            case intelligence:
                return "IN";
            case charisma:
                return "CH";
            case resilience:
                return "BL";
            case maneuverability:
                return "BW";
            case precision:
                return "GN";
        }
        return "--";
    }

    public static PrimaryAttribute getPrimaryAttribute(String name) {
        switch (name) {
            case "Körperkraft":
                return strength;
            case "Ausdauer":
                return endurance;
            case "Geschicklichkeit":
                return dexterity;
            case "Intelligenz":
                return intelligence;
            case "Charisma":
                return charisma;
            case "Belastbarkeit":
                return resilience;
            case "Beweglichkeit":
                return maneuverability;
            case "Genauigkeit":
                return precision;
        }
        throw new NoSuchElementException("The PrimaryAttribute with the name " + name + " does not exists.");
    }
}
