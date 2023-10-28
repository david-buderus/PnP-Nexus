package de.pnp.manager.server.service;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.Material.MaterialItem;
import de.pnp.manager.server.database.MaterialRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link MaterialService}.
 */
public class MaterialServiceTest extends RepositoryServiceBaseTest<Material, MaterialRepository, MaterialService> {

    public MaterialServiceTest(@Autowired MaterialService materialService) {
        super(materialService, Material.class);
    }

    @Override
    protected List<Material> createObjects() {

        return List.of(
            new Material(null, "Iron", List.of(
                new MaterialItem(1, createItem().withName("Iron Ingot").persist().buildItem()),
                new MaterialItem(10, createItem().withName("Iron Nugget").persist().buildItem())
            )),
            new Material(null, "Wood", List.of(
                new MaterialItem(1, createItem().withName("Branch").persist().buildItem())
            )),
            new Material(null, "Leather", List.of(
                new MaterialItem(1, createItem().withName("Leather piece").persist().buildItem())
            ))
        );
    }
}
