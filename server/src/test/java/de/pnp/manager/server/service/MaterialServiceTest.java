package de.pnp.manager.server.service;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.MaterialRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link MaterialService}.
 */
public class MaterialServiceTest extends RepositoryServiceBaseTest<Material, MaterialRepository, MaterialService> {

    public MaterialServiceTest(@Autowired MaterialService materialService) {
        super(materialService);
    }

    @Override
    protected List<Material> createObjects() {

        return List.of(
            new Material(null, "Iron", Collections.emptyList()),
            new Material(null, "Wood", Collections.emptyList()),
            new Material(null, "Leather", Collections.emptyList())
        );
    }
}
