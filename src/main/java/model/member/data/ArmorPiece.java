package model.member.data;

public enum ArmorPiece {
	head, upperBody, legs, arm, shield;

	@Override
	public String toString() {
		switch (this) {
		case arm:
			return "Arme";
		case head:
			return "Kopf";
		case legs:
			return "Beine";
		case upperBody:
			return "Oberk\u00f6rper";
		case shield:
			return "Schild";
		}
		return "Nichts";
	}

	public static ArmorPiece getArmorPiece(String piece){
		switch (piece){
			case "Kopf":
				return head;
			case "Oberkörper":
				return upperBody;
			case "Arme":
				return arm;
			case "Beine":
				return legs;
			case "Schild":
			case "Großschild":
				return shield;
		}
		return null;
	}
}
