package manager;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.item.*;
import model.loot.DungeonLootFactory;
import model.upgrade.UpgradeFactory;
import model.upgrade.UpgradeModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class Database {

    public static final Collection<String> rarities = Arrays.asList("gew\u00f6hnlich", "selten", "episch", "legend\u00e4r");
    public static final ListProperty<Armor> armorList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<DungeonLootFactory> dungeonLootList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Event> eventList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Jewellery> jewelleryList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Plant> plantList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Spell> spellList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Weapon> weaponList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<UpgradeFactory> upgradeList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<UpgradeModel> upgradeModelList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Item> itemList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<CraftingBonus> craftingBonusList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Fabrication> fabricationList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Inconsistency> inconsistencyList = new SimpleListProperty<>(FXCollections.observableArrayList());

    public static final MapProperty<UpgradeModel, UpgradeFactory> upgradeMap = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public static final BooleanBinding inconsistent = Bindings.isEmpty(inconsistencyList).not();

    public static void init() {
        upgradeList.addListener((ob, o, n) -> {
            upgradeMap.clear();
            ObservableList<UpgradeModel> list = FXCollections.observableArrayList();

            for (UpgradeFactory factory : n) {
                Collection<UpgradeModel> models = factory.getModels();
                list.addAll(models);

                for (UpgradeModel model : models) {
                    upgradeMap.put(model, factory);
                }
            }

            upgradeModelList.set(list);
        });
    }

    /**
     * If an item with the given name exists in the {@link #itemList},
     * it returns that item.
     * If there is no item, it returns an item with the name and the suffix
     * "(Nicht in  der Datenbank)".
     *
     * @param name of the searched item
     * @return a more ore less matching item
     */
    public static Item getItem(String name) {
        Item item = new Item();
        item.setName(name + " (Nicht in  der Datenbank)");
        return getItemOrElse(name, item);
    }

    /**
     * If an item with the given name exists in the {@link #itemList},
     * it returns that item.
     * If there is no item, it returns the item specified in the
     * parameters.
     *
     * @param name of the searched item
     * @param item fallback item
     * @return a matching item or the fallback
     */
    public static Item getItemOrElse(String name, Item item) {
        return itemList.stream().filter(x -> trimSpaces(x.getName()).equalsIgnoreCase(trimSpaces(name)))
                .findFirst().orElse(item);
    }

    /**
     * If an item with the given name exists in the {@link #itemList},
     * it returns that item.
     * If there is no item, it throws a {@link NoSuchElementException}.
     *
     * @param name of the searched item
     * @return an matching item
     * @throws NoSuchElementException if there is no item with the given name
     */
    public static Item getItemWithoutDefault(String name) throws NoSuchElementException {
        return itemList.stream().filter(x -> trimSpaces(x.getName()).equalsIgnoreCase(trimSpaces(name)))
                .findFirst().orElseThrow();
    }

    private static String trimSpaces(String string) {
        return Arrays.stream(string.split(" ")).map(String::trim).collect(Collectors.joining(" "));
    }
}
