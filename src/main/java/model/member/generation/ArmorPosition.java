package model.member.generation;

public enum ArmorPosition {
    head, body, arms, legs;


    @Override
    public String toString() {
        switch (this) {
            case head:
                return "Kopf";
            case body:
                return "Oberkörper";
            case arms:
                return "Arme";
            case legs:
                return "Beine";
        }
        return super.toString();
    }
}
