package de.pnp.manager.webapp.database;

import de.pnp.manager.component.item.ExtendedItemType;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.item.ItemTypeTranslation;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.contoller.ExtendedItemTypeController;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for testing item type overview pages.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
public class ItemTypePageTest extends OverviewTestBase<ExtendedItemType> {

    @Autowired
    private ItemTypeRepository typeRepository;

    @Autowired
    private ExtendedItemTypeController typeController;

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openItemTypePage();
    }

    @Override
    protected Collection<ExtendedItemType> getTestObjects() {
        return typeController.getAll(getUniverseName());
    }

    @Override
    protected Comparator<ExtendedItemType> getDefaultSort() {
        return Comparator.comparing(ExtendedItemType::getName);
    }

    @Override
    protected List<Pair<String, Comparator<ExtendedItemType>>> getSorters() {
        return List.of(Pair.of("typeRestriction", Comparator.comparing(type -> type.getTypeRestriction().name())));
    }

    @Override
    protected ExtendedItemType getWrongObject() {
        ItemType swordType = typeRepository.get(getUniverseName(), "Sword").orElseThrow();

        ItemType itemType = new ItemType(null, "", ETypeRestriction.WEAPON);
        return ExtendedItemType.from(itemType, new ItemTypeTranslation(null, itemType, Set.of(swordType)));
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name");
    }

    @Override
    protected ExtendedItemType getCorrectObject() {
        ItemType swordType = typeRepository.get(getUniverseName(), "Sword").orElseThrow();

        ItemType itemType = new ItemType(null, "Blade", ETypeRestriction.WEAPON);
        return ExtendedItemType.from(itemType, new ItemTypeTranslation(null, itemType, Set.of(swordType)));
    }

    @Override
    protected String getIdentifier(ExtendedItemType object) {
        return object.getName();
    }

    @Override
    protected Optional<ExtendedItemType> getPersistedObject(ExtendedItemType object) {
        return typeController.getAll(getUniverseName()).stream().filter(type -> type.getName().equals(object.getName()))
            .findFirst();
    }

    @Override
    protected Optional<ExtendedItemType> getPersistedObject(ObjectId id) {
        return typeController.getAll(getUniverseName(), List.of(id)).stream().findFirst();
    }

    @Override
    protected ObjectId getModifyId() {
        return typeRepository.get(getUniverseName(), "Sword").orElseThrow().getId();
    }

    @Override
    protected ExtendedItemType getEditedObject() {
        return new ExtendedItemType(null, "Super Sword", ETypeRestriction.WEAPON, null, Set.of());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Super Sword";
    }
}
