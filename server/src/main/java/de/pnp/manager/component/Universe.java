package de.pnp.manager.component;

import org.springframework.data.annotation.Id;

/**
 * The description of a universe.
 */
public class Universe {

  /**
   * The unique name of this universe.
   * <p>
   * This will never change.
   */
  @Id
  private final String name;

  /**
   * The human-readable name of this universe.
   */
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
