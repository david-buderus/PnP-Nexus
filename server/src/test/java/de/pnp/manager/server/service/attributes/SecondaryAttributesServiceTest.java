package de.pnp.manager.server.service.attributes;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
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
            createSecondaryAttribute().withName("Power").addDependency(strength).withFormula("2 * STR").build(),
            createSecondaryAttribute().withName("Health").isConsumable().addDependency(strength)
                .withFormula("10 * STR + 5").build(),
            createSecondaryAttribute().withName("Damage").addDependency(strength).withFormula("0.1 * STR").build());
    }
}
