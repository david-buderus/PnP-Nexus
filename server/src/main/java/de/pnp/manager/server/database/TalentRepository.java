package de.pnp.manager.server.database;

import de.pnp.manager.component.character.Talent;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Talent talents}.
 */
@Component
public class TalentRepository extends RepositoryBase<Talent> {

  /**
   * Name of the repository
   */
  public static final String REPOSITORY_NAME = "talent";

  public TalentRepository() {
    super(Talent.class, REPOSITORY_NAME);
  }
}
