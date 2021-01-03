package manager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.item.*;
import model.loot.DungeonLootFactory;
import model.member.generation.*;
import model.member.generation.specs.*;
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
                info.add(loadTalents(statement, semaphore));
                TypTranslation.addStandards();

                semaphore.acquire(2);
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
                weapon.setRarity(getRarity(weaponSet, "Seltenheit"));
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
                armor.setRarity(getRarity(armorSet, "Seltenheit"));
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
                jewellery.setRarity(getRarity(jewellerySet, "Seltenheit"));
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
                plant.setRarity(getRarity(plantSet, "Seltenheit"));
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
                item.setRarity(getRarity(itemSet, "Seltenheit"));
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

    private static String loadTalents(Statement statement, Semaphore semaphore) {
        try (ResultSet talentSet = statement.executeQuery("SELECT * FROM Talente")) {
            ObservableList<Talent> talentList = FXCollections.observableArrayList();

            while (talentSet.next()) {
                Talent talent = new Talent();
                talent.setName(getString(talentSet, "Bezeichnung"));
                talent.setAttributes(new PrimaryAttribute[]{
                        PrimaryAttribute.getPrimaryAttribute(getString(talentSet, "Attribut 1")),
                        PrimaryAttribute.getPrimaryAttribute(getString(talentSet, "Attribut 2")),
                        PrimaryAttribute.getPrimaryAttribute(getString(talentSet, "Attribut 3"))
                });
                talent.setMagicTalent(talentSet.getBoolean("Magietalent"));
                talent.setWeaponTalent(talentSet.getBoolean("Waffentalent"));

                talentList.add(talent);
            }

            Platform.runLater(() -> {
                Database.talentList.set(talentList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Talente konnten nicht geladen werden.";
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
                spell.setTalent(getTalent(spell.getTyp()));

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

    private static String loadShieldTypes(Statement statement, Semaphore semaphore) {
        try {
            ObservableList<String> shieldTypList =
                    FXCollections.observableArrayList(getCollection(statement, "SELECT * FROM Schildtypen", "Schildtyp"));

            Platform.runLater(() -> {
                Database.shieldTypes.set(shieldTypList);
                semaphore.release();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Schildtypen konnten nicht geladen werden.";
        }
        return "";
    }

    private static String loadEnemies(Statement statement, Semaphore semaphore) {
        ObservableList<Characterisation> characterisationList;
        ObservableList<Race> raceList;
        ObservableList<Profession> professionList;
        ObservableList<FightingStyle> fightingStyleList;
        ObservableList<Specialisation> specialisationList;

        // Load raw model from database
        try (ResultSet enemySet = statement.executeQuery("SELECT * FROM Gegner WHERE TYP=\"Charakterisierung\"")) {
            characterisationList = loadEnemies(enemySet, Characterisation::new);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Charakterisierung konnte nicht geladen werden.";
        }
        try (ResultSet enemySet = statement.executeQuery("SELECT * FROM Gegner WHERE TYP=\"Rasse\"")) {
            raceList = loadEnemies(enemySet, Race::new);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Rassen konnten nicht geladen werden.";
        }
        try (ResultSet enemySet = statement.executeQuery("SELECT * FROM Gegner WHERE TYP=\"Beruf\"")) {
            professionList = loadEnemies(enemySet, Profession::new);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Berufe konnten nicht geladen werden.";
        }
        try (ResultSet enemySet = statement.executeQuery("SELECT * FROM Gegner WHERE TYP=\"Kampfstil\"")) {
            fightingStyleList = loadEnemies(enemySet, FightingStyle::new);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Kampfstile konnten nicht geladen werden.";
        }
        try (ResultSet enemySet = statement.executeQuery("SELECT * FROM Gegner WHERE TYP=\"Spezialisierung\"")) {
            specialisationList = loadEnemies(enemySet, Specialisation::new);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Spezialisierungen konnten nicht geladen werden.";
        }

        // Link parents
        try {
            linkParents(statement, characterisationList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Charakterisierungen konnten nicht gruppiert werden.";
        }
        try {
            linkParents(statement, raceList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Rassen konnten nicht gruppiert werden.";
        }
        try {
            linkParents(statement, professionList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Berufe konnten nicht gruppiert werden.";
        }
        try {
            linkParents(statement, fightingStyleList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Kampfstile konnten nicht gruppiert werden.";
        }
        try {
            linkParents(statement, specialisationList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Spezialisierungen konnten nicht gruppiert werden.";
        }

        // Link subtypes
        try {
            linkSubTypes(statement, characterisationList, raceList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Charakterisierungen konnten nicht mit SubTypen gelinkt werden.";
        }
        try {
            linkSubTypes(statement, raceList, professionList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Rassen konnten nicht mit SubTypen gelinkt werden.";
        }
        try {
            linkSubTypes(statement, professionList, fightingStyleList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Berufe konnten nicht mit SubTypen gelinkt werden.";
        }
        try {
            linkSubTypes(statement, fightingStyleList, specialisationList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Kampfstile konnten nicht mit SubTypen gelinkt werden.";
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
            e.printStackTrace();
            return "Haupttalente konnten nicht gesetzt werden.";
        }
        try {
            addForbiddenTalents(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Verbotene Talente konnten nicht gesetzt werden.";
        }

        // Add Attributes
        try {
            addPrimaryAttributes(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Primäre Attribute konnten nicht gesetzt werden.";
        }
        try {
            addSecondaryAttributes(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Sekundäre Attribute konnten nicht gesetzt werden.";
        }

        // Add Weapon Types
        try {
            addPrimaryWeaponTypes(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Primäre Waffentypen konnten nicht gesetzt werden.";
        }
        try {
            addSecondaryWeaponTypes(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Sekundäre Waffentypen konnten nicht gesetzt werden.";
        }

        // Add specific equipment
        try {
            addSpecificPrimaryWeapons(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Primäre Waffen konnten nicht gesetzt werden.";
        }
        try {
            addSpecificSecondaryWeapons(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Sekundäre Waffen konnten nicht gesetzt werden.";
        }
        try {
            addSpecificArmor(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Rüstungen konnten nicht gesetzt werden.";
        }

        // Add drops
        try {
            addDrops(statement, combinedList);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Drops konnten nicht gesetzt werden.";
        }

        Platform.runLater(() -> {
            Database.characterisationList.set(characterisationList);
            Database.raceList.set(raceList);
            Database.professionList.set(professionList);
            Database.fightingStyleList.set(fightingStyleList);
            Database.specialisationList.set(specialisationList);
            semaphore.release();
        });
        return "";
    }

    private static <Generation extends GenerationBase> ObservableList<Generation> loadEnemies(ResultSet set, Supplier<Generation> constructor) throws SQLException {
        ObservableList<Generation> list = FXCollections.observableArrayList();

        while (set.next()) {
            try {
                Generation generation = constructor.get();
                generation.setName(getString(set, "Bezeichnung"));
                generation.setAdvantages(Arrays.stream(getString(set, "Vorteile").split("\n"))
                        .filter(s -> !s.isBlank()).collect(Collectors.toList()));
                generation.setDisadvantages(Arrays.stream(getString(set, "Nachteile").split("\n"))
                        .filter(s -> !s.isBlank()).collect(Collectors.toList()));
                generation.setDropsWeapon(set.getBoolean("Droppt Waffen"));
                generation.setDropsArmor(set.getBoolean("Droppt Rüstung"));
                generation.setDropsJewellery(set.getBoolean("Droppt Schmuck"));
                generation.setAbleToUsesPrimaryHand(set.getBoolean("Kann Primärhand benutzen"));
                generation.setAbleToUsesSecondaryHand(set.getBoolean("Kann Sekundärhand benutzen"));
                generation.setAbleToUseShield(set.getBoolean("Kann Schild benutzen"));
                generation.setAbleToUseArmor(ArmorPosition.head, set.getBoolean("Kann Helm benutzen"));
                generation.setAbleToUseArmor(ArmorPosition.body, set.getBoolean("Kann Harnisch benutzen"));
                generation.setAbleToUseArmor(ArmorPosition.arms, set.getBoolean("Kann Armschienen benutzen"));
                generation.setAbleToUseArmor(ArmorPosition.legs, set.getBoolean("Kann Beinrüstung benutzen"));
                generation.setAbleToUseJewellery(set.getBoolean("Kann Schmuck benutzen"));
                generation.setUsesAlwaysShield(set.getBoolean("Benutzt immer Schild"));
                generation.setAbleToUseSpells(set.getBoolean("Kann Zauber benutzen"));

                list.add(generation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private static <Generation extends GenerationBase> void linkParents(Statement statement, Collection<Generation> generations) throws SQLException {
        try (ResultSet groupSet = statement.executeQuery("SELECT * FROM [Gegner Gruppierungen]")) {
            while (groupSet.next()) {
                String group = getString(groupSet, "Gruppe");
                String part = getString(groupSet, "Teilgruppe");

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

        try (ResultSet subTypSet = statement.executeQuery("SELECT * FROM [Gegner Subtypen]")) {
            while (subTypSet.next()) {
                String mainName = getString(subTypSet, "Haupttyp");
                String subName = getString(subTypSet, "Subtyp");

                Optional<Generation> parent = mains.stream().filter(g -> g.getName().equalsIgnoreCase(mainName)).findFirst();
                Optional<SubType> child = subs.stream().filter(g -> g.getName().equalsIgnoreCase(subName)).findFirst();

                if (parent.isPresent() && child.isPresent()) {
                    parent.get().addSubType(child.get());
                }

            }
        }
    }

    private static void addMainTalents(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        try (ResultSet talentTypSet = statement.executeQuery("SELECT * FROM [Haupt Talente]")) {
            while (talentTypSet.next()) {
                String name = getString(talentTypSet, "Bezeichnung");
                String talentName = getString(talentTypSet, "Talent");

                GenerationBase base = combined.stream().filter(g -> g.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
                Talent talent = Database.getTalentWithoutDefault(talentName);
                base.addMainTalent(talent);
            }
        }
    }

    private static void addForbiddenTalents(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        try (ResultSet talentTypSet = statement.executeQuery("SELECT * FROM [Verbotene Talente]")) {
            while (talentTypSet.next()) {
                String name = getString(talentTypSet, "Bezeichnung");
                String talentName = getString(talentTypSet, "Talent");

                GenerationBase base = combined.stream().filter(g -> g.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
                Talent talent = Database.getTalentWithoutDefault(talentName);
                base.addForbiddenTalent(talent);
            }
        }
    }

    private static void addPrimaryAttributes(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            generationBase.setPrimaryAttributes(
                    getCollection(statement, "SELECT * FROM [Primäre Attribute] WHERE Bezeichnung=\"" + generationBase.getName() + "\"", "Attribut").stream()
                            .map(PrimaryAttribute::getPrimaryAttribute).collect(Collectors.toList()));
        }
    }

    private static void addSecondaryAttributes(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            generationBase.setSecondaryAttributes(
                    getCollection(statement, "SELECT * FROM [Sekundäre Attribute] WHERE Bezeichnung=\"" + generationBase.getName() + "\"", "Attribut").stream()
                            .map(SecondaryAttribute::getSecondaryAttribute).collect(Collectors.toList()));
        }
    }

    private static void addPrimaryWeaponTypes(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            generationBase.setPrimaryWeaponTypes(
                    getCollection(statement, "SELECT * FROM [Primärhand Waffenart] WHERE Bezeichnung=\"" + generationBase.getName() + "\"", "Waffenart"));
        }
    }

    private static void addSecondaryWeaponTypes(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            generationBase.setSecondaryWeaponTypes(
                    getCollection(statement, "SELECT * FROM [Sekundärhand Waffenart] WHERE Bezeichnung=\"" + generationBase.getName() + "\"", "Waffenart"));
        }
    }

    private static void addSpecificPrimaryWeapons(Statement statement, Collection<GenerationBase> combined) throws SQLException {

        for (GenerationBase generationBase : combined) {
            Collection<String> weaponNames = getCollection(statement, "SELECT * FROM [Ausgerüstete spezifische Primärwaffen] WHERE Bezeichnung=\"" + generationBase.getName() + "\"", "Waffe");

            Collection<Weapon> weapons = new ArrayList<>();
            for (String weaponName : weaponNames) {
                weapons.addAll(loadSpecificWeapons(statement, weaponName));
            }
            generationBase.setSpecificPrimaryWeapons(weapons);
        }
    }

    private static void addSpecificSecondaryWeapons(Statement statement, Collection<GenerationBase> combined) throws SQLException {

        for (GenerationBase generationBase : combined) {
            Collection<String> weaponNames = getCollection(statement, "SELECT * FROM [Ausgerüstete spezifische Sekundärwaffen] WHERE Bezeichnung=\"" + generationBase.getName() + "\"", "Waffe");

            Collection<Weapon> weapons = new ArrayList<>();
            for (String weaponName : weaponNames) {
                weapons.addAll(loadSpecificWeapons(statement, weaponName));
            }
            generationBase.setSpecificSecondaryWeapons(weapons);
        }
    }

    private static Collection<Weapon> loadSpecificWeapons(Statement statement, String weaponName) throws SQLException {
        Collection<Weapon> weapons = new ArrayList<>();

        try (ResultSet weaponSet = statement.executeQuery("SELECT * FROM [Spezifische Waffenstats] WHERE Bezeichnung=\"" + weaponName + "\"")) {
            while (weaponSet.next()) {
                Weapon weapon = new Weapon();
                weapon.setName(weaponName);
                weapon.setTyp("Waffe");
                weapon.setSubTyp(getString(weaponSet, "Waffentyp"));
                weapon.setTier(weaponSet.getInt("Tier"));
                weapon.setRarity(getRarity(weaponSet, "Seltenheit"));
                weapon.setInitiative(getString(weaponSet, "Initiative"));
                weapon.setDice(getString(weaponSet, "Würfel/Belastung"));
                weapon.setDamage(weaponSet.getInt("Schaden/Schutz"));
                weapon.setHit(weaponSet.getInt("Treffer"));
                weapon.setEffect(getString(weaponSet, "Effekt"));

                weapons.add(weapon);
            }
        }
        return weapons;
    }

    private static void addSpecificArmor(Statement statement, Collection<GenerationBase> combined) throws SQLException {

        for (GenerationBase generationBase : combined) {
            try (ResultSet eqSet = statement.executeQuery("SELECT * FROM [Ausgerüstete spezifische Rüstung] WHERE Bezeichnung=\"" + generationBase.getName() + "\"")) {
                Map<ArmorPosition, Collection<Armor>> armor = new HashMap<>();
                for (ArmorPosition position : ArmorPosition.values()) {
                    armor.put(position, new ArrayList<>());
                }

                while (eqSet.next()) {
                    String armorName = getString(eqSet, "Rüstung");
                    ArmorPosition position = ArmorPosition.getArmorPosition(getString(eqSet, "Rüstungstyp"));

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

        try (ResultSet armorSet = statement.executeQuery("SELECT * FROM [Spezifische Rüstungenstats] WHERE (Bezeichnung=\"" + armorName + "\") AND (Rüstungstyp=\"" + position + "\")" )) {
            while (armorSet.next()) {
                Armor armor = new Armor();
                armor.setName(armorName);
                armor.setTyp("Rüstung");
                armor.setSubTyp(position.toString());
                armor.setTier(armorSet.getInt("Tier"));
                armor.setRarity(getRarity(armorSet, "Seltenheit"));
                armor.setProtection(armorSet.getInt("Schutz"));
                armor.setWeight(armorSet.getDouble("Belastung"));
                armor.setEffect(getString(armorSet, "Effekt"));

                armorList.add(armor);
            }
        }
        return armorList;
    }

    private static void addDrops(Statement statement, Collection<GenerationBase> combined) throws SQLException {
        for (GenerationBase generationBase : combined) {
            try (ResultSet dropSet = statement.executeQuery("SELECT * FROM Drops WHERE Bezeichnung=\"" + generationBase.getName() + "\"")) {
                Collection<Drop> drops = new ArrayList<>();

                while (dropSet.next()) {
                    Drop drop = new Drop();
                    drop.setName(getString(dropSet, "Drop"));
                    drop.setChance(dropSet.getFloat("Wahrscheinlichkeit"));
                    drop.setAmount(dropSet.getInt("Anzahl"));
                    drop.setLevelMultiplication(dropSet.getFloat("Levelmultiplikator"));
                    drop.setTierMultiplication(dropSet.getFloat("Tiermultiplikator"));
                    drop.setMinLevel(dropSet.getInt("Mindestlevel"));
                    drop.setMinTier(dropSet.getInt("Mindesttier"));
                    drop.setMaxLevel(dropSet.getInt("Maximallevel"));
                    drop.setMaxTier(dropSet.getInt("Maximaltier"));

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

    private static Talent getTalent(String typ) {
        switch (typ) {
            case "Arkan":
                return Database.getTalent("Arkanmagie");
            case "Erde":
                return Database.getTalent("Erdmagie");
            case "Feuer":
                return Database.getTalent("Feuermagie");
            case "Finster":
                return Database.getTalent("Finstermagie");
            case "Frost":
                return Database.getTalent("Frostmagie");
            case "Illusion":
                return Database.getTalent("Illusionsmagie");
            case "Licht":
                return Database.getTalent("Lichtmagie");
            case "Luft":
                return Database.getTalent("Luftmagie");
            case "Natur":
                return Database.getTalent("Naturmagie");
            case "Sturm":
                return Database.getTalent("Sturmmagie");
            case "Tot":
                return Database.getTalent("Totenmagie");
            case "Wasser":
                return Database.getTalent("Wassermagie");
            case "Wissen":
                return Database.getTalent("Magisches Wissen");

        }
        return Database.getTalent(typ);
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

    private static Collection<String> getCollection(Statement statement, String sql, String label) throws SQLException {
        ArrayList<String> collection = new ArrayList<>();
        try (ResultSet set = statement.executeQuery(sql)) {
            while (set.next()) {
                collection.add(getString(set, label));
            }
        }
        return collection;
    }
}
