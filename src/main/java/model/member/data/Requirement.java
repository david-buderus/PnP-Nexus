package model.member.data;

public enum Requirement {
	none, little, normal, much, lot, extrem;

	@Override
	public String toString() {
		switch (this) {
		case lot:
			return "Sehr Hoch";
		case little:
			return "Niedrig";
		case much:
			return "Hoch";
		case none:
			return "Nichts";
		case normal:
			return "Durchschnittlich";
		case extrem:
			return "Extrem Hoch";
		default:
			return "";
		}
	}

	public int toInteger() {
		switch (this) {
		case extrem:
			return 5;
		case lot:
			return 4;
		case little:
			return 1;
		case much:
			return 3;
		case none:
			return 0;
		case normal:
			return 2;
		default:
			return 0;
		}
	}
}
