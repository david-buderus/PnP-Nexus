package de.pnp.manager.component;

import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;

/**
 * An interface for {@link IUniquelyNamedRepository}.
 */
public interface IUniquelyNamedDataObject {

  /**
   * The human-readable name of this object.
   * <p>
   * This entry is always unique.
   */
  String getName();

}
