package de.pnp.manager.component;

import de.pnp.manager.annotations.ExportToTypescript;
import de.pnp.manager.component.character.Talent;
import java.util.Collections;
import java.util.List;

@ExportToTypescript
public class Spell {

  private final String name;
  private final String effect;
  private final String type;
  private final String cost;
  private final String castTime;
  private final List<Talent> talents;
  private final int tier;

  public Spell(String name, String effect, String type, String cost, String castTime,
      List<Talent> talents, int tier) {
    this.name = name;
    this.effect = effect;
    this.type = type;
    this.cost = cost;
    this.castTime = castTime;
    this.talents = Collections.unmodifiableList(talents);
    this.tier = tier;
  }

  public String getName() {
    return name;
  }

  public String getEffect() {
    return effect;
  }

  public String getType() {
    return type;
  }

  public String getCost() {
    return cost;
  }

  public String getCastTime() {
    return castTime;
  }

  public List<Talent> getTalents() {
    return talents;
  }

  public int getTier() {
    return tier;
  }
}
