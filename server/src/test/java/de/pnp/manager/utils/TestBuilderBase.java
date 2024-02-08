package de.pnp.manager.utils;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.server.database.item.ItemTypeRepository;

/**
 * Base class for builders.
 */
public abstract class TestBuilderBase {

    /**
     * Name of the connected universe.
     */
    protected final String universe;

    private final ItemTypeRepository typeRepository;

    public TestBuilderBase(String universe, ItemTypeRepository typeRepository) {
        this.universe = universe;
        this.typeRepository = typeRepository;
    }

    /**
     * Returns a {@link ItemType} with the given name.
     * <p>
     * If a {@link #typeRepository} has been given, the returned type will be persisted.
     */
    protected ItemType getType(String typeName) {
        if (typeRepository == null) {
            return new ItemType(null, typeName, ETypeRestriction.ITEM);
        }
        return typeRepository.get(universe, typeName).orElseGet(() ->
            typeRepository.insert(universe, new ItemType(null, typeName, ETypeRestriction.ITEM)));
    }
}
