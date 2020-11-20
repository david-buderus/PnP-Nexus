package model.member.generation;

public enum SecondaryAttribute {
    meleeDamage, rangeDamage, magicPower, defense, initiative, health, mentalHealth, mana;

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
}
