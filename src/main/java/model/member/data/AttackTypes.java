package model.member.data;

public enum AttackTypes{
    head, upperBody, legs, arm, ignoreArmor, direct;

    @Override
    public String toString() {
        switch (this) {
            case arm:
                return "Arme";
            case head:
                return "Kopf";
            case ignoreArmor:
                return "Ignoriere Rüstung";
            case legs:
                return "Beine";
            case upperBody:
                return "Oberk\u00f6rper";
            case direct:
                return "Direkt";
            default:
                return "";
        }
    }
}
