package model.member;

import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.member.data.AttackTypes;
import model.member.data.MemberStateEffect;

public class MemberState {

	private static Random RAND = new Random();

	private String name;
	private int maxDuration;
	private IntegerProperty duration;
	private StringProperty durationDisplay;
	private boolean active;
	private double power;
	private boolean random;
	private AttackTypes type;

	private BattleMember source;
	private MemberStateEffect effect;

	public MemberState(String name, MemberStateEffect effect, int duration, boolean active, double power,
					   boolean random, AttackTypes type, BattleMember source) {
		this.type = type;
		this.duration = new SimpleIntegerProperty(0);
		this.durationDisplay = new SimpleStringProperty("");
		this.setName(name);
		this.setEffect(effect);
		this.setDuration(duration);
		this.maxDuration = duration;
		this.active = active;
		this.setPower(power);
		this.random = random;
		this.source = source;
		this.setDurationDisplay(getDuration() + (this.active ? " aktive Runden" : " Runden"));

		this.duration.addListener((ob, o, n) -> setDurationDisplay(getDuration() + (this.active ? " aktive Runden" : " Runden")));
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

	public BattleMember getSource(){
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

	public void setPower(double power) {
		this.power = power;
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

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActiveRounder() {
		return this.active;
	}

	public String getDurationDisplay() {
		return durationDisplay.get();
	}

	public void setDurationDisplay(String s) {
		this.durationDisplay.set(s);
	}

	public StringProperty durationDisplayProperty() {
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
