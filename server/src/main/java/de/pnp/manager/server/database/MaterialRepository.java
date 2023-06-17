package de.pnp.manager.server.database;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.interfaces.IUniqueNameRepository;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Material materials}
 */
@Component
public class MaterialRepository extends RepositoryBase<Material> implements
    IUniqueNameRepository<Material> {

  /**
   * Name of the repository
   */
  public static final String REPOSITORY_NAME = "materials";

  public MaterialRepository() {
    super(Material.class, REPOSITORY_NAME);
  }
}
