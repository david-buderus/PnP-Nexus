package model.member;

import javafx.beans.property.*;
import manager.LanguageUtility;
import model.member.data.AttackTypes;
import model.member.data.MemberStateEffect;

import java.util.Random;

public class MemberState {

    private static final Random RAND = new Random();

    private final String name;
    private final int maxDuration;
    private final IntegerProperty duration;
    private final StringProperty durationDisplay;
    private final boolean active;
    private final double power;
    private final boolean random;
    private final AttackTypes type;

    private final BattleMember source;
    private MemberStateEffect effect;

    public MemberState(String name, MemberStateEffect effect, int duration, boolean active, double power,
                       boolean random, AttackTypes type, BattleMember source) {
        this.type = type;
        this.duration = new SimpleIntegerProperty(duration);
        this.durationDisplay = new SimpleStringProperty("");
        this.name = name;
        this.effect = effect;
        this.maxDuration = duration;
        this.active = active;
        this.power = power;
        this.random = random;
        this.source = source;
        this.durationDisplay.bind(this.duration.asString().concat(" ").concat(this.active ?
                LanguageUtility.getMessageProperty("state.info.activeRounds") :
                LanguageUtility.getMessageProperty("state.info.rounds")));
    }

    public void decreaseDuration() {
        decreaseDuration(1);
    }

    public void decreaseDuration(int i) {
        this.setDuration(this.getDuration() - i);
    }

    public int getDuration() {
        return duration.get();
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public IntegerProperty durationProperty() {
        return duration;
    }

    public boolean isRandom() {
        return random;
    }

    public BattleMember getSource() {
        return source;
    }

    public String getPowerAsString() {
        if (Math.rint(getPower()) == getPower()) {
            return (random ? "D" : "") + (int) power;
        } else {
            return (random ? "D" : "") + power;
        }
    }

    public double getPower() {
        return power;
    }

    public double getEffectPower() {
        if (Math.rint(this.power) == this.power) {
            int power = (int) this.power;

            return power == 0 ? 0 : random ? RAND.nextInt(power) + 1 : power;

        } else {
            return random ? 1 + RAND.nextDouble() * (power - 1) : power;
        }
    }

    public MemberStateEffect getEffect() {
        return effect;
    }

    public void setEffect(MemberStateEffect effect) {
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public boolean isActiveRounder() {
        return this.active;
    }

    public ReadOnlyStringProperty durationDisplayProperty() {
        return durationDisplay;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MemberState)) {
            return false;
        }
        MemberState other = (MemberState) o;

        return getName().equals(other.getName()) && getEffect() == other.getEffect()
                && getDuration() == other.getDuration() && isActiveRounder() == other.isActiveRounder()
                && !(getPower() != other.getPower()) && isRandom() == other.isRandom();
    }

    public AttackTypes getType() {
        return type;
    }

    public int getMaxDuration() {
        return maxDuration;
    }
}
