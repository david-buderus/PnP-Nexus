package de.pnp.manager.server.database;

import de.pnp.manager.component.Fabrication;
import org.springframework.stereotype.Component;

@Component
public class FabricationRepository extends RepositoryBase<Fabrication> {

  public static final String REPOSITORY_NAME = "fabrication";

  public FabricationRepository() {
    super(Fabrication.class, REPOSITORY_NAME);
  }
}
