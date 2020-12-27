package model.member.generation;

public enum SecondaryAttribute {
    meleeDamage, rangeDamage, magicPower, defense, initiative, health, mentalHealth, mana, unknown;

    @Override
    public String toString() {
        switch (this) {
            case meleeDamage:
                return "Basisschaden Nahkampf";
            case rangeDamage:
                return "Basisschaden Fernkampf";
            case magicPower:
                return "Magiekraft";
            case defense:
                return "Abwehr";
            case initiative:
                return "Initiative";
            case health:
                return "Lebensenergie";
            case mentalHealth:
                return "Geistige Gesundheit";
            case mana:
                return "Mana";
        }
        return "Sonstiges";
    }

    public static SecondaryAttribute getSecondaryAttribute(String name) {
        switch (name) {
            case "Schaden Nahkampf":
                return meleeDamage;
            case "Schaden Fernkampf":
                return rangeDamage;
            case "Magiekraft":
                return magicPower;
            case "Abwehr":
                return defense;
            case "Initiative":
                return initiative;
            case "Leben":
                return health;
            case "Geistige Gesundheit":
                return mentalHealth;
            case "Mana":
                return mana;
        }
        return unknown;
    }
}
