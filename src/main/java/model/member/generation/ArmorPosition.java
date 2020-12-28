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

    public static ArmorPosition getArmorPosition(String name) {
        switch (name) {
            case "Kopf":
                return head;
            case "Oberkörper":
                return body;
            case "Arme":
                return arms;
            case "Beine":
                return legs;
        }
        throw new IllegalArgumentException();
    }
}
