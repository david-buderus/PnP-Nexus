package de.pnp.manager.component;

import org.springframework.data.annotation.Id;

public class Universe {

  @Id
  private final String name;

  private final String displayName;

  private StatComputationRules statComputationRules;

  public Universe(String name, String displayName) {
    this.name = name;
    this.displayName = displayName;
  }

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return displayName;
  }
}
