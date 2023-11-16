package de.pnp.manager.server.service.attributes;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.service.RepositoryServiceBaseTest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link PrimaryAttributeService}.
 */
public class PrimaryAttributesServiceTest extends
    RepositoryServiceBaseTest<PrimaryAttribute, PrimaryAttributeRepository, PrimaryAttributeService> {

    public PrimaryAttributesServiceTest(@Autowired PrimaryAttributeService primaryAttributeService,
        @Autowired PrimaryAttributeRepository repository) {
        super(primaryAttributeService, repository, PrimaryAttribute.class);
    }

    @Override
    protected List<PrimaryAttribute> createObjects() {
        return List.of(new PrimaryAttribute(null, "Strength", "STR"),
            new PrimaryAttribute(null, "Intelligence", "INT"),
            new PrimaryAttribute(null, "Endurance", "END"));
    }
}
