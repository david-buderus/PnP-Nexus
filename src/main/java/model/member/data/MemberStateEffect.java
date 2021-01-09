package model.member.data;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;

public enum MemberStateEffect implements WithToStringProperty {
    damage(1), heal(2), manaDrain(3), manaRegeneration(4),
    slow(5), relativeSlow(5), speed(6), relativeSpeed(6),
    snare(7), stun(8), fear(9),
    armorPlus(10), armorMinus(11), other(0);

    private final int imageId;

    MemberStateEffect(int imageId) {
        this.imageId = imageId;
    }

    public int getImageID() {
        return imageId;
    }

    public boolean isAbsoluteInitiativeEffect() {
        switch (this) {
            case slow:
            case speed:
                return true;
        }
        return false;
    }

    public boolean isRelativeInitiativeEffect() {
        switch (this) {
            case relativeSlow:
            case relativeSpeed:
                return true;
        }
        return false;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect." + super.toString());
    }
}
