package ui.utility.helper;

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
import javafx.util.converter.NumberStringConverter;
import manager.Utility;
import model.Event;
import ui.IView;
import ui.ViewPart;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventView extends ViewPart {
    private StringProperty trigger;
    private StringProperty continent;
    private StringProperty land;
    private StringProperty location;
    private StringProperty chosenLocation;
    private ListProperty<Event> list;
    private ListProperty<String> triggers;
    private ListProperty<String> continents;
    private ListProperty<String> lands;
    private ListProperty<String> locations;
    private ListProperty<String> chosenLocations;
    private BooleanProperty disabled;
    private IntegerProperty searchCount;
    private Random rand;

    public EventView(IView parent) {
        super("Events", parent);

        this.trigger = new SimpleStringProperty("Ausl\u00f6ser");
        this.continent = new SimpleStringProperty("Kontinent");
        this.land = new SimpleStringProperty("Land");
        this.location = new SimpleStringProperty("Gebiet");
        this.chosenLocation = new SimpleStringProperty();
        this.list = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.triggers = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.triggers.add("Ausl\u00f6ser");
        this.continents = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.continents.add("Kontinent");
        this.lands = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.lands.add("Land");
        this.locations = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.locations.add("Gebiet");
        this.chosenLocations = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.disabled = new SimpleBooleanProperty(true);
        this.searchCount = new SimpleIntegerProperty(1);
        this.rand = new Random();

        Utility.eventList.addListener((ob, o, n) -> update());

        float width = 400;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        HBox titleLine = new HBox(10);
        titleLine.setAlignment(Pos.CENTER);

        root.getChildren().add(titleLine);

        TableView<Event> searchTable = new TableView<>();
        searchTable.itemsProperty().bindBidirectional(list);
        searchTable.setPrefWidth(1000);

        TableColumn<Event, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameC.setPrefWidth(100);
        searchTable.getColumns().add(nameC);

        TableColumn<Event, String> typC = new TableColumn<>("Typ");
        typC.setCellValueFactory(new PropertyValueFactory<>("typ"));
        typC.setPrefWidth(100);
        searchTable.getColumns().add(typC);

        TableColumn<Event, String> infoC = new TableColumn<>("Info");
        infoC.setCellValueFactory(new PropertyValueFactory<>("info"));
        searchTable.getColumns().add(infoC);

        TableColumn<Event, String> triggerC = new TableColumn<>("Ausl\u00f6ser");
        triggerC.setCellValueFactory(new PropertyValueFactory<>("trigger"));
        triggerC.setPrefWidth(150);
        searchTable.getColumns().add(triggerC);

        TableColumn<Event, String> continentC = new TableColumn<>("Kontinent");
        continentC.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().continentsAsString()));
        continentC.setPrefWidth(150);
        searchTable.getColumns().add(continentC);

        TableColumn<Event, String> landsC = new TableColumn<>("Land");
        landsC.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().landsAsString()));
        landsC.setPrefWidth(150);
        searchTable.getColumns().add(landsC);

        TableColumn<Event, String> locationsC = new TableColumn<>("Gebiet");
        locationsC.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().locationsAsString()));
        locationsC.setPrefWidth(150);
        searchTable.getColumns().add(locationsC);

        infoC.prefWidthProperty()
                .bind(searchTable.widthProperty().subtract(nameC.widthProperty()).subtract(typC.widthProperty())
                        .subtract(continentC.widthProperty()).subtract(landsC.widthProperty())
                        .subtract(locationsC.widthProperty()).subtract(triggerC.widthProperty()));

        root.getChildren().add(searchTable);

        HBox infos = new HBox(50);
        infos.setAlignment(Pos.CENTER);

        // Buttons
        VBox searchBox = new VBox(10);
        searchBox.setAlignment(Pos.CENTER);

        HBox infos1 = new HBox(50);
        infos1.setAlignment(Pos.CENTER);

        ComboBox<String> triggerB = new ComboBox<>();
        trigger.bind(triggerB.selectionModelProperty().get().selectedItemProperty());
        triggerB.itemsProperty().bindBidirectional(triggers);
        triggerB.getSelectionModel().selectFirst();
        triggerB.setPrefWidth(width / 2 - 5);
        infos1.getChildren().add(triggerB);

        ComboBox<String> continentB = new ComboBox<>();
        continent.bind(continentB.selectionModelProperty().get().selectedItemProperty());
        continentB.itemsProperty().bindBidirectional(continents);
        continentB.getSelectionModel().selectFirst();
        continentB.setPrefWidth(width / 2 - 5);
        infos1.getChildren().add(continentB);

        searchBox.getChildren().add(infos1);

        HBox infos2 = new HBox(50);
        infos2.setAlignment(Pos.CENTER);

        ComboBox<String> landB = new ComboBox<>();
        land.bind(landB.selectionModelProperty().get().selectedItemProperty());
        landB.itemsProperty().bindBidirectional(lands);
        landB.getSelectionModel().selectFirst();
        landB.setPrefWidth(width / 2 - 5);
        infos2.getChildren().add(landB);

        ComboBox<String> locationB = new ComboBox<>();
        location.bind(locationB.selectionModelProperty().get().selectedItemProperty());
        locationB.itemsProperty().bindBidirectional(locations);
        locationB.getSelectionModel().selectFirst();
        locationB.setPrefWidth(width / 2 - 5);
        infos2.getChildren().add(locationB);

        searchBox.getChildren().add(infos2);

        HBox locationBox = new HBox(50);
        locationBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Hinzuf\u00fcgen");
        addButton.setOnAction(ev -> add());
        addButton.setPrefWidth(width / 2 - 5);
        locationBox.getChildren().add(addButton);

        Button removeButton = new Button("Entfernen");
        removeButton.setOnAction(ev -> remove());
        removeButton.setPrefWidth(width / 2 - 5);
        locationBox.getChildren().add(removeButton);

        searchBox.getChildren().add(locationBox);

        HBox searching = new HBox(50);
        searching.setAlignment(Pos.CENTER);

        TextField count = new TextField();
        count.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        count.textProperty().bindBidirectional(searchCount, new NumberStringConverter());
        count.setPrefWidth(width / 2 - 5);
        searching.getChildren().add(count);

        Button searchButton = new Button("Suchen");
        searchButton.setOnAction(ev -> search());
        searchButton.setPrefWidth(width / 2 - 5);
        searchButton.disableProperty().bindBidirectional(disabled);
        searching.getChildren().add(searchButton);

        searchBox.getChildren().add(searching);

        Button clear = new Button("Reset");
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

        TableColumn<String, String> locationC = new TableColumn<>("Gebiete");
        locationC.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()));
        locationT.getColumns().add(locationC);
        locationListBox.getChildren().add(locationT);

        Button clearL = new Button("Reset");
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
        if (location.get() != null && !location.get().equals("Gebiet") && !chosenLocations.contains(location.get())) {
            chosenLocations.add(location.get());
        }
    }

    private void search() {
        for (int i = 0; i < searchCount.intValue(); i++) {

            Stream<Event> stream = Utility.eventList.stream();

            if (!this.trigger.get().equals("Ausl\u00f6ser")) {
                stream = stream.filter(ev -> eq(ev.getTrigger(), this.trigger));
            }
            if (!this.continent.get().equals("Kontinent")) {
                stream = stream.filter(ev -> eq(ev.getContinents(), this.continent));
            }
            if (!this.land.get().equals("Land")) {
                stream = stream.filter(ev -> eq(ev.getLands(), this.land));
            }
            if (!this.chosenLocations.isEmpty()) {
                stream = stream.filter(ev -> {
                    if (ev.getLocations().contains("")) {
                        return true;
                    }
                    for (String loc : ev.getLocations()) {
                        if (chosenLocations.contains(loc)) {
                            return true;
                        }
                    }
                    return false;
                });
            }

            Collection<Event> result = stream.collect(Collectors.toList());

            for (Event event : result) {
                if (rand.nextDouble() <= event.getChance()) {
                    list.add(event);
                }
            }
        }
    }

    private boolean eq(String s1, StringProperty s2) {
        return s1.equals(s2.get()) || s1.equals("");
    }

    private boolean eq(Collection<String> col, StringProperty s2) {
        return col.contains(s2.get()) || col.contains("");
    }

    private void update() {
        if (!Utility.eventList.isEmpty()) {
            this.disabled.set(false);

            ObservableList<String> trigger = FXCollections.observableArrayList("Ausl\u00f6ser");
            ObservableList<String> continent = FXCollections.observableArrayList("Kontinent");
            ObservableList<String> land = FXCollections.observableArrayList("Land");
            ObservableList<String> location = FXCollections.observableArrayList("Gebiet");

            for (Event event : Utility.eventList) {
                String name = event.getName();

                if (name.isEmpty()) {
                    continue;
                }

                if (!trigger.contains(event.getTrigger())) {
                    trigger.add(event.getTrigger());
                }

                for (String element : event.getContinents()) {
                    if (!continent.contains(element)) {
                        continent.add(element);
                    }
                }
                for (String element : event.getLands()) {
                    if (!land.contains(element)) {
                        land.add(element);
                    }
                }
                for (String element : event.getLocations()) {
                    if (!location.contains(element)) {
                        location.add(element);
                    }
                }
            }
            triggers.set(trigger.sorted());
            continents.set(continent.sorted());
            locations.set(location.sorted());
            lands.set(land.sorted());
        } else {
            disabled.set(true);
            triggers.set(FXCollections.observableArrayList("Ausl\u00f6ser"));
            continents.set(FXCollections.observableArrayList("Kontinent"));
            locations.set(FXCollections.observableArrayList("Gebiet"));
            lands.set(FXCollections.observableArrayList("Land"));
        }
    }
}
