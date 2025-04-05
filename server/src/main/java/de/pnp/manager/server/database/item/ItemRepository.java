package de.pnp.manager.server.database.item;

import de.pnp.manager.Tag;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.TagRepository;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Item items}.
 */
@Component
public class ItemRepository extends RepositoryBase<Item> implements IUniquelyNamedRepository<Item> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "items";

    @Autowired
    private TagRepository tagRepository;

    public ItemRepository() {
        super(Item.class, REPOSITORY_NAME);
    }

    /**
     * Returns all {@link Armor} in this repository.
     */
    public Collection<Armor> getAllArmor(String universe) {
        return getAllByClass(universe, Armor.class);
    }

    /**
     * Returns all {@link Weapon} in this repository.
     */
    public Collection<Weapon> getAllWeapons(String universe) {
        return getAllByClass(universe, Weapon.class);
    }

    /**
     * Returns all {@link Jewellery} in this repository.
     */
    public Collection<Jewellery> getAllJewellery(String universe) {
        return getAllByClass(universe, Jewellery.class);
    }

    /**
     * Returns all {@link Shield} in this repository.
     */
    public Collection<Shield> getAllShields(String universe) {
        return getAllByClass(universe, Shield.class);
    }

    @Override
    protected void onAfterPersist(String universe, List<Item> objects) {
        super.onAfterPersist(universe, objects);

        Set<Tag> newTags = new HashSet<>();
        for (Item item : objects) {
            newTags.addAll(item.getTags());
        }
        tagRepository.saveAll(universe, newTags);
    }

    private <I> Collection<I> getAllByClass(String universe, Class<I> clazz) {
        return getTemplate(universe).find(Query.query(Criteria.where("_class").is(clazz.getTypeName())),
            clazz, collectionName);
    }
}
