package manager;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import model.CraftingBonus;
import model.Fabrication;
import model.Inconsistency;
import model.Spell;
import model.item.*;
import model.loot.DungeonLootFactory;
import model.member.generation.GenerationBase;
import model.member.generation.Talent;
import model.member.generation.specs.*;
import model.upgrade.UpgradeFactory;
import model.upgrade.UpgradeModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class Database {

    public static final ListProperty<Armor> armorList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<DungeonLootFactory> dungeonLootList = new SimpleListProperty<>(FXCollections.observableArrayList());
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
    public static final ListProperty<Talent> talentList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<String> shieldTypes = new SimpleListProperty<>(FXCollections.observableArrayList());

    public static final ListProperty<GenerationBase> generationBaseList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Characterisation> characterisationList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Race> raceList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Profession> professionList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<FightingStyle> fightingStyleList = new SimpleListProperty<>(FXCollections.observableArrayList());
    public static final ListProperty<Specialisation> specialisationList = new SimpleListProperty<>(FXCollections.observableArrayList());

    public static final MapProperty<UpgradeModel, UpgradeFactory> upgradeMap = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public static final BooleanBinding inconsistent = Bindings.isEmpty(inconsistencyList).not();

    private static final MapProperty<Integer, ObservableSet<String>> materialsMap = new SimpleMapProperty<>(FXCollections.observableHashMap());

    static {
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
        itemList.addListener((ob, o, n) -> {
            materialsMap.clear();

            for (Item item : n) {
                if (item instanceof Equipment) {
                    Equipment equipment = (Equipment) item;

                    if (!materialsMap.containsKey(equipment.getTier())) {
                        materialsMap.put(equipment.getTier(), FXCollections.observableSet());
                    }

                    materialsMap.get(equipment.getTier()).add(equipment.getMaterial());
                }
            }
        });

        addObservableList(generationBaseList, characterisationList);
        addObservableList(generationBaseList, raceList);
        addObservableList(generationBaseList, professionList);
        addObservableList(generationBaseList, fightingStyleList);
        addObservableList(generationBaseList, specialisationList);
    }

    /**
     * If an item with the given name exists in the {@link #itemList},
     * it returns that item.
     * If there is no item, it returns an item with the name and the suffix
     * "(database.notFound)".
     *
     * @param name of the searched item
     * @return the matching item or a fallback item with the given name
     */
    public static Item getItem(String name) {
        Item item = new Item();
        item.setName(name + " (" + LanguageUtility.getMessage("database.notFound") + ")");
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
     * If an spell with the given name exists in the {@link #spellList},
     * it returns that spell.
     * If there is no spell, it returns an spell with the name and the suffix
     * "(database.notFound)".
     *
     * @param name of the searched spell
     * @return the matching spell or a fallback spell with the given name
     */
    public static Spell getSpell(String name) {
        Spell spell = new Spell();
        spell.setName(name + " (" + LanguageUtility.getMessage("database.notFound") + ")");
        return getSpellOrElse(name, spell);
    }

    /**
     * If an spell with the given name exists in the {@link #spellList},
     * it returns that spell.
     * If there is no spell, it returns the spell specified in the
     * parameters.
     *
     * @param name of the searched spell
     * @param spell fallback spell
     * @return a matching spell or the fallback
     */
    public static Spell getSpellOrElse(String name, Spell spell) {
        return spellList.stream().filter(x -> trimSpaces(x.getName()).equalsIgnoreCase(trimSpaces(name)))
                .findFirst().orElse(spell);
    }

    /**
     * If an item with the given name exists in the {@link #itemList},
     * it returns that item.
     * If there is no item, it throws a {@link NoSuchElementException}.
     *
     * @param name of the searched item
     * @return a matching item
     * @throws NoSuchElementException if there is no item with the given name
     */
    public static Item getItemWithoutDefault(String name) throws NoSuchElementException {
        return itemList.stream().filter(x -> trimSpaces(x.getName()).equalsIgnoreCase(trimSpaces(name)))
                .findFirst().orElseThrow();
    }

    /**
     * If a talent with the given name exists in the {@link #talentList},
     * it returns that talent.
     * If there is no talent with that name, it returns a new talent with the name and the suffix
     * "(database.notFound)".
     *
     * @param name of the searched talent
     * @return the matching talent or a fallback talent with the given name
     */
    public static Talent getTalent(String name) {
        Talent talent = new Talent();
        talent.setName(name + " (" + LanguageUtility.getMessage("database.notFound") + ")");
        return getTalentOrElse(name, talent);
    }

    /**
     * If a talent with the given name exists in the {@link #talentList},
     * it returns that talent.
     * If there is no talent, it returns the talent specified in the
     * parameters.
     *
     * @param name   of the searched talent
     * @param talent fallback talent
     * @return a matching talent or the fallback
     */
    public static Talent getTalentOrElse(String name, Talent talent) {
        return talentList.stream().filter(x -> trimSpaces(x.getName()).equalsIgnoreCase(trimSpaces(name)))
                .findFirst().orElse(talent);
    }

    /**
     * If a talent with the given name exists in the {@link #talentList},
     * it returns that talent.
     * If there is no talent, it throws a {@link NoSuchElementException}.
     *
     * @param name of the searched talent
     * @return a matching talent
     * @throws NoSuchElementException if there is no talent with the given name
     */
    public static Talent getTalentWithoutDefault(String name) throws NoSuchElementException {
        return talentList.stream().filter(x -> trimSpaces(x.getName()).equalsIgnoreCase(trimSpaces(name)))
                .findFirst().orElseThrow();
    }

    /**
     * Returns a Collection of String which
     * represents materials of the database
     *
     * @param tier of the material
     * @return a list of materials of the specific tier
     */
    public static Collection<String> getMaterialsOfTier(int tier) {
        return materialsMap.getOrDefault(tier, FXCollections.observableSet());
    }

    /**
     * Generates a Collection of String which
     * represents materials of the database.
     * Each material will be of the same, but random tier.
     *
     * @return a list of materials of a random tier
     */
    public static Collection<String> getRandomMaterial() {
        return getMaterialsOfTier(Utility.getRandomTier());
    }

    private static String trimSpaces(String string) {
        return Arrays.stream(string.split(" ")).map(String::trim).collect(Collectors.joining(" "));
    }

    private static <U, T extends U>  void addObservableList(Collection<U> collection, ObservableList<T> toAdd) {
        toAdd.addListener((ListChangeListener<T>) c -> {
            while(c.next()) {
                collection.addAll(c.getAddedSubList());
                collection.removeAll(c.getRemoved());
            }
        });
    }
}
