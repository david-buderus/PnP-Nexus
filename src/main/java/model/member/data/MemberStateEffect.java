package model.member.data;

public enum MemberStateEffect {
	damage, heal, manaDrain, manaRegeneration, slow, relativeSlow, speed, relativeSpeed, snare, stun, fear,
	armorPlus, armorMinus, other;
	
	@Override
	public String toString() {
		switch(this) {
			case damage: return "Schaden";
			case heal: return "Heilung";
			case manaRegeneration: return "Manaregeneration";
			case manaDrain: return "Manaentzug";
			case slow: return "Verlangsamung";
			case relativeSlow: return "Relative Verlangsamung";
			case speed: return "Beschleunigung";
			case relativeSpeed: return "Relative Beschleunigung";
			case snare: return "Festhalten";
			case stun: return "Betäuben";
			case fear: return "Veränstigen";
			case armorPlus: return "Verstärkte Abwehr";
			case armorMinus: return "Geschwächte Abwehr";
		}
		return "Sonstiges";
	}

	public int getImageID(){
		switch (this){
			case damage:
				return 1;
			case heal:
				return 2;
			case manaDrain:
				return 3;
			case manaRegeneration:
				return 4;
			case slow:
			case relativeSlow:
				return 5;
			case speed:
			case relativeSpeed:
				return 6;
			case snare:
				return 7;
			case stun:
				return 8;
			case fear:
				return 9;
			case armorPlus:
				return 10;
			case armorMinus:
				return 11;
		}
		return 0;
	}

	public boolean isAbsoluteInitiativeEffect(){
		switch (this){
			case slow:
			case speed:
				return true;
		}
		return false;
	}

	public boolean isRelativeInitiativeEffect(){
		switch (this){
			case relativeSlow:
			case relativeSpeed:
				return true;
		}
		return false;
	}
}
