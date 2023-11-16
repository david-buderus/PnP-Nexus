package de.pnp.manager.server.database.attributes;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.server.database.RepositoryBase;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link PrimaryAttribute}
 */
@Component
public class PrimaryAttributeRepository extends RepositoryBase<PrimaryAttribute> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "primaryAttribute";

    public PrimaryAttributeRepository() {
        super(PrimaryAttribute.class, REPOSITORY_NAME);
    }
}
