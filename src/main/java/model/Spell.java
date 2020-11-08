package model;

import model.member.generation.Talent;

public class Spell {
	
	private String name;
	private String effect;
	private String typ;
	private String cost;
	private String castTime;
	private int tier;

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getCastTime() {
		return castTime;
	}

	public void setCastTime(String castTime) {
		this.castTime = castTime;
	}

	public Talent getTalent(){

		switch (getTyp()){
			case "Arkan":
				return Talent.arkan;
			case "Illusion":
				return Talent.illusion;
			case "Licht":
				return Talent.light;
			case "Finsternis":
				return Talent.darkness;
			case "Feuer":
				return Talent.fire;
			case "Wasser":
				return Talent.water;
			case "Luft":
				return Talent.air;
			case "Erde":
				return Talent.earth;
			case "Sturm":
				return Talent.storm;
			case "Frost":
				return Talent.ice;
			case "Natur":
				return Talent.nature;
			case "Tot":
				return Talent.death;
		}

		return Talent.knowledge;
	}
}
