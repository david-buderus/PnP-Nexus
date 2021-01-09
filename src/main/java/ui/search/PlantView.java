package ui.search;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import manager.Database;
import model.Rarity;
import model.item.Plant;
import ui.IView;
import ui.ViewPart;
import ui.part.NumberField;
import ui.part.UpdatingListCell;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static manager.LanguageUtility.getMessageProperty;

public class PlantView extends ViewPart {

    private final StringProperty name;
    private final StringProperty typ;
    private final ObjectProperty<Rarity> rarity;
    private final StringProperty location;
    private final StringProperty chosenLocation;
    private final ListProperty<Plant> list;
    private final ListProperty<String> names;
    private final ListProperty<String> types;
    private final ListProperty<String> locations;
    private final ListProperty<String> chosenLocations;
    private final BooleanProperty disabled;
    private final IntegerProperty searchCount;
    private final Random rand;

    protected final ReadOnlyStringProperty defaultName;
    protected final ReadOnlyStringProperty defaultTyp;
    protected final ReadOnlyStringProperty defaultLocation;

    public PlantView(IView parent) {
        super("search.plant.title", parent);
        this.defaultName = getMessageProperty("search.default.name");
        this.defaultTyp = getMessageProperty("search.default.typ");
        this.defaultLocation = getMessageProperty("search.plant.default.location");

        this.name = new SimpleStringProperty(defaultName.get());
        this.typ = new SimpleStringProperty(defaultTyp.get());
        this.rarity = new SimpleObjectProperty<>();
        this.location = new SimpleStringProperty(defaultLocation.get());
        this.chosenLocation = new SimpleStringProperty();
        this.list = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.names = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.names.add(defaultName.get());
        this.types = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.types.add(defaultTyp.get());
        this.locations = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.locations.add(defaultLocation.get());
        this.chosenLocations = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.disabled = new SimpleBooleanProperty(true);
        this.searchCount = new SimpleIntegerProperty(1);
        this.rand = new Random();

        Database.plantList.addListener((ob, o, n) -> update());

        int width = 400;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        HBox titleLine = new HBox(10);
        titleLine.setAlignment(Pos.CENTER);

        root.getChildren().add(titleLine);

        TableView<Plant> searchTable = new TableView<>();
        searchTable.itemsProperty().bindBidirectional(list);
        searchTable.setPrefWidth(770);

        TableColumn<Plant, Float> amountC = new TableColumn<>();
        amountC.textProperty().bind(getMessageProperty("column.amount.short"));
        amountC.setCellValueFactory(val -> val.getValue().amountProperty().asObject());
        amountC.setPrefWidth(20);
        amountC.setMaxWidth(25);
        searchTable.getColumns().add(amountC);

        TableColumn<Plant, String> nameC = new TableColumn<>();
        nameC.textProperty().bind(getMessageProperty("column.name"));
        nameC.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameC.setPrefWidth(100);
        searchTable.getColumns().add(nameC);

        TableColumn<Plant, String> typC = new TableColumn<>();
        typC.textProperty().bind(getMessageProperty("column.type"));
        typC.setCellValueFactory(new PropertyValueFactory<>("subTyp"));
        typC.setPrefWidth(100);
        searchTable.getColumns().add(typC);

        TableColumn<Plant, String> effectC = new TableColumn<>();
        effectC.textProperty().bind(getMessageProperty("column.effect"));
        effectC.setCellValueFactory(new PropertyValueFactory<>("effect"));
        effectC.setPrefWidth(250);
        searchTable.getColumns().add(effectC);

        TableColumn<Plant, String> reqC = new TableColumn<>();
        reqC.textProperty().bind(getMessageProperty("search.plant.column.requirement"));
        reqC.setCellValueFactory(new PropertyValueFactory<>("requirement"));
        reqC.setPrefWidth(150);
        searchTable.getColumns().add(reqC);

        TableColumn<Plant, String> locationsC = new TableColumn<>();
        locationsC.textProperty().bind(getMessageProperty("search.plant.column.location"));
        locationsC.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().locationsAsString()));
        locationsC.setPrefWidth(200);

        TableColumn<Plant, String> rarityC = new TableColumn<>();
        rarityC.textProperty().bind(getMessageProperty("column.item.rarity"));
        rarityC.setCellValueFactory(new PropertyValueFactory<>("rarity"));
        rarityC.prefWidthProperty().bind(searchTable.widthProperty().subtract(nameC.widthProperty()).subtract(reqC.widthProperty())
                .subtract(effectC.widthProperty()).subtract(typC.widthProperty()).subtract(locationsC.widthProperty()));
        searchTable.getColumns().add(rarityC);

        searchTable.getColumns().add(locationsC);

        root.getChildren().add(searchTable);

        HBox infos = new HBox(50);
        infos.setAlignment(Pos.CENTER);

        //Buttons
        VBox searchBox = new VBox(10);
        searchBox.setAlignment(Pos.CENTER);

        HBox infos1 = new HBox(50);
        infos1.setAlignment(Pos.CENTER);

        ComboBox<String> nameB = new ComboBox<>();
        name.bind(nameB.selectionModelProperty().get().selectedItemProperty());
        nameB.itemsProperty().bindBidirectional(names);
        nameB.getSelectionModel().selectFirst();
        nameB.setPrefWidth((double) width / 2 - 5);
        infos1.getChildren().add(nameB);

        ComboBox<String> typB = new ComboBox<>();
        typ.bind(typB.selectionModelProperty().get().selectedItemProperty());
        typB.itemsProperty().bindBidirectional(types);
        typB.getSelectionModel().selectFirst();
        typB.setPrefWidth((double) width / 2 - 5);
        infos1.getChildren().add(typB);

        searchBox.getChildren().add(infos1);

        HBox infos2 = new HBox(50);
        infos2.setAlignment(Pos.CENTER);

        ComboBox<Rarity> rarityB = new ComboBox<>();
        rarity.bind(rarityB.selectionModelProperty().get().selectedItemProperty());
        rarityB.setItems(FXCollections.observableArrayList(Rarity.values()));
        rarityB.setCellFactory(list -> new UpdatingListCell<>());
        rarityB.setButtonCell(new UpdatingListCell<>());
        rarityB.getSelectionModel().selectFirst();
        rarityB.setPrefWidth((double) width / 2 - 5);
        infos2.getChildren().add(rarityB);

        ComboBox<String> locationB = new ComboBox<>();
        location.bind(locationB.selectionModelProperty().get().selectedItemProperty());
        locationB.itemsProperty().bindBidirectional(locations);
        locationB.getSelectionModel().selectFirst();
        locationB.setPrefWidth((double) width / 2 - 5);
        infos2.getChildren().add(locationB);

        searchBox.getChildren().add(infos2);

        HBox locationBox = new HBox(50);
        locationBox.setAlignment(Pos.CENTER);

        Button addButton = new Button();
        addButton.textProperty().bind(getMessageProperty("search.plant.button.add"));
        addButton.setOnAction(ev -> add());
        addButton.setPrefWidth((double) width / 2 - 5);
        locationBox.getChildren().add(addButton);

        Button removeButton = new Button();
        removeButton.textProperty().bind(getMessageProperty("search.plant.button.remove"));
        removeButton.setOnAction(ev -> remove());
        removeButton.setPrefWidth((double) width / 2 - 5);
        locationBox.getChildren().add(removeButton);

        searchBox.getChildren().add(locationBox);

        HBox searching = new HBox(50);
        searching.setAlignment(Pos.CENTER);

        NumberField count = new NumberField();
        count.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        count.numberProperty().bindBidirectional(searchCount);
        count.setPrefWidth((double) width / 2 - 5);
        searching.getChildren().add(count);

        Button searchButton = new Button();
        searchButton.textProperty().bind(getMessageProperty("search.button.search"));
        searchButton.setOnAction(ev -> search());
        searchButton.setPrefWidth((double) width / 2 - 5);
        searchButton.disableProperty().bindBidirectional(disabled);
        searching.getChildren().add(searchButton);

        searchBox.getChildren().add(searching);

        Button clear = new Button();
        clear.textProperty().bind(getMessageProperty("search.button.reset"));
        clear.setPrefWidth(width + 40);
        clear.setOnAction(ev -> clearList());
        searchBox.getChildren().add(clear);

        infos.getChildren().add(searchBox);

        // Location
        VBox locationListBox = new VBox(10);
        locationListBox.setAlignment(Pos.CENTER);
        locationListBox.prefHeightProperty().bind(searchBox.heightProperty());


        TableView<String> locationT = new TableView<>();
        locationT.itemsProperty().bindBidirectional(chosenLocations);
        locationT.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        chosenLocation.bind(locationT.getSelectionModel().selectedItemProperty());

        TableColumn<String, String> locationC = new TableColumn<>();
        locationC.textProperty().bind(getMessageProperty("search.plant.column.locations"));
        locationC.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()));
        locationT.getColumns().add(locationC);
        locationListBox.getChildren().add(locationT);

        Button clearL = new Button();
        clearL.textProperty().bind(getMessageProperty("search.button.reset"));
        clearL.setPrefWidth(width + 40);
        clearL.setOnAction(ev -> clearLocations());
        locationListBox.getChildren().add(clearL);

        infos.getChildren().add(locationListBox);

        root.getChildren().add(infos);

        update();
        this.setContent(root);
    }

    private void clearList() {
        this.list.set(FXCollections.observableArrayList());
    }

    private void clearLocations() {
        this.chosenLocations.set(FXCollections.observableArrayList());
    }

    private void remove() {
        chosenLocations.remove(chosenLocation.get());
    }

    private void add() {
        if (location.get() != null && !location.get().equals(defaultLocation.get()) && !chosenLocations.contains(location.get())) {
            chosenLocations.add(location.get());
        }
    }

    private void search() {
        for (int i = 0; i < searchCount.intValue(); i++) {
            Rarity rarity = this.rarity.get() == Rarity.unknown ? Rarity.getRandomRarity() : this.rarity.get();

            Stream<Plant> stream = Database.plantList.stream().filter(w -> w.getRarity() == rarity);

            if (!this.name.get().equals(defaultName.get())) {
                stream = stream.filter(w -> w.getName().equals(this.name.get()));
            }
            if (!this.typ.get().equals(defaultTyp.get())) {
                stream = stream.filter(w -> w.getSubTyp().equals(this.typ.get()));
            }
            if (!this.chosenLocations.isEmpty()) {
                stream = stream.filter(p -> {
                    for (String loc : p.getLocations()) {
                        if (chosenLocations.contains(loc)) {
                            return true;
                        }
                    }
                    return false;
                });
            }

            List<Plant> result = stream.collect(Collectors.toList());

            if (result.size() > 0) {
                Plant found = result.get(rand.nextInt(result.size()));
                Plant other = getItem(found);

                if (other != null) {
                    other.addAmount(1);
                } else {
                    list.add(found);
                }
            }
        }
    }

    private Plant getItem(Plant found) {

        for (Plant other : list) {
            if (other.equals(found)) {
                return other;
            }
        }

        return null;
    }

    private void update() {
        if (!Database.plantList.isEmpty()) {
            this.disabled.set(false);

            ObservableList<String> name = FXCollections.observableArrayList(defaultName.get());
            ObservableList<String> typ = FXCollections.observableArrayList(defaultTyp.get());
            ObservableList<String> location = FXCollections.observableArrayList(defaultLocation.get());

            for (Plant plant : Database.plantList) {
                String n = plant.getName();
                String t = plant.getSubTyp();

                if (n.isEmpty()) {
                    continue;
                }

                if (!name.contains(n)) {
                    name.add(n);
                }
                if (!typ.contains(t)) {
                    typ.add(t);
                }

                for (String loc : plant.getLocations()) {
                    if (!location.contains(loc)) {
                        location.add(loc);
                    }
                }
            }
            names.set(name.sorted());
            types.set(typ.sorted());
            locations.set(location.sorted());
        } else {
            disabled.set(true);
            names.set(FXCollections.observableArrayList(defaultName.get()));
            types.set(FXCollections.observableArrayList(defaultTyp.get()));
            locations.set(FXCollections.observableArrayList(location.get()));
        }
    }
}
