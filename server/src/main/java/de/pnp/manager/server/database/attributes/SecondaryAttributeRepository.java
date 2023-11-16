package de.pnp.manager.server.database.attributes;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.server.database.RepositoryBase;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link SecondaryAttribute}
 */
@Component
public class SecondaryAttributeRepository extends RepositoryBase<SecondaryAttribute> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "secondaryAttribute";

    public SecondaryAttributeRepository() {
        super(SecondaryAttribute.class, REPOSITORY_NAME);
    }
}
