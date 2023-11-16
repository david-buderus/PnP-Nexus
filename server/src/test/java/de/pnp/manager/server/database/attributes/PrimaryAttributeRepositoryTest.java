package de.pnp.manager.server.database.attributes;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.server.database.RepositoryTestBase;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link PrimaryAttributeRepository}
 */
class PrimaryAttributeRepositoryTest extends RepositoryTestBase<PrimaryAttribute, PrimaryAttributeRepository> {

    public PrimaryAttributeRepositoryTest(@Autowired PrimaryAttributeRepository repository) {
        super(repository);
    }

    @Override
    protected PrimaryAttribute createObject() {
        return new PrimaryAttribute(null, "Strength", "ST");
    }

    @Override
    protected PrimaryAttribute createSlightlyChangeObject() {
        return new PrimaryAttribute(null, "Strength", "STR");
    }

    @Override
    protected List<PrimaryAttribute> createMultipleObjects() {
        return List.of(new PrimaryAttribute(null, "Strength", "STR"),
            new PrimaryAttribute(null, "Intelligence", "INT"));
    }
}
