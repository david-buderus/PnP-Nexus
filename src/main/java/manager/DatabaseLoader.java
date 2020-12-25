package manager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.item.*;
import model.loot.DungeonLootFactory;
import model.upgrade.UpgradeFactory;
import net.ucanaccess.complex.SingleValue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public abstract class DatabaseLoader {

    public static Collection<String> loadDatabase(Connection connection) throws SQLException {
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
                TypTranslation.addStandards();

                semaphore.acquire();
                info.add(loadSpells(statement, semaphore));
                info.add(loadUpgrades(statement, semaphore));
                info.add(loadDungeonLoot(statement, semaphore));
                info.add(loadEvents(statement, semaphore));
                info.add(loadCraftingBoni(statement, semaphore));
                info.add(loadFabrication(statement, semaphore));

                semaphore.acquire(6);
                checkInconsistencies();

            } catch (InterruptedException ignored) {
            }
        }

        return info.stream().filter(s -> !s.isBlank()).collect(Collectors.toList());
    }

    private static String loadWeapons(Statement statement, Semaphore semaphore) {
        try (ResultSet weaponSet = statement.executeQuery("SELECT * FROM Items NATURAL JOIN Waffen")) {
            ObservableList<Weapon> weaponList = FXCollections.observableArrayList();
            while (weaponSet.next()) {
                Weapon weapon = new Weapon();
                weapon.setName(getString(weaponSet, "Bezeichnung"));
                weapon.setMaterial(getString(weaponSet, "Material"));
                weapon.setTyp(getString(weaponSet, "Typ"));
                weapon.setSubTyp(getString(weaponSet, "Subtyp"));
                weapon.setRequirement(getString(weaponSet, "Vorraussetzung"));
                weapon.setInitiative(getString(weaponSet, "Initiative"));
                weapon.setDice(getString(weaponSet, "Würfel/Belastung"));
                weapon.setDamage(weaponSet.getInt("Schaden/Schutz"));
                weapon.setHit(weaponSet.getInt("Treffer"));
                weapon.setEffect(getString(weaponSet, "Effekt"));
                weapon.setSlots(weaponSet.getInt("Verbesserungsslots"));
                weapon.setRarity(getString(weaponSet, "Seltenheit"));
                weapon.setCost(getString(weaponSet, "Preis"));
                weapon.setTier(weaponSet.getInt("Tier"));

                weaponList.add(weapon);

                TypTranslation.add(weapon.getTyp(), "Waffe");
            }

            Platform.runLater(() -> {
                Database.weaponList.set(weaponList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Waffen konnten nicht geladen werden.";
        }
        return "";
    }

    private static String loadArmor(Statement statement, Semaphore semaphore) {
        try (ResultSet armorSet = statement.executeQuery("SELECT * FROM Items NATURAL JOIN Rüstungen")) {
            ObservableList<Armor> armorList = FXCollections.observableArrayList();

            while (armorSet.next()) {
                Armor armor = new Armor();
                armor.setName(getString(armorSet, "Bezeichnung"));
                armor.setMaterial(getString(armorSet, "Material"));
                armor.setTyp(getString(armorSet, "Typ"));
                armor.setSubTyp(getString(armorSet, "Subtyp"));
                armor.setRequirement(getString(armorSet, "Vorraussetzung"));
                armor.setProtection(armorSet.getInt("Schutz"));
                armor.setWeight(armorSet.getDouble("Belastung"));
                armor.setEffect(getString(armorSet, "Effekt"));
                armor.setSlots(armorSet.getInt("Verbesserungsslots"));
                armor.setRarity(getString(armorSet, "Seltenheit"));
                armor.setCost(getString(armorSet, "Preis"));
                armor.setTier(armorSet.getInt("Tier"));

                armorList.add(armor);

                TypTranslation.add(armor.getTyp(), "Rüstung");
            }

            Platform.runLater(() -> {
                Database.armorList.set(armorList);
                semaphore.release();
            });
        } catch (SQLException e) {
            return "Rüstungen konnten nicht geladen werden.";
        }
        return "";
    }

    private static String loadJewellery(Statement statement, Semaphore semaphore) {
        try (ResultSet jewellerySet = statement.executeQuery("SELECT * FROM Items NATURAL JOIN Schmuck")) {
            ObservableList<Jewellery> jewelleryList = FXCollections.observableArrayList();

            while (jewellerySet.next()) {
                Jewellery jewellery = new Jewellery();
                jewellery.setName(getString(jewellerySet, "Bezeichnung"));
                jewellery.setMaterial(getString(jewellerySet, "Material"));
                jewellery.setGem(getString(jewellerySet, "Edelstein"));
                jewellery.setTyp(getString(jewellerySet, "Typ"));
                jewellery.setSubTyp(getString(jewellerySet, "Subtyp"));
                jewellery.setRequirement(getString(jewellerySet, "Vorraussetzung"));
                jewellery.setEffect(getString(jewellerySet, "Effekt"));
                jewellery.setSlots(jewellerySet.getInt("Verbesserungsslots"));
                jewellery.setRarity(getString(jewellerySet, "Seltenheit"));
                jewellery.setCost(getString(jewellerySet, "Preis"));
                jewellery.setTier(jewellerySet.getInt("Tier"));

                jewelleryList.add(jewellery);

                TypTranslation.add(jewellery.getTyp(), "Schmuck");
            }

            Platform.runLater(() -> {
                Database.jewelleryList.set(jewelleryList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Schmuck konnte nicht geladen werden.";
        }
        return "";
    }

    private static String loadPlants(Statement statement, Semaphore semaphore) {
        try (ResultSet plantSet = statement.executeQuery("SELECT * FROM Items WHERE Typ=\"Pflanze\"")) {
            ObservableList<Plant> plantList = FXCollections.observableArrayList();

            while (plantSet.next()) {
                Plant plant = new Plant();
                plant.setName(getString(plantSet, "Bezeichnung"));
                plant.setTyp(getString(plantSet, "Typ"));
                plant.setSubTyp(getString(plantSet, "Subtyp"));
                plant.setEffect(getString(plantSet, "Effekt"));
                plant.setRarity(getString(plantSet, "Seltenheit"));
                plant.setCost(getString(plantSet, "Preis"));
                plant.setTier(plantSet.getInt("Tier"));
                plant.setLocations(getCollection(statement,
                        "SELECT Fundort FROM Fundorte WHERE Bezeichnung=\"" + plant.getName() + "\"", "Fundort"));

                plantList.add(plant);
            }

            Platform.runLater(() -> {
                Database.plantList.set(plantList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Pflanzen konnten nicht geladen werden.";
        }
        return "";
    }

    private static String loadItems(Statement statement, Semaphore semaphore) {
        try (ResultSet itemSet = statement.executeQuery("SELECT * FROM Items WHERE Typ NOT IN (\"Waffe\", \"Rüstung\", \"Schmuck\", \"Pflanze\")")) {
            ObservableList<Item> itemList = FXCollections.observableArrayList();

            while (itemSet.next()) {
                Item item = new Item();
                item.setTyp(getString(itemSet, "Typ"));
                item.setName(getString(itemSet, "Bezeichnung"));
                item.setSubTyp(getString(itemSet, "Subtyp"));
                item.setEffect(getString(itemSet, "Effekt"));
                item.setRarity(getString(itemSet, "Seltenheit"));
                item.setCost(getString(itemSet, "Preis"));
                item.setTier(itemSet.getInt("Tier"));

                itemList.add(item);
            }
            Platform.runLater(() -> {
                Database.itemList.set(itemList);
                Database.itemList.addAll(Database.weaponList);
                Database.itemList.addAll(Database.armorList);
                Database.itemList.addAll(Database.jewelleryList);
                Database.itemList.addAll(Database.plantList);
                semaphore.release();
            });

        } catch (SQLException e) {
            e.printStackTrace();
            return "Items konnten nicht geladen werden.";
        }
        return "";
    }

    private static String loadSpells(Statement statement, Semaphore semaphore) {
        try (ResultSet spellSet = statement.executeQuery("SELECT * FROM Zauber")) {
            ObservableList<Spell> spellList = FXCollections.observableArrayList();

            while (spellSet.next()) {
                Spell spell = new Spell();
                spell.setName(getString(spellSet, "Bezeichnung"));
                spell.setEffect(getString(spellSet, "Effekt"));
                spell.setTyp(Arrays.stream((SingleValue[]) spellSet.getObject("Typ")).
                        map(val -> (String) val.getValue()).collect(Collectors.joining(",")));
                spell.setCost(getString(spellSet, "Kosten"));
                spell.setCastTime(getString(spellSet, "Zauberzeit"));
                spell.setTier(spellSet.getInt("Tier"));

                spellList.add(spell);
            }

            Platform.runLater(() -> {
                Database.spellList.set(spellList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Zauber konnten nicht geladen werden.";
        }
        return "";
    }

    private static String loadUpgrades(Statement statement, Semaphore semaphore) {
        try (ResultSet upgradeSet = statement.executeQuery("SELECT * FROM Verbesserungen")) {
            ObservableList<UpgradeFactory> upgradeList = FXCollections.observableArrayList();

            while (upgradeSet.next()) {

                String name = getString(upgradeSet, "Bezeichnung");
                int level = upgradeSet.getInt("Stufe");

                UpgradeFactory exist = getUpgradeFactory(name, upgradeList);

                if (exist != null) {
                    if (exist.getMaxLevel() < level) {
                        exist.setMaxLevel(level);
                    }
                    exist.setEffect(level, getString(upgradeSet, "Effekt"));
                    exist.setCost(level, getString(upgradeSet, "Preis"));
                    exist.setMana(level, getString(upgradeSet, "Mana"));
                    exist.setRequirement(level, getString(upgradeSet, "Benötigt"));

                    ItemList materials = new ItemList();
                    ResultSet materialSet = statement.executeQuery("SELECT * FROM Verbesserungsmaterial " +
                            "WHERE Bezeichnung=\"" + name + "\" AND Stufe=" + level);
                    while (materialSet.next()) {
                        String mat = getString(materialSet, "Material");
                        float amount = materialSet.getFloat("Anzahl");
                        Item material = Database.getItemWithoutDefault(mat).copy();
                        material.setAmount(amount);
                        materials.add(material);
                    }
                    exist.setMaterials(level, materials);

                } else {
                    UpgradeFactory upgradeFactory = new UpgradeFactory();
                    upgradeFactory.setName(name);
                    upgradeFactory.setMaxLevel(level);
                    upgradeFactory.setTarget(getString(upgradeSet, "Ziel"));
                    upgradeFactory.setSlots(upgradeSet.getInt("Slots"));
                    upgradeFactory.setRequirement(level, getString(upgradeSet, "Benötigt"));
                    upgradeFactory.setEffect(level, getString(upgradeSet, "Effekt"));
                    upgradeFactory.setCost(level, getString(upgradeSet, "Preis"));
                    upgradeFactory.setMana(level, getString(upgradeSet, "Mana"));

                    ItemList materials = new ItemList();
                    ResultSet materialSet = statement.executeQuery("SELECT * FROM Verbesserungsmaterial " +
                            "WHERE Bezeichnung=\"" + name + "\" AND Stufe=" + level);
                    while (materialSet.next()) {
                        String mat = getString(materialSet, "Material");
                        float amount = materialSet.getFloat("Anzahl");
                        Item material = Database.getItemWithoutDefault(mat).copy();
                        material.setAmount(amount);
                        materials.add(material);
                    }
                    upgradeFactory.setMaterials(level, materials);

                    upgradeList.add(upgradeFactory);
                }
            }

            Platform.runLater(() -> {
                Database.upgradeList.set(upgradeList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Verbesserungen konnten nicht geladen werden.";
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return "Verbesserungen konnten nicht geladen werden. Ein Material existierte nicht.";
        }
        return "";
    }

    private static String loadDungeonLoot(Statement statement, Semaphore semaphore) {
        try (ResultSet lootSet = statement.executeQuery("SELECT * FROM Loot")) {
            ObservableList<DungeonLootFactory> lootList = FXCollections.observableArrayList();

            while (lootSet.next()) {
                DungeonLootFactory factory = new DungeonLootFactory();
                String name = getString(lootSet, "Gegenstand");
                factory.setName(name.equals("Allgemeiner Gegenstand") ? getString(lootSet, "Info") : name);
                factory.setContainer(getString(lootSet, "Behälter"));
                factory.setPlace(getString(lootSet, "Ort"));
                factory.setChance(lootSet.getDouble("Wahrscheinlichkeit"));
                factory.setMaxAmount(lootSet.getInt("Anzahl"));

                lootList.add(factory);
            }

            Platform.runLater(() -> {
                Database.dungeonLootList.set(lootList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Loot konnte nicht geladen werden.";
        }
        return "";
    }

    private static String loadEvents(Statement statement, Semaphore semaphore) {
        try (ResultSet eventSet = statement.executeQuery("SELECT * FROM Events")) {
            ObservableList<Event> eventList = FXCollections.observableArrayList();

            while (eventSet.next()) {
                Event event = new Event();
                event.setName(getString(eventSet, "Bezeichnung"));
                event.setTyp(getString(eventSet, "Typ"));
                event.setInfo(getString(eventSet, "Info"));
                event.setTrigger(getString(eventSet, "Auslöser"));
                event.setChance(eventSet.getDouble("Wahrscheinlichkeit"));
                event.setContinents(Arrays.stream(getString(eventSet, "Kontinent").split(",")).map(String::trim).collect(Collectors.toList()));
                event.setLands(Arrays.stream(getString(eventSet, "Land").split(",")).map(String::trim).collect(Collectors.toList()));
                event.setLocations(Arrays.stream(getString(eventSet, "Gebiet").split(",")).map(String::trim).collect(Collectors.toList()));

                eventList.add(event);
            }

            Platform.runLater(() -> {
                Database.eventList.set(eventList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Events konnten nicht geladen werden.";
        }
        return "";
    }

    private static String loadCraftingBoni(Statement statement, Semaphore semaphore) {
        try (ResultSet eventSet = statement.executeQuery("SELECT * FROM Herstellungsverbesserungen")) {
            ObservableList<CraftingBonus> craftingBonusList = FXCollections.observableArrayList();

            while (eventSet.next()) {
                CraftingBonus craftingBonus = new CraftingBonus();
                craftingBonus.setName(getString(eventSet, "Bezeichnung"));
                craftingBonus.setTarget(getString(eventSet, "Ziel"));
                craftingBonus.setEffect(getString(eventSet, "Effekt"));

                craftingBonusList.add(craftingBonus);
            }

            Platform.runLater(() -> {
                Database.craftingBonusList.set(craftingBonusList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Herstellungsverbesserungen konnten nicht geladen werden.";
        }
        return "";
    }

    private static String loadFabrication(Statement statement, Semaphore semaphore) {
        try (ResultSet fabricationSet = statement.executeQuery("SELECT * FROM Herstellung")) {
            ObservableList<Fabrication> fabricationList = FXCollections.observableArrayList();

            while (fabricationSet.next()) {
                Fabrication fabrication = new Fabrication();
                fabrication.setProductName(getString(fabricationSet, "Bezeichnung"));
                fabrication.setProfession(getString(fabricationSet, "Beruf"));
                fabrication.setRequirement(getString(fabricationSet, "Benötigte Fähigkeiten"));
                fabrication.setOtherCircumstances(getString(fabricationSet, "Sonstige Umstände"));
                fabrication.setProductAmount(fabricationSet.getInt("Hergestellte Menge"));
                fabrication.setSideProductAmount(fabricationSet.getInt("Anzahl  Neben"));
                fabrication.setSideProductName(getString(fabricationSet, "Nebenprodukt"));

                int id = fabricationSet.getInt("ID");

                ItemList materials = new ItemList();
                ResultSet materialSet = statement.executeQuery("SELECT * FROM Herstellungsmaterial WHERE ID=" + id);
                while (materialSet.next()) {
                    String mat = getString(materialSet, "Material");
                    float amount = materialSet.getFloat("Anzahl");
                    Item material = Database.getItemWithoutDefault(mat).copy();
                    material.setAmount(amount);
                    materials.add(material);
                }
                fabrication.setMaterials(materials);

                fabricationList.add(fabrication);
            }

            Platform.runLater(() -> {
                Database.fabricationList.set(fabricationList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Herstellungen konnten nicht geladen werden.";
        }
        return "";
    }

    private static void checkInconsistencies() {
        ObservableList<Inconsistency> inconsistencyList = FXCollections.observableArrayList();

        for (UpgradeFactory upgrade : Database.upgradeList) {

            for (int level = 1; level <= upgrade.getMaxLevel(); level++) {

                int cost = 0;

                for (Item item : upgrade.getMaterials(level)) {
                    if (item != null) {
                        cost += item.getCostAsCopper();
                    }
                }

                int actualCost = upgrade.getCostAsCopper(level);
                if (cost > actualCost) {
                    Inconsistency inconsistency = new Inconsistency();
                    inconsistency.setName(upgrade.getName());
                    inconsistency.setInconsistency(Utility.visualiseSell(cost) + " > " + Utility.visualiseSell(actualCost));

                    ArrayList<String> information = new ArrayList<>();
                    for (Item material : upgrade.getMaterials(level)) {
                        if (material != null) {
                            String matCost = Utility.visualiseSell(Math.round(material.getCostAsCopper()));
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
                int cost = 0;

                for (Item item : fabrication.getMaterials()) {
                    if (item != null) {
                        cost += item.getCostAsCopper();
                    }
                }

                cost /= fabrication.getProductAmount();

                int actualCost = fabrication.getProduct().getCostOfOneAsCopper() * fabrication.getProductAmount();
                if (cost > actualCost) {
                    Inconsistency inconsistency = new Inconsistency();
                    inconsistency.setName(fabrication.getProduct().getName());
                    inconsistency.setInconsistency(Utility.visualiseSell(cost)
                            + " > " + Utility.visualiseSell(actualCost));

                    ArrayList<String> information = new ArrayList<>();
                    for (Item material : fabrication.getMaterials()) {
                        if (material != null) {
                            String matCost = Utility.visualiseSell(Math.round(material.getCostAsCopper()));
                            information.add(material.getAmount() + " " + material + " (" + matCost + ")");
                        }
                    }
                    inconsistency.setInfo(information);

                    inconsistencyList.add(inconsistency);
                }
            }
        }
        Platform.runLater(() -> Database.inconsistencyList.set(inconsistencyList));
    }

    private static UpgradeFactory getUpgradeFactory(String name, Collection<UpgradeFactory> collection) {
        for (UpgradeFactory upgradeFactory : collection) {
            if (upgradeFactory.getName().equals(name)) {
                return upgradeFactory;
            }
        }
        return null;
    }

    private static String getString(ResultSet resultSet, String label) throws SQLException {
        String string = resultSet.getString(label);
        return string != null ? string : "";
    }

    private static Collection<String> getCollection(Statement statement, String sql, String label) throws SQLException {
        ArrayList<String> collection = new ArrayList<>();
        ResultSet set = statement.executeQuery(sql);
        while (set.next()) {
            collection.add(getString(set, label));
        }
        return collection;
    }
}
