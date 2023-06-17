package de.pnp.manager.server.database;

import de.pnp.manager.component.Fabrication;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Fabrication fabrications}
 */
@Component
public class FabricationRepository extends RepositoryBase<Fabrication> {

  /**
   * Name of the repository
   */
  public static final String REPOSITORY_NAME = "fabrication";

  public FabricationRepository() {
    super(Fabrication.class, REPOSITORY_NAME);
  }
}
