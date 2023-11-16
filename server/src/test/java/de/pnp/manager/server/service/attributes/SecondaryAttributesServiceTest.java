package de.pnp.manager.server.service.attributes;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute.PrimaryAttributeDependency;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.server.service.RepositoryServiceBaseTest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link SecondaryAttributeService}.
 */
public class SecondaryAttributesServiceTest extends
    RepositoryServiceBaseTest<SecondaryAttribute, SecondaryAttributeRepository, SecondaryAttributeService> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    public SecondaryAttributesServiceTest(@Autowired SecondaryAttributeService secondaryAttributeService,
        @Autowired SecondaryAttributeRepository repository) {
        super(secondaryAttributeService, repository, SecondaryAttribute.class);
    }

    @Override
    protected List<SecondaryAttribute> createObjects() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Strength", "STR"));
        return List.of(
            new SecondaryAttribute(null, "Power", false, List.of(new PrimaryAttributeDependency(2, strength))),
            new SecondaryAttribute(null, "Health", true, List.of(new PrimaryAttributeDependency(10, strength))),
            new SecondaryAttribute(null, "Damage", false, List.of(new PrimaryAttributeDependency(0.1, strength))));
    }
}
