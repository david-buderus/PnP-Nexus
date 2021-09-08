package manager;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Currency;
import model.*;
import model.item.*;
import model.loot.DungeonLootFactory;
import model.member.generation.*;
import model.member.generation.specs.*;
import model.other.CraftingBonus;
import model.other.Spell;
import model.other.Talent;
import model.upgrade.UpgradeFactory;
import net.ucanaccess.complex.SingleValue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class DatabaseLoader {

    public static final ObjectProperty<Language> tableLanguage = new SimpleObjectProperty<>(Language.system);

    private static final ObjectProperty<ResourceBundle> tables = new SimpleObjectProperty<>();

    private static boolean loadSynchronous = false;

    static {
        reloadLanguage(tableLanguage.get());
        tableLanguage.addListener((ob, o, n) -> reloadLanguage(n));
    }

    private static final Map<String, String> talentTypes = new HashMap<>();

    public static Collection<String> loadDatabase(Connection connection, boolean loadSynchronous) throws SQLException {
        DatabaseLoader.loadSynchronous = loadSynchronous;

        TypTranslation.clear();
        Collection<String> info = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {

            try {
                final Semaphore semaphore = new Semaphore(0);
                info.add(loadWeapons(statement, semaphore));
                info.add(loadArmor(statement, semaphore));
                info.add(loadJewellery(statement, semaphore));
                info.add(loadPlants(statement, semaphore));

                semaphore.acquire(4);
                info.add(loadItems(statement, semaphore));
                info.add(loadTalents(statement, semaphore));
                info.add(loadEquivalences(statement));

                semaphore.acquire(2);
                info.add(loadSpellsTypes(statement));
                info.add(loadSpells(statement, semaphore));
                info.add(loadUpgrades(statement, semaphore));
                info.add(loadDungeonLoot(statement, semaphore));
                info.add(loadCraftingBoni(statement, semaphore));
                info.add(loadFabrication(statement, semaphore));
                info.add(loadShieldTypes(statement, semaphore));

                semaphore.acquire(6);
                info.add(loadEnemies(statement, semaphore));

                semaphore.acquire();
                checkInconsistencies();

            } catch (InterruptedException ignored) {
                ignored.printStackTrace();
            }
        }

        return info.stream().filter(s -> !s.isBlank()).collect(Collectors.toList());
    }

    private static String loadWeapons(Statement statement, Semaphore semaphore) {
        ObservableList<Weapon> weaponList = FXCollections.observableArrayList();

        try (ResultSet weaponSet = statement.executeQuery(format("SELECT * FROM %s NATURAL JOIN %s", "table.items", "table.weapons"))) {
            while (weaponSet.next()) {
                Weapon weapon = new Weapon();
                weapon.setName(getString(weaponSet, getLocalized("column.name")));
                weapon.setMaterial(getString(weaponSet, getLocalized("column.material")));
                weapon.setType(getString(weaponSet, getLocalized("column.type")));
                weapon.setSubtype(getString(weaponSet, getLocalized("column.subtype")));
                weapon.setRequirement(getString(weaponSet, getLocalized("column.requirement")));
                weapon.setInitiative(getFloat(weaponSet, getLocalized("column.initiative")));
                weapon.setInitiativeModifier(getFloat(weaponSet, getLocalized("column.initiativeModifier")));
                weapon.setDice(getString(weaponSet, getLocalized("column.dice_weight")));
                weapon.setDamage(weaponSet.getInt(getLocalized("column.damage_protection")));
                weapon.setHit(weaponSet.getInt(getLocalized("column.hit")));
                weapon.setEffect(getString(weaponSet, getLocalized("column.effect")));
                weapon.setUpgradeSlots(weaponSet.getInt(getLocalized("column.upgradeSlots")));
                weapon.setRarity(getRarity(weaponSet, getLocalized("column.rarity")));
                weapon.setCurrency(new Currency(getString(weaponSet, getLocalized("column.price"))));
                weapon.setTier(weaponSet.getInt(getLocalized("column.tier")));

                weaponList.add(weapon);

                TypTranslation.add(weapon.getType(), getLocalized("type.weapon"));
            }

        } catch (SQLException e) {
            System.out.println(e);
            return getErrorString("table.weapons");
        } finally {
            if (loadSynchronous) {
                Database.weaponList.set(weaponList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.weaponList.set(weaponList);
                    semaphore.release();
                });
            }
        }

        return "";
    }

    private static String loadArmor(Statement statement, Semaphore semaphore) {
        ObservableList<Armor> armorList = FXCollections.observableArrayList();

        try (ResultSet armorSet = statement.executeQuery(format("SELECT * FROM %s NATURAL JOIN %s", "table.items", "table.armors"))) {

            while (armorSet.next()) {
                Armor armor = new Armor();
                armor.setName(getString(armorSet, getLocalized("column.name")));
                armor.setMaterial(getString(armorSet, getLocalized("column.material")));
                armor.setType(getString(armorSet, getLocalized("column.type")));
                armor.setSubtype(getString(armorSet, getLocalized("column.subtype")));
                armor.setRequirement(getString(armorSet, getLocalized("column.requirement")));
                armor.setProtection(armorSet.getInt(getLocalized("column.protection")));
                armor.setWeight(armorSet.getDouble(getLocalized("column.weight")));
                armor.setEffect(getString(armorSet, getLocalized("column.effect")));
                armor.setUpgradeSlots(armorSet.getInt(getLocalized("column.upgradeSlots")));
                armor.setRarity(getRarity(armorSet, getLocalized("column.rarity")));
                armor.setCurrency(new Currency(getString(armorSet, getLocalized("column.price"))));
                armor.setTier(armorSet.getInt(getLocalized("column.tier")));

                armorList.add(armor);

                TypTranslation.add(armor.getType(), getLocalized("type.armor"));
            }

        } catch (SQLException e) {
            return getErrorString("table.armors");
        } finally {
            if (loadSynchronous) {
                Database.armorList.set(armorList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.armorList.set(armorList);
                    semaphore.release();
                });
            }

        }
        return "";
    }

    private static String loadJewellery(Statement statement, Semaphore semaphore) {
        ObservableList<Jewellery> jewelleryList = FXCollections.observableArrayList();

        try (ResultSet jewellerySet = statement.executeQuery(format("SELECT * FROM %s NATURAL JOIN %s", "table.items", "table.jewellery"))) {

            while (jewellerySet.next()) {
                Jewellery jewellery = new Jewellery();
                jewellery.setName(getString(jewellerySet, getLocalized("column.name")));
                jewellery.setMaterial(getString(jewellerySet, getLocalized("column.material")));
                jewellery.setGem(getString(jewellerySet, getLocalized("column.gem")));
                jewellery.setType(getString(jewellerySet, getLocalized("column.type")));
                jewellery.setSubtype(getString(jewellerySet, getLocalized("column.subtype")));
                jewellery.setRequirement(getString(jewellerySet, getLocalized("column.requirement")));
                jewellery.setEffect(getString(jewellerySet, getLocalized("column.effect")));
                jewellery.setUpgradeSlots(jewellerySet.getInt(getLocalized("column.upgradeSlots")));
                jewellery.setRarity(getRarity(jewellerySet, getLocalized("column.rarity")));
                jewellery.setCurrency(new Currency(getString(jewellerySet, getLocalized("column.price"))));
                jewellery.setTier(jewellerySet.getInt(getLocalized("column.tier")));

                jewelleryList.add(jewellery);

                TypTranslation.add(jewellery.getType(), getLocalized("type.jewellery"));
            }
        } catch (SQLException e) {
            return getErrorString("table.jewellery");
        } finally {
            if (loadSynchronous) {
                Database.jewelleryList.set(jewelleryList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.jewelleryList.set(jewelleryList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadPlants(Statement statement, Semaphore semaphore) {
        ObservableList<Plant> plantList = FXCollections.observableArrayList();

        try (ResultSet plantSet = statement.executeQuery(format("SELECT * FROM %s WHERE %s=\"%s\"", "table.items", "column.type", "type.plant"))) {

            while (plantSet.next()) {
                Plant plant = new Plant();
                plant.setName(getString(plantSet, getLocalized("column.name")));
                plant.setType(getString(plantSet, getLocalized("column.type")));
                plant.setSubtype(getString(plantSet, getLocalized("column.subtype")));
                plant.setEffect(getString(plantSet, getLocalized("column.effect")));
                plant.setRarity(getRarity(plantSet, getLocalized("column.rarity")));
                plant.setCurrency(new Currency(getString(plantSet, getLocalized("column.price"))));
                plant.setTier(plantSet.getInt(getLocalized("column.tier")));
                plant.setLocations(getCollection(statement,
                        format("SELECT %s FROM %s WHERE %s=\"%s\"",
                                "column.place", "table.places", "column.name", plant.getName()), "column.place"));

                plantList.add(plant);
            }
        } catch (SQLException e) {
            return getErrorString("column.type");
        } finally {
            if (loadSynchronous) {
                Database.plantList.set(plantList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.plantList.set(plantList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadItems(Statement statement, Semaphore semaphore) {
        ObservableList<Item> itemList = FXCollections.observableArrayList();

        try (ResultSet itemSet = statement.executeQuery(
                format("SELECT * FROM %s WHERE %s NOT IN (\"%s\", \"%s\", \"%s\", \"%s\")",
                        "table.items", "column.type", "type.weapon", "type.armor", "type.jewellery", "type.plant"))) {

            while (itemSet.next()) {
                Item item = new Item();
                item.setType(getString(itemSet, getLocalized("column.type")));
                item.setName(getString(itemSet, getLocalized("column.name")));
                item.setSubtype(getString(itemSet, getLocalized("column.subtype")));
                item.setEffect(getString(itemSet, getLocalized("column.effect")));
                item.setRarity(getRarity(itemSet, getLocalized("column.rarity")));
                item.setCurrency(new Currency(getString(itemSet, getLocalized("column.price"))));
                item.setTier(itemSet.getInt(getLocalized("column.tier")));

                itemList.add(item);
            }
        } catch (SQLException e) {
            return getErrorString("table.items");
        } finally {
            if (loadSynchronous) {
                Database.itemList.set(itemList);
                Database.itemList.addAll(Database.weaponList);
                Database.itemList.addAll(Database.armorList);
                Database.itemList.addAll(Database.jewelleryList);
                Database.itemList.addAll(Database.plantList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.itemList.set(itemList);
                    Database.itemList.addAll(Database.weaponList);
                    Database.itemList.addAll(Database.armorList);
                    Database.itemList.addAll(Database.jewelleryList);
                    Database.itemList.addAll(Database.plantList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadEquivalences(Statement statement) {
        try (ResultSet eqSet = statement.executeQuery(format("SELECT * FROM %s", "table.equivalences"))) {

            while (eqSet.next()) {
                String sub = getString(eqSet, getLocalized("column.subtype"));
                String main = getString(eqSet, getLocalized("column.mainType"));

                TypTranslation.add(sub, main);
            }

        } catch (SQLException e) {
            return getErrorString("table.equivalences");
        }
        return "";
    }

    private static String loadTalents(Statement statement, Semaphore semaphore) {
        ObservableList<Talent> talentList = FXCollections.observableArrayList();

        try (ResultSet talentSet = statement.executeQuery(format("SELECT * FROM %s", "table.talents"))) {

            while (talentSet.next()) {
                Talent talent = new Talent();
                talent.setName(getString(talentSet, getLocalized("column.name")));
                // TODO attributes for imrpvovised weapons
                talent.setAttributes(new PrimaryAttribute[]{
                        PrimaryAttribute.getPrimaryAttributeOrElse(getString(talentSet, getLocalized("column.attribute1")), PrimaryAttribute.DUMMY),
                        PrimaryAttribute.getPrimaryAttributeOrElse(getString(talentSet, getLocalized("column.attribute2")), PrimaryAttribute.DUMMY),
                        PrimaryAttribute.getPrimaryAttributeOrElse(getString(talentSet, getLocalized("column.attribute3")), PrimaryAttribute.DUMMY)
                });
                talent.setMagicTalent(talentSet.getBoolean(getLocalized("column.magicTalent")));
                talent.setWeaponTalent(talentSet.getBoolean(getLocalized("column.weaponTalent")));

                talentList.add(talent);
            }
        } catch (SQLException e) {
            return getErrorString("table.talents");
        } finally {
            if (loadSynchronous) {
                Database.talentList.set(talentList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.talentList.set(talentList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadSpells(Statement statement, Semaphore semaphore) {
        ObservableList<Spell> spellList = FXCollections.observableArrayList();

        try (ResultSet spellSet = statement.executeQuery(format("SELECT * FROM %s", "table.spells"))) {

            while (spellSet.next()) {
                Spell spell = new Spell();
                spell.setName(getString(spellSet, getLocalized("column.name")));
                spell.setEffect(getString(spellSet, getLocalized("column.effect")));
                spell.setType(Arrays.stream((SingleValue[]) spellSet.getObject(getLocalized("column.type"))).
                        map(val -> (String) val.getValue()).collect(Collectors.joining(",")));
                spell.setCost(getString(spellSet, getLocalized("column.cost")));
                spell.setCastTime(getString(spellSet, getLocalized("column.castTime")));
                spell.setTier(spellSet.getInt(getLocalized("column.tier")));
                spell.setTalent(getTalent(spell.getType()));

                spellList.add(spell);
            }
        } catch (SQLException e) {
            return getErrorString("table.spells");
        } finally {
            if (loadSynchronous) {
                Database.spellList.set(spellList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.spellList.set(spellList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadSpellsTypes(Statement statement) {
        talentTypes.clear();

        try (ResultSet spellSet = statement.executeQuery(format("SELECT * FROM %s", "table.spellTypes"))) {

            while (spellSet.next()) {
                String typ = getString(spellSet, getLocalized("column.type"));
                String talent = getString(spellSet, getLocalized("column.talent"));

                talentTypes.put(typ, talent);
            }
        } catch (SQLException e) {
            return getErrorString("table.spellTypes");
        }
        return "";
    }

    private static String loadUpgrades(Statement statement, Semaphore semaphore) {
        ObservableList<UpgradeFactory> upgradeList = FXCollections.observableArrayList();

        try (ResultSet upgradeSet = statement.executeQuery(format("SELECT * FROM %s", "table.upgrades"))) {

            while (upgradeSet.next()) {

                String name = getString(upgradeSet, getLocalized("column.name"));
                int level = upgradeSet.getInt(getLocalized("column.level"));

                UpgradeFactory exist = getUpgradeFactory(name, upgradeList);

                if (exist != null) {
                    if (exist.getMaxLevel() < level) {
                        exist.setMaxLevel(level);
                    }
                    exist.setEffect(level, getString(upgradeSet, getLocalized("column.effect")));
                    exist.setCurrency(level, new Currency(getString(upgradeSet, getLocalized("column.price"))));
                    exist.setMana(level, getString(upgradeSet, getLocalized("column.mana")));
                    exist.setRequirement(level, getString(upgradeSet, getLocalized("column.requires")));

                    ItemList materials = new ItemList();
                    ResultSet materialSet = statement.executeQuery(format(
                            "SELECT * FROM %s WHERE %s=\"%s\" AND %s=%s",
                            "table.upgrades.materials", "column.name", name, "column.level", String.valueOf(level)));
                    while (materialSet.next()) {
                        String mat = getString(materialSet, getLocalized("column.material"));
                        float amount = materialSet.getFloat(getLocalized("column.amount"));
                        Item material = Database.getItemWithoutDefault(mat).copy();
                        material.setAmount(amount);
                        materials.add(material);
                    }
                    exist.setMaterials(level, materials);

                } else {
                    UpgradeFactory upgradeFactory = new UpgradeFactory();
                    upgradeFactory.setName(name);
                    upgradeFactory.setMaxLevel(level);
                    upgradeFactory.setTarget(getString(upgradeSet, getLocalized("column.target")));
                    upgradeFactory.setSlots(upgradeSet.getInt(getLocalized("column.slots")));
                    upgradeFactory.setRequirement(level, getString(upgradeSet, getLocalized("column.requires")));
                    upgradeFactory.setEffect(level, getString(upgradeSet, getLocalized("column.effect")));
                    upgradeFactory.setCurrency(level, new Currency(getString(upgradeSet, getLocalized("column.price"))));
                    upgradeFactory.setMana(level, getString(upgradeSet, getLocalized("column.mana")));

                    ItemList materials = new ItemList();
                    ResultSet materialSet = statement.executeQuery(format(
                            "SELECT * FROM %s WHERE %s=\"%s\" AND %s=%s",
                            "table.upgrades.materials", "column.name", name, "column.level", String.valueOf(level)));
                    while (materialSet.next()) {
                        String mat = getString(materialSet, getLocalized("column.material"));
                        float amount = materialSet.getFloat(getLocalized("column.amount"));
                        Item material = Database.getItemWithoutDefault(mat).copy();
                        material.setAmount(amount);
                        materials.add(material);
                    }
                    upgradeFactory.setMaterials(level, materials);

                    upgradeList.add(upgradeFactory);
                }
            }
        } catch (SQLException e) {
            return getErrorString("table.upgrades");
        } catch (NoSuchElementException e) {
            return getErrorString("table.upgrades") + ". " + LanguageUtility.getMessage("database.missingMaterial");
        } finally {
            if (loadSynchronous) {
                Database.upgradeList.set(upgradeList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.upgradeList.set(upgradeList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadDungeonLoot(Statement statement, Semaphore semaphore) {
        ObservableList<DungeonLootFactory> lootList = FXCollections.observableArrayList();

        try (ResultSet lootSet = statement.executeQuery(format("SELECT * FROM %s", "table.loot"))) {

            while (lootSet.next()) {
                DungeonLootFactory factory = new DungeonLootFactory();
                String name = getString(lootSet, getLocalized("column.item"));
                factory.setName(name.equals(getLocalized("type.notSpecifiedItem")) ? getString(lootSet, getLocalized("column.info")) : name);
                factory.setContainer(getString(lootSet, getLocalized("column.container")));
                factory.setPlace(getString(lootSet, getLocalized("column.location")));
                factory.setChance(lootSet.getDouble(getLocalized("column.chance")));
                factory.setMaxAmount(lootSet.getInt(getLocalized("column.amount")));

                lootList.add(factory);
            }
        } catch (SQLException e) {
            return getErrorString("table.loot");
        } finally {
            if (loadSynchronous) {
                Database.dungeonLootList.set(lootList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.dungeonLootList.set(lootList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadCraftingBoni(Statement statement, Semaphore semaphore) {
        ObservableList<CraftingBonus> craftingBonusList = FXCollections.observableArrayList();

        try (ResultSet eventSet = statement.executeQuery(format("SELECT * FROM %s", "table.manufacturingImprovements"))) {

            while (eventSet.next()) {
                CraftingBonus craftingBonus = new CraftingBonus();
                craftingBonus.setName(getString(eventSet, getLocalized("column.name")));
                craftingBonus.setTarget(getString(eventSet, getLocalized("column.target")));
                craftingBonus.setEffect(getString(eventSet, getLocalized("column.effect")));

                craftingBonusList.add(craftingBonus);
            }
        } catch (SQLException e) {
            return getErrorString("table.manufacturingImprovements");
        } finally {
            if (loadSynchronous) {
                Database.craftingBonusList.set(craftingBonusList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.craftingBonusList.set(craftingBonusList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadFabrication(Statement statement, Semaphore semaphore) {
        ObservableList<Fabrication> fabricationList = FXCollections.observableArrayList();

        try (ResultSet fabricationSet = statement.executeQuery(format("SELECT * FROM %s", "table.manufacturing"))) {

            while (fabricationSet.next()) {
                Fabrication fabrication = new Fabrication();
                fabrication.setProductName(getString(fabricationSet, getLocalized("column.name")));
                fabrication.setProfession(getString(fabricationSet, getLocalized("column.profession")));
                fabrication.setRequirement(getString(fabricationSet, getLocalized("column.requiredAbilities")));
                fabrication.setOtherCircumstances(getString(fabricationSet, getLocalized("column.otherCircumstances")));
                fabrication.setProductAmount(fabricationSet.getInt(getLocalized("column.createdAmount")));
                fabrication.setSideProductAmount(fabricationSet.getInt(getLocalized("column.createdSideAmount")));
                fabrication.setSideProductName(getString(fabricationSet, getLocalized("column.sideProduct")));

                int id = fabricationSet.getInt(getLocalized("column.id"));

                ItemList materials = new ItemList();
                ResultSet materialSet = statement.executeQuery(
                        format("SELECT * FROM %s WHERE %s=%s",
                                "table.manufacturing.material", "column.id", String.valueOf(id)));
                while (materialSet.next()) {
                    String mat = getString(materialSet, getLocalized("column.material"));
                    float amount = materialSet.getFloat(getLocalized("column.amount"));
                    Item material = Database.getItemWithoutDefault(mat).copy();
                    material.setAmount(amount);
                    materials.add(material);
                }
                fabrication.setMaterials(materials);

                fabricationList.add(fabrication);
            }
        } catch (SQLException e) {
            return getErrorString("table.manufacturing");
        } finally {
            if (loadSynchronous) {
                Database.fabricationList.set(fabricationList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.fabricationList.set(fabricationList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadShieldTypes(Statement statement, Semaphore semaphore) {
        ObservableList<String> shieldTypList = FXCollections.emptyObservableList();

        try {
            shieldTypList = FXCollections.observableArrayList(getCollection(statement,
                    format("SELECT * FROM %s", "table.shieldTypes"), "column.shieldTyp"));

        } catch (SQLException e) {
            return getErrorString("table.shieldTypes");
        } finally {
            final ObservableList<String> fShieldTypList = shieldTypList;

            if (loadSynchronous) {
                Database.shieldTypes.set(fShieldTypList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.shieldTypes.set(fShieldTypList);
                    semaphore.release();
                });
            }
        }
        return "";
    }

    private static String loadEnemies(Statement statement, Semaphore semaphore) {
        ObservableList<Characterisation> characterisationList = FXCollections.emptyObservableList();
        ObservableList<Race> raceList = FXCollections.emptyObservableList();
        ObservableList<Profession> professionList = FXCollections.emptyObservableList();
        ObservableList<FightingStyle> fightingStyleList = FXCollections.emptyObservableList();
        ObservableList<Specialisation> specialisationList = FXCollections.emptyObservableList();

        try {
            // Load raw model from database
            try (ResultSet enemySet = statement.executeQuery(format(
                    "SELECT * FROM %s WHERE %s=\"%s\"",
                    "table.enemies", "column.type", "type.characterisation"))) {
                characterisationList = loadEnemies(statement, enemySet, Characterisation::new);
            } catch (SQLException e) {
                return getErrorString("type.characterisation");
            }
            try (ResultSet enemySet = statement.executeQuery(format(
                    "SELECT * FROM %s WHERE %s=\"%s\"",
                    "table.enemies", "column.type", "type.race"))) {
                raceList = loadEnemies(statement, enemySet, Race::new);
            } catch (SQLException e) {
                return getErrorString("type.race");
            }
            try (ResultSet enemySet = statement.executeQuery(format(
                    "SELECT * FROM %s WHERE %s=\"%s\"",
                    "table.enemies", "column.type", "type.profession"))) {
                professionList = loadEnemies(statement, enemySet, Profession::new);
            } catch (SQLException e) {
                return getErrorString("type.profession");
            }
            try (ResultSet enemySet = statement.executeQuery(format(
                    "SELECT * FROM %s WHERE %s=\"%s\"",
                    "table.enemies", "column.type", "type.fightingStyle"))) {
                fightingStyleList = loadEnemies(statement, enemySet, FightingStyle::new);
            } catch (SQLException e) {
                return getErrorString("type.fightingStyle");
            }
            try (ResultSet enemySet = statement.executeQuery(format(
                    "SELECT * FROM %s WHERE %s=\"%s\"",
                    "table.enemies", "column.type", "type.specialisation"))) {
                specialisationList = loadEnemies(statement, enemySet, Specialisation::new);
            } catch (SQLException e) {
                return getErrorString("type.specialisation");
            }

            // Link parents
            try {
                linkParents(statement, characterisationList);
            } catch (SQLException e) {
                return getLocalized("type.characterisation") + " " + LanguageUtility.getMessage("database.cantGetGrouped");
            }
            try {
                linkParents(statement, raceList);
            } catch (SQLException e) {
                return getLocalized("type.race") + " " + LanguageUtility.getMessage("database.cantGetGrouped");
            }
            try {
                linkParents(statement, professionList);
            } catch (SQLException e) {
                return getLocalized("type.profession") + " " + LanguageUtility.getMessage("database.cantGetGrouped");
            }
            try {
                linkParents(statement, fightingStyleList);
            } catch (SQLException e) {
                return getLocalized("type.fightingStyle") + " " + LanguageUtility.getMessage("database.cantGetGrouped");
            }
            try {
                linkParents(statement, specialisationList);
            } catch (SQLException e) {
                return getLocalized("type.specialisation") + " " + LanguageUtility.getMessage("database.cantGetGrouped");
            }

            // Link subtypes
            try {
                linkSubTypes(statement, characterisationList, raceList);
            } catch (SQLException e) {
                return getLocalized("type.characterisation") + " " + LanguageUtility.getMessage("database.cantGetLinked");
            }
            try {
                linkSubTypes(statement, raceList, professionList);
            } catch (SQLException e) {
                return getLocalized("type.race") + " " + LanguageUtility.getMessage("database.cantGetLinked");
            }
            try {
                linkSubTypes(statement, professionList, fightingStyleList);
            } catch (SQLException e) {
                return getLocalized("type.profession") + " " + LanguageUtility.getMessage("database.cantGetLinked");
            }
            try {
                linkSubTypes(statement, fightingStyleList, specialisationList);
            } catch (SQLException e) {
                return getLocalized("type.fightingStyle") + " " + LanguageUtility.getMessage("database.cantGetLinked");
            }

            Collection<GenerationBase> combinedList = new ArrayList<>();
            combinedList.addAll(characterisationList);
            combinedList.addAll(raceList);
            combinedList.addAll(professionList);
            combinedList.addAll(fightingStyleList);
            combinedList.addAll(specialisationList);

            // Add Talents
            try {
                addMainTalents(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.mainTalents") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }
            try {
                addForbiddenTalents(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.forbiddenTalents") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }

            // Add Attributes
            try {
                addPrimaryAttributes(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.primaryAttributes") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }
            try {
                addSecondaryAttributes(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.secondaryAttributes") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }

            // Add Weapon Types
            try {
                addPrimaryWeaponTypes(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.primaryWeaponType") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }
            try {
                addSecondaryWeaponTypes(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.secondaryWeaponType") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }

            // Add specific equipment
            try {
                addSpecificPrimaryWeapons(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.equippedPrimaryWeapon") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }
            try {
                addSpecificSecondaryWeapons(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.equippedSecondaryWeapon") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }
            try {
                addSpecificArmor(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.equippedArmor") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }
            try {
                addSpecificJewellery(statement, combinedList);
            } catch (SQLException e) {
                e.printStackTrace();
                return getLocalized("table.enemies.equippedJewellery") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }

            // Add drops
            try {
                addDrops(statement, combinedList);
            } catch (SQLException e) {
                return getLocalized("table.enemies.drop") + " " + LanguageUtility.getMessage("database.cantGetSet");
            }
        } finally {
            final ObservableList<Characterisation> fCharacterisationList = characterisationList;
            final ObservableList<Race> fRaceList = raceList;
            final ObservableList<Profession> fProfessionList = professionList;
            final ObservableList<FightingStyle> fFightingStyleList = fightingStyleList;
            final ObservableList<Specialisation> fSpecialisationList = specialisationList;

            if (loadSynchronous) {
                Database.characterisationList.set(fCharacterisationList);
                Database.raceList.set(fRaceList);
                Database.professionList.set(fProfessionList);
                Database.fightingStyleList.set(fFightingStyleList);
                Database.specialisationList.set(fSpecialisationList);
                semaphore.release();
            } else {
                Platform.runLater(() -> {
                    Database.characterisationList.set(fCharacterisationList);
                    Database.raceList.set(fRaceList);
                    Database.professionList.set(fProfessionList);
                    Database.fightingStyleList.set(fFightingStyleList);
                    Database.specialisationList.set(fSpecialisationList);
                    semaphore.release();
                });
            }
        }

        return "";
    }

    private static <Generation extends GenerationBase> ObservableList<Generation> loadEnemies(Statement statement, ResultSet set, Supplier<Generation> constructor) throws SQLException {
        ObservableList<Generation> list = FXCollections.observableArrayList();

        while (set.next()) {
            try {
                Generation generation = constructor.get();
                String name = getString(set, getLocalized("column.name"));

                generation.setName(name);
                generation.setAdvantages(getCollection(
                        statement, format("SELECT * FROM %s WHERE %s=\"%s\"",
                                "table.enemies.advantages", "column.name", name),
                        getLocalized("column.advantages")));
                generation.setDisadvantages(getCollection(
                        statement, format("SELECT * FROM %s WHERE %s=\"%s\"",
                                "table.enemies.disadvantages", "column.name", name),
                        getLocalized("column.disadvantages")));
                generation.setDropsWeapon(set.getBoolean(getLocalized("column.dropsWeapons")));
                generation.setDropsArmor(set.getBoolean(getLocalized("column.dropsArmor")));
                generation.setDropsJewellery(set.getBoolean(getLocalized("column.dropsJewellery")));
                generation.setAbleToUsesPrimaryHand(set.getBoolean(getLocalized("column.ableToUsePrimaryHand")));
                generation.setAbleToUsesSecondaryHand(set.getBoolean(getLocalized("column.ableToUseSecondaryHand")));
                generation.setAbleToUseShield(set.getBoolean(getLocalized("column.ableToUseShield")));
                generation.setAbleToUseArmor(ArmorPosition.head, set.getBoolean(getLocalized("column.ableToUseHelmet")));
                generation.setAbleToUseArmor(ArmorPosition.upperBody, set.getBoolean(getLocalized("column.ableToUseHarness")));
                generation.setAbleToUseArmor(ArmorPosition.arm, set.getBoolean(getLocalized("column.ableToUseBracers")));
                generation.setAbleToUseArmor(ArmorPosition.legs, set.getBoolean(getLocalized("column.ableToUseLegArmor")));
                generation.setAbleToUseJewellery(set.getBoolean(getLocalized("column.ableToUseJewellery")));
                generation.setUsesAlwaysShield(set.getBoolean(getLocalized("column.usesAlwaysShield")));
                generation.setAbleToUseSpells(set.getBoolean(getLocalized("column.ableToUseSpells")));
                generation.setUsesExclusivelySpecificPrimaryWeapons(set.getBoolean(getLocalized("column.exclusivePrimaryHand")));
                generation.setUsesExclusivelySpecificSecondaryWeapons(set.getBoolean(getLocalized("column.exclusiveSecondaryHand")));
                generation.setUsesExclusivelySpecificArmor(ArmorPosition.head, set.getBoolean(getLocalized("column.exclusiveHelmet")));
                generation.setUsesExclusivelySpecificArmor(ArmorPosition.upperBody, set.getBoolean(getLocalized("column.exclusiveHarness")));
                generation.setUsesExclusivelySpecificArmor(ArmorPosition.arm, set.getBoolean(getLocalized("column.exclusiveBracers")));
                generation.setUsesExclusivelySpecificArmor(ArmorPosition.legs, set.getBoolean(getLocalized("column.exclusiveLegArmor")));
                generation.setUsesExclusivelySpecificJewellery(set.getBoolean(getLocalized("column.exclusiveJewellery")));

                list.add(generation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private static <Generation extends GenerationBase> void linkParents(Statement statement, Collection<Generation> generations) throws SQLException {
        try (ResultSet groupSet = statement.executeQuery(format("SELECT * FROM [%s]", "table.enemies.enemyGroups"))) {
            while (groupSet.next()) {
                String group = getString(groupSet, getLocalized("column.group"));
                String part = getString(groupSet, getLocalized("column.groupPart"));

                Optional<Generation> parent = generations.stream().filter(g -> g.getName().equalsIgnoreCase(group)).findFirst();
                Optional<Generation> child = generations.stream().filter(g -> g.getName().equalsIgnoreCase(part)).findFirst();

                if (parent.isPresent() && child.isPresent()) {
                    child.get().addParent(parent.get());
                }
            }
        }
    }

    private static <SubType extends GenerationBase, Generation extends TypedGenerationBase<SubType>>
    void linkSubTypes(Statement statement, Collection<Generation> mains, Collection<SubType> subs) throws SQLException {

        try (ResultSet subTypSet = statement.executeQuery(format("SELECT * FROM [%s]", "table.enemies.enemySubtypes"))) {
            while (subTypSet.next()) {
                String mainName = getString(subTypSet, getLocalized("column.mainType"));
                String subName = getString(subTypSet, getLocalized("column.subtype"));

                Optional<Generation> parent = mains.stream().filter(g -> g.getName().equalsIgnoreCase(mainName)).findFirst();
                Optional<SubType> child = subs.stream().filter(g -> g.getName().equalsIgnoreCase(subName)).findFirst();

                if (parent.isPresent() && child.isPresent()) {
                    parent.get().addSubType(child.get());
                }

            }
        }
    }

    private static void addMainTalents(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        try (ResultSet talentTypSet = statement.executeQuery(format("SELECT * FROM [%s]", "table.enemies.mainTalents"))) {
            while (talentTypSet.next()) {
                String name = getString(talentTypSet, getLocalized("column.name"));
                String talentName = getString(talentTypSet, getLocalized("column.talent"));

                GenerationBase base = combined.stream().filter(g -> g.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
                Talent talent = Database.getTalentWithoutDefault(talentName);
                base.addMainTalent(talent);
            }
        }
    }

    private static void addForbiddenTalents(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        try (ResultSet talentTypSet = statement.executeQuery(format("SELECT * FROM [%s]", "table.enemies.forbiddenTalents"))) {
            while (talentTypSet.next()) {
                String name = getString(talentTypSet, getLocalized("column.name"));
                String talentName = getString(talentTypSet, getLocalized("column.talent"));

                GenerationBase base = combined.stream().filter(g -> g.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
                Talent talent = Database.getTalentWithoutDefault(talentName);
                base.addForbiddenTalent(talent);
            }
        }
    }

    private static void addPrimaryAttributes(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            generationBase.setPrimaryAttributes(
                    getCollection(statement, format(
                            "SELECT * FROM [%s] WHERE %s=\"%s\"",
                            "table.enemies.primaryAttributes", "column.name", generationBase.getName()),
                            "column.attribute").stream()
                            .map(PrimaryAttribute::getPrimaryAttribute).collect(Collectors.toList()));
        }
    }

    private static void addSecondaryAttributes(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            generationBase.setSecondaryAttributes(
                    getCollection(statement, format(
                            "SELECT * FROM [%s] WHERE %s=\"%s\"",
                            "table.enemies.secondaryAttributes", "column.name", generationBase.getName()),
                            "column.attribute").stream()
                            .map(SecondaryAttribute::getSecondaryAttribute).collect(Collectors.toList()));
        }
    }

    private static void addPrimaryWeaponTypes(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            generationBase.setPrimaryWeaponTypes(
                    getCollection(statement, format(
                            "SELECT * FROM [%s] WHERE %s=\"%s\"",
                            "table.enemies.primaryWeaponType", "column.name", generationBase.getName()),
                            "column.weaponTyp"));
        }
    }

    private static void addSecondaryWeaponTypes(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            generationBase.setSecondaryWeaponTypes(getCollection(statement, format(
                    "SELECT * FROM [%s] WHERE %s=\"%s\"",
                    "table.enemies.secondaryWeaponType", "column.name", generationBase.getName()),
                    "column.weaponTyp"));
        }
    }

    private static void addSpecificPrimaryWeapons(Statement statement, Collection<GenerationBase> combined) throws SQLException {

        for (GenerationBase generationBase : combined) {
            Collection<String> weaponNames = getCollection(statement, format(
                    "SELECT * FROM [%s] WHERE %s=\"%s\"",
                    "table.enemies.equippedPrimaryWeapon", "column.name", generationBase.getName()),
                    "type.weapon");

            Collection<Weapon> weapons = new ArrayList<>();
            for (String weaponName : weaponNames) {
                weapons.addAll(loadSpecificWeapons(statement, weaponName));
            }
            generationBase.setSpecificPrimaryWeapons(weapons);
        }
    }

    private static void addSpecificSecondaryWeapons(Statement statement, Collection<GenerationBase> combined) throws SQLException {

        for (GenerationBase generationBase : combined) {
            Collection<String> weaponNames = getCollection(statement, format(
                    "SELECT * FROM [%s] WHERE %s=\"%s\"",
                    "table.enemies.equippedSecondaryWeapon", "column.name", generationBase.getName()),
                    "type.weapon");

            Collection<Weapon> weapons = new ArrayList<>();
            for (String weaponName : weaponNames) {
                weapons.addAll(loadSpecificWeapons(statement, weaponName));
            }
            generationBase.setSpecificSecondaryWeapons(weapons);
        }
    }

    private static Collection<Weapon> loadSpecificWeapons(Statement statement, String weaponName) throws SQLException {
        Collection<Weapon> weapons = new ArrayList<>();

        try (ResultSet weaponSet = statement.executeQuery(format(
                "SELECT * FROM [%s] WHERE %s=\"%s\"",
                "table.enemies.specificWeaponStats", "column.name", weaponName))) {
            while (weaponSet.next()) {
                Weapon weapon = new Weapon();
                weapon.setName(weaponName);
                weapon.setType(getLocalized("type.weapon"));
                weapon.setSubtype(getString(weaponSet, getLocalized("column.weaponTyp")));
                weapon.setTier(weaponSet.getInt(getLocalized("column.tier")));
                weapon.setRarity(getRarity(weaponSet, getLocalized("column.rarity")));
                weapon.setInitiative(getFloat(weaponSet, getLocalized("column.initiative")));
                weapon.setInitiativeModifier(getFloat(weaponSet, getLocalized("column.initiativeModifier")));
                weapon.setDice(getString(weaponSet, getLocalized("column.dice_weight")));
                weapon.setDamage(weaponSet.getInt(getLocalized("column.damage_protection")));
                weapon.setHit(weaponSet.getInt(getLocalized("column.hit")));
                weapon.setEffect(getString(weaponSet, getLocalized("column.effect")));

                weapons.add(weapon);
            }
        }
        return weapons;
    }

    private static void addSpecificArmor(Statement statement, Collection<GenerationBase> combined) throws SQLException {

        for (GenerationBase generationBase : combined) {
            try (ResultSet eqSet = statement.executeQuery(format(
                    "SELECT * FROM [%s] WHERE %s=\"%s\"",
                    "table.enemies.equippedArmor", "column.name", generationBase.getName()))) {
                Map<ArmorPosition, Collection<Armor>> armor = new HashMap<>();
                for (ArmorPosition position : ArmorPosition.values()) {
                    armor.put(position, new ArrayList<>());
                }

                while (eqSet.next()) {
                    String armorName = getString(eqSet, getLocalized("column.armor"));
                    ArmorPosition position = ArmorPosition.getArmorPosition(getString(eqSet, getLocalized("column.armorType")));

                    armor.get(position).addAll(loadSpecificArmor(statement, armorName, position));
                }

                for (ArmorPosition position : ArmorPosition.values()) {
                    generationBase.setSpecificArmor(position, armor.get(position));
                }
            }
        }
    }

    private static Collection<Armor> loadSpecificArmor(Statement statement, String armorName, ArmorPosition position) throws SQLException {
        Collection<Armor> armorList = new ArrayList<>();
        String positionName = position.toStringProperty().get();

        try (ResultSet armorSet = statement.executeQuery(format(
                "SELECT * FROM [%s] WHERE (%s=\"%s\") AND (%s=\"%s\")",
                "table.enemies.armor", "column.name", armorName, "column.armorTyp", positionName))) {
            while (armorSet.next()) {
                Armor armor = new Armor();
                armor.setName(armorName);
                armor.setType(getLocalized("type.armor"));
                armor.setSubtype(positionName);
                armor.setTier(armorSet.getInt(getLocalized("column.tier")));
                armor.setRarity(getRarity(armorSet, getLocalized("column.rarity")));
                armor.setProtection(armorSet.getInt(getLocalized("column.protection")));
                armor.setWeight(armorSet.getDouble(getLocalized("column.weight")));
                armor.setEffect(getString(armorSet, getLocalized("column.effect")));

                armorList.add(armor);
            }
        }
        return armorList;
    }

    private static void addSpecificJewellery(Statement statement, Collection<GenerationBase> combined) throws SQLException {

        for (GenerationBase generationBase : combined) {
            Collection<String> jewelleryNames = getCollection(statement, format(
                    "SELECT * FROM [%s] WHERE %s=\"%s\"",
                    "table.enemies.equippedJewellery", "column.name", generationBase.getName()),
                    "type.jewellery");

            Collection<Jewellery> jewelleries = new ArrayList<>();
            for (String jewelleryName : jewelleryNames) {
                jewelleries.addAll(loadSpecificJewellery(statement, jewelleryName));
            }
            generationBase.setSpecificJewellery(jewelleries);
        }
    }

    private static Collection<Jewellery> loadSpecificJewellery(Statement statement, String jewelleryName) throws SQLException {
        Collection<Jewellery> jewelleries = new ArrayList<>();

        try (ResultSet jewellerySet = statement.executeQuery(format(
                "SELECT * FROM [%s] WHERE %s=\"%s\"",
                "table.enemies.specificJewelleryStats", "column.name", jewelleryName))) {
            while (jewellerySet.next()) {
                Jewellery jewellery = new Jewellery();
                jewellery.setName(jewelleryName);
                jewellery.setType(getLocalized("type.jewellery"));
                jewellery.setSubtype(getString(jewellerySet, getLocalized("column.jewelleryTyp")));
                jewellery.setTier(jewellerySet.getInt(getLocalized("column.tier")));
                jewellery.setRarity(getRarity(jewellerySet, getLocalized("column.rarity")));
                jewellery.setEffect(getString(jewellerySet, getLocalized("column.effect")));

                jewelleries.add(jewellery);
            }
        }
        return jewelleries;
    }

    private static void addDrops(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            try (ResultSet dropSet = statement.executeQuery(format(
                    "SELECT * FROM %s WHERE %s=\"%s\"",
                    "table.enemies.drop", "column.name", generationBase.getName()))) {
                Collection<Drop> drops = new ArrayList<>();

                while (dropSet.next()) {
                    Drop drop = new Drop();
                    drop.setName(getString(dropSet, getLocalized("column.drop")));
                    drop.setChance(dropSet.getFloat(getLocalized("column.chance")));
                    drop.setBaseAmount(dropSet.getInt(getLocalized("column.baseAmount")));
                    drop.setMultiplicativeAmount(dropSet.getInt(getLocalized("column.multiplicativeAmount")));
                    drop.setLevelMultiplication(dropSet.getFloat(getLocalized("column.levelMultiplication")));
                    drop.setTierMultiplication(dropSet.getFloat(getLocalized("column.tierMultiplication")));
                    drop.setMinLevel(dropSet.getInt(getLocalized("column.minLevel")));
                    drop.setMinTier(dropSet.getInt(getLocalized("column.minTier")));
                    drop.setMaxLevel(dropSet.getInt(getLocalized("column.maxLevel")));
                    drop.setMaxTier(dropSet.getInt(getLocalized("column.maxTier")));

                    drops.add(drop);
                }

                generationBase.setDrops(drops);
            }
        }
    }

    private static void checkInconsistencies() {
        ObservableList<Inconsistency> inconsistencyList = FXCollections.observableArrayList();

        for (UpgradeFactory upgrade : Database.upgradeList) {

            for (int level = 1; level <= upgrade.getMaxLevel(); level++) {

                ICurrency currency = new Currency(0);

                for (IItem item : upgrade.getMaterials(level)) {
                    if (item != null) {
                        currency = currency.add(item.getCurrencyWithAmount());
                    }
                }

                ICurrency actualCurrency = upgrade.getCurrency(level);

                if (currency.getCoinValue() > actualCurrency.getCoinValue()) {
                    Inconsistency inconsistency = new Inconsistency();
                    inconsistency.setName(upgrade.getName());
                    inconsistency.setInconsistency(currency.getCoinString() + " > " + actualCurrency.getCoinString());

                    ArrayList<String> information = new ArrayList<>();
                    for (IItem material : upgrade.getMaterials(level)) {
                        if (material != null) {
                            String matCost = material.getCurrencyWithAmount().getCoinString();
                            information.add(material.getPrettyAmount() + " " + material + " (" + matCost + ")");
                        }
                    }
                    inconsistency.setInfo(information);

                    inconsistencyList.add(inconsistency);
                }
            }
        }

        for (Fabrication fabrication : Database.fabricationList) {

            if (fabrication.getProduct().isTradeable()) {
                ICurrency currency = new Currency(0);

                for (IItem item : fabrication.getMaterials()) {
                    if (item != null) {
                        currency = currency.add(item.getCurrencyWithAmount());
                    }
                }

                currency = currency.divide(fabrication.getProductAmount());

                ICurrency actualCurrency = fabrication.getProduct().getCurrency().multiply(fabrication.getProductAmount());
                if (currency.getCoinValue() > actualCurrency.getCoinValue()) {
                    Inconsistency inconsistency = new Inconsistency();
                    inconsistency.setName(fabrication.getProduct().getName());
                    inconsistency.setInconsistency(currency.getCoinString()
                            + " > " + actualCurrency.getCoinString());

                    ArrayList<String> information = new ArrayList<>();
                    for (IItem material : fabrication.getMaterials()) {
                        if (material != null) {
                            String matCost = material.getCurrency().getCoinString();
                            information.add(material.getAmount() + " " + material + " (" + matCost + ")");
                        }
                    }
                    inconsistency.setInfo(information);

                    inconsistencyList.add(inconsistency);
                }
            }
        }
        if (loadSynchronous) {
            Database.inconsistencyList.set(inconsistencyList);
        } else {
            Platform.runLater(() -> Database.inconsistencyList.set(inconsistencyList));
        }
    }

    private static Talent getTalent(String typ) {
        return Database.getTalent(talentTypes.getOrDefault(typ, typ));
    }

    private static UpgradeFactory getUpgradeFactory(String name, Collection<UpgradeFactory> collection) {
        for (UpgradeFactory upgradeFactory : collection) {
            if (upgradeFactory.getName().equals(name)) {
                return upgradeFactory;
            }
        }
        return null;
    }

    private static Rarity getRarity(ResultSet resultSet, String label) throws SQLException {
        return Rarity.getRarity(getString(resultSet, label));
    }

    private static String getString(ResultSet resultSet, String label) throws SQLException {
        String string = resultSet.getString(label);
        return string != null ? string : "";
    }

    private static float getFloat(ResultSet resultSet, String label) throws SQLException {
        return resultSet.getFloat(label);
    }

    private static Collection<String> getCollection(Statement statement, @org.intellij.lang.annotations.Language("SQL") String sql, String label) throws SQLException {
        ArrayList<String> collection = new ArrayList<>();
        try (ResultSet set = statement.executeQuery(sql)) {
            while (set.next()) {
                collection.add(getString(set, getLocalized(label)));
            }
        }
        return collection;
    }

    private static String getErrorString(String table) {
        return LanguageUtility.getMessage("database.cantGetLoaded") + " " +
                getLocalized(table) + " " +
                LanguageUtility.getMessage("database.cantGetLoaded.ending");
    }

    private static String getLocalized(String key) {
        if (tables.get().containsKey(key)) {
            return tables.get().getString(key);
        } else {
            return key;
        }
    }

    private static String format(@org.intellij.lang.annotations.Language("SQL") String sql, String... keys) {
        keys = Arrays.stream(keys).map(DatabaseLoader::getLocalized).toArray(String[]::new);

        return String.format(sql, (Object[]) keys);
    }

    private static void reloadLanguage(Language language) {
        try {
            if (language == Language.system) {
                tables.set(ResourceBundle.getBundle("table/Table", LanguageUtility.language.get().getLocale()));
            } else {
                tables.set(ResourceBundle.getBundle("table/Table", language.getLocale()));
            }
        } catch (MissingResourceException e) {
            tables.set(ResourceBundle.getBundle("table/Table", Locale.ENGLISH));
        }
    }
}
