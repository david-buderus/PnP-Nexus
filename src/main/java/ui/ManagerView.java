package ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import manager.TypTranslation;
import manager.Utility;
import model.*;
import model.item.*;
import model.loot.DungeonLootFactory;
import model.upgrade.UpgradeFactory;
import net.ucanaccess.complex.SingleValue;
import org.apache.commons.lang.exception.ExceptionUtils;
import ui.battle.BattleView;
import ui.map.MapView;
import ui.search.SearchOverview;
import ui.utility.InconsistencyView;
import ui.utility.InfoView;
import ui.utility.MemoryView;
import ui.utility.SQLView;
import ui.utility.helper.HelperOverview;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ManagerView extends View {

    public ManagerView(Stage stage) {
        super(stage, new SimpleStringProperty("Keine Datei ausgew\u00e4hlt"));

        stage.setTitle("P&P Manager");

        int width = 400;

        TabPane root = new TabPane();

        Tab startTab = new Tab("Start");
        startTab.setClosable(false);
        root.getTabs().add(startTab);

        Tab battleTab = new BattleView(this);
        root.getTabs().add(battleTab);

        Tab itemTab = new SQLView(this);
        root.getTabs().add(itemTab);

        Tab searchOverviewTab = new SearchOverview(this);
        root.getTabs().add(searchOverviewTab);

        Tab helpOverviewTab = new HelperOverview(this);
        root.getTabs().add(helpOverviewTab);

        MemoryView memoryTab = new MemoryView(this);
        Utility.memoryView = memoryTab;
        root.getTabs().add(memoryTab);

        Tab mapTab = new MapView(this);
        root.getTabs().add(mapTab);

        Tab inconsistencyTab = new InconsistencyView(this);
        root.getTabs().add(inconsistencyTab);


        //First Menu
        VBox startPane = new VBox(10);
        startPane.setPadding(new Insets(10, 20, 20, 20));
        startPane.setAlignment(Pos.CENTER);
        startTab.setContent(startPane);

        HBox titleLine = new HBox(10);
        titleLine.setPadding(new Insets(0, 0, 10, 0));
        titleLine.setAlignment(Pos.CENTER);

        Label fileText = new Label();
        fileText.setPrefWidth((int) (width * (2.0 / 3)) - 10);
        fileText.textProperty().bindBidirectional(fileName);

        titleLine.getChildren().add(fileText);

        Button loadButton = new Button("Laden");
        loadButton.setPrefWidth(width - ((int) (width * (2.0 / 3)) - 10));
        loadButton.setOnAction(ev -> load());

        titleLine.getChildren().add(loadButton);

        startPane.getChildren().add(titleLine);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private InfoView info;

    private void load() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Accessdatei", "*.accdb"),
                new ExtensionFilter("Alle Dateien", "*.*"));
        File file = chooser.showOpenDialog(stage);

        if (file == null) {
            return;
        }

        load(file);
    }

    public void load(File file) {
        this.info = new InfoView("Ladefehler");
        this.fileName.set("Lädt...");

        Service<Object> service = new Service<>() {
            @Override
            protected Task<Object> createTask() {
                return new Task<>() {
                    @Override
                    protected Object call() {

                        Connection connection = null;
                        try {
                            connection = DriverManager.getConnection("jdbc:ucanaccess://" + file.getPath());
                            update(connection);

                        } catch (SQLException e) {
                            info.add(ExceptionUtils.getFullStackTrace(e));
                            this.cancel();
                        } catch (Exception e) {
                            info.add(ExceptionUtils.getFullStackTrace(e));
                            e.printStackTrace();
                        } finally {
                            if (connection != null) {
                                try {
                                    connection.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    this.cancel();
                                }
                            }
                        }
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded(ev -> {
            this.fileName.set(file.getName());
            if (!info.isEmpty())
                info.show();
        });
        service.setOnCancelled(ev -> {
            this.fileName.set("Datei konnte nicht geladen werden");
            if (!info.isEmpty())
                info.show();
        });
        service.start();
    }

    private void update(Connection connection) throws SQLException {
        TypTranslation.clear();
        Statement statement = connection.createStatement();

        //Weapon
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

            Platform.runLater(() -> Utility.weaponList.set(weaponList));
        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Waffen konnten nicht geladen werden.");
        }

        //Armor
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

            Platform.runLater(() -> Utility.armorList.set(armorList));
        } catch (SQLException e) {
            info.add("Rüstungen konnten nicht geladen werden.");
        }

        //Jewellery
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

            Platform.runLater(() -> Utility.jewelleryList.set(jewelleryList));
        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Schmuck konnte nicht geladen werden.");
        }

        //Plant
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

            Platform.runLater(() -> Utility.plantList.set(plantList));
        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Pflanzen konnten nicht geladen werden.");
        }

        Thread loadingThread = Thread.currentThread();

        //Item
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
                Utility.itemList.set(itemList);
                Utility.itemList.addAll(Utility.weaponList);
                Utility.itemList.addAll(Utility.armorList);
                Utility.itemList.addAll(Utility.jewelleryList);
                Utility.itemList.addAll(Utility.plantList);
                loadingThread.interrupt();
            });

        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Items konnten nicht geladen werden.");
        }

        try {
            //Time to let the itemList be refreshed
            Thread.sleep(10000);
        } catch (InterruptedException ignored) {
        }

        //Spell
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

            Platform.runLater(() -> Utility.spellList.set(spellList));
        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Zauber konnten nicht geladen werden.");
        }

        //Upgrade
        try (ResultSet upgradeSet = statement.executeQuery("SELECT * FROM Verbesserungen")) {
            ObservableList<UpgradeFactory> upgradeList = FXCollections.observableArrayList();

            while (upgradeSet.next()) {

                String name = getString(upgradeSet, "Bezeichnung");
                int level = upgradeSet.getInt("Stufe");

                UpgradeFactory exist = this.getUpgradeFactory(name, upgradeList);

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
                        Item material = Utility.getItemWithoutDefault(mat).copy();
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
                        Item material = Utility.getItemWithoutDefault(mat).copy();
                        material.setAmount(amount);
                        materials.add(material);
                    }
                    upgradeFactory.setMaterials(level, materials);

                    upgradeList.add(upgradeFactory);
                }
            }

            Platform.runLater(() -> Utility.upgradeList.set(upgradeList));
        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Verbesserungen konnten nicht geladen werden.");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            info.add("Verbesserungen konnten nicht geladen werden. Ein Material existierte nicht.");
        }

        TypTranslation.addStandards();

        //DungeonLoot
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

            Platform.runLater(() -> Utility.dungeonLootList.set(lootList));
        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Loot konnte nicht geladen werden.");
        }

        //Event
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

            Platform.runLater(() -> Utility.eventList.set(eventList));
        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Events konnten nicht geladen werden.");
        }

        //CraftingBonus
        try (ResultSet eventSet = statement.executeQuery("SELECT * FROM Herstellungsverbesserungen")) {
            ObservableList<CraftingBonus> craftingBonusList = FXCollections.observableArrayList();

            while (eventSet.next()) {
                CraftingBonus craftingBonus = new CraftingBonus();
                craftingBonus.setName(getString(eventSet, "Bezeichnung"));
                craftingBonus.setTarget(getString(eventSet, "Ziel"));
                craftingBonus.setEffect(getString(eventSet, "Effekt"));

                craftingBonusList.add(craftingBonus);
            }

            Platform.runLater(() -> Utility.craftingBonusList.set(craftingBonusList));
        } catch (SQLException e) {
            e.printStackTrace();
            info.add("Herstellungsverbesserungen konnten nicht geladen werden.");
        }

        //Fabrication
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
                    Item material = Utility.getItemWithoutDefault(mat).copy();
                    material.setAmount(amount);
                    materials.add(material);
                }
                fabrication.setMaterials(materials);

                fabricationList.add(fabrication);
            }

            Platform.runLater(() -> Utility.fabricationList.set(fabricationList));
        } catch (SQLException e) {
            info.add("Herstellungen konnten nicht geladen werden.");
            e.printStackTrace();
        }

        //Inconsistencies
        ObservableList<Inconsistency> inconsistencyList = FXCollections.observableArrayList();

        for (UpgradeFactory upgrade : Utility.upgradeList) {

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

                    ArrayList<String> info = new ArrayList<>();
                    for (Item material : upgrade.getMaterials(level)) {
                        if (material != null) {
                            String matCost = Utility.visualiseSell(Math.round(material.getCostAsCopper()));
                            info.add(material.getPrettyAmount() + " " + material + " (" + matCost + ")");
                        }
                    }
                    inconsistency.setInfo(info);

                    inconsistencyList.add(inconsistency);
                }
            }
        }

        for (Fabrication fabrication : Utility.fabricationList) {

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

                    ArrayList<String> info = new ArrayList<>();
                    for (Item material : fabrication.getMaterials()) {
                        if (material != null) {
                            String matCost = Utility.visualiseSell(Math.round(material.getCostAsCopper()));
                            info.add(material.getAmount() + " " + material + " (" + matCost + ")");
                        }
                    }
                    inconsistency.setInfo(info);

                    inconsistencyList.add(inconsistency);
                }
            }
        }
        Platform.runLater(() -> Utility.inconsistencyList.set(inconsistencyList));

        statement.close();
    }

    private UpgradeFactory getUpgradeFactory(String name, Collection<UpgradeFactory> collection) {
        for (UpgradeFactory upgradeFactory : collection) {
            if (upgradeFactory.getName().equals(name)) {
                return upgradeFactory;
            }
        }
        return null;
    }

    private String getString(ResultSet resultSet, String label) throws SQLException {
        String string = resultSet.getString(label);
        return string != null ? string : "";
    }

    private Collection<String> getCollection(Statement statement, String sql, String label) throws SQLException {
        ArrayList<String> collection = new ArrayList<>();
        ResultSet set = statement.executeQuery(sql);
        while (set.next()) {
            collection.add(getString(set, label));
        }
        return collection;
    }
}
