package de.pnp.manager.server.exception;

public class UniverseNotFoundException extends RuntimeException {

  private final String universe;

  public UniverseNotFoundException(String universe) {
    super("Universe " + universe + " does not exist");
    this.universe = universe;
  }

  public String getUniverse() {
    return universe;
  }
}
