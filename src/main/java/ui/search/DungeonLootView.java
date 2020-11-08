package ui.search;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import manager.Utility;
import model.loot.DungeonLootFactory;
import model.loot.Loot;
import ui.IView;
import ui.ViewPart;

import java.util.HashMap;

public class DungeonLootView extends ViewPart {

    private StringProperty container;
    private StringProperty place;
    private ListProperty<Loot> list;
    private ListProperty<String> containers;
    private ListProperty<String> places;
    private BooleanProperty disabled;
    private IntegerProperty lootingCount;
    private HashMap<String, ObservableList<String>> placesMap;

    public DungeonLootView(IView parent) {
        super("Dungeon Loot", parent);

        this.container = new SimpleStringProperty("Beh\u00e4lter");
        this.place = new SimpleStringProperty("Ort");
        this.list = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.containers = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.containers.add("Beh\u00e4lter");
        this.places = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.places.add("Ort");
        this.disabled = new SimpleBooleanProperty(true);
        this.lootingCount = new SimpleIntegerProperty(1);
        this.placesMap = new HashMap<>();

        Utility.dungeonLootList.addListener((ob, o, n) -> update());

        float width = 400;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        HBox titleLine = new HBox(10);
        titleLine.setAlignment(Pos.CENTER);

        root.getChildren().add(titleLine);

        TableView<Loot> lootTable = new TableView<>();
        lootTable.itemsProperty().bindBidirectional(list);
        lootTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Loot, String> name = new TableColumn<>("Gegenstand");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        lootTable.getColumns().add(name);

        TableColumn<Loot, Integer> amount = new TableColumn<>("Menge");
        amount.setCellValueFactory(cell -> cell.getValue().amountProperty().asObject());
        lootTable.getColumns().add(amount);

        root.getChildren().add(lootTable);

        BorderPane infos = new BorderPane();

        ComboBox<String> pla = new ComboBox<>();
        place.bind(pla.selectionModelProperty().get().selectedItemProperty());
        pla.itemsProperty().bindBidirectional(places);
        pla.getSelectionModel().selectFirst();
        pla.setPrefWidth(width / 2 - 5);
        infos.setLeft(pla);

        ComboBox<String> cont = new ComboBox<>();
        container.bind(cont.selectionModelProperty().get().selectedItemProperty());
        cont.itemsProperty().bindBidirectional(containers);
        cont.getSelectionModel().selectFirst();
        cont.setPrefWidth(width / 2 - 5);
        infos.setRight(cont);

        this.place.addListener((ob, o, n) -> {
            if (n != null) {
                this.containers.set(placesMap.get(n).sorted());
                cont.getSelectionModel().selectFirst();
            }
        });

        root.getChildren().add(infos);

        BorderPane looting = new BorderPane();

        TextField count = new TextField();
        count.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        count.textProperty().bindBidirectional(lootingCount, new NumberStringConverter());
        count.setPrefWidth(width / 2 - 5);
        looting.setLeft(count);

        Button lootButton = new Button("Pl\u00fcndern");
        lootButton.setOnAction(ev -> loot());
        lootButton.setPrefWidth(width / 2 - 5);
        lootButton.disableProperty().bindBidirectional(disabled);
        looting.setRight(lootButton);

        root.getChildren().add(looting);

        Button clear = new Button("Reset");
        clear.setOnAction(ev -> clear());
        clear.setMaxWidth(Double.MAX_VALUE);
        root.getChildren().add(clear);

        update();
        this.setContent(root);
    }

    private void clear() {
        this.list.set(FXCollections.observableArrayList());
    }

    private void loot() {
        for (int i = 0; i < lootingCount.intValue(); i++) {

            for (DungeonLootFactory factory : Utility.dungeonLootList) {

                if (this.container.get().equals(factory.getContainer())
                        && this.place.get().equals(factory.getPlace())) {

                    Loot loot = factory.getLoot();
                    Loot own = getLoot(factory.getName());

                    if (own != null) {
                        own.addAmount(loot.getAmount());
                    } else if (loot.getAmount() > 0) {
                        list.add(loot);
                    }
                }
            }
        }
    }

    private Loot getLoot(String artifact) {
        for (Loot l : list) {
            if (l.getName().equals(artifact)) {
                return l;
            }
        }
        return null;
    }

    private void update() {
        if (!Utility.dungeonLootList.isEmpty()) {
            this.disabled.set(false);
            placesMap.clear();

            ObservableList<String> plas = FXCollections.observableArrayList("Ort");
            placesMap.put("Ort", FXCollections.observableArrayList("Behälter"));

            for (DungeonLootFactory factory : Utility.dungeonLootList) {
                String container = factory.getContainer();
                String place = factory.getPlace();

                if (container.isEmpty()) {
                    continue;
                }

                if (!placesMap.containsKey(place)) {
                    placesMap.put(place, FXCollections.observableArrayList());
                }

                if (!plas.contains(place)) {
                    plas.add(place);
                }

                if (!placesMap.get(place).contains(container)) {
                    placesMap.get(place).add(container);
                }

            }
            containers.set(placesMap.get("Ort").sorted());
            places.set(plas.sorted());
        } else {
            this.disabled.set(true);
            this.containers.set(FXCollections.observableArrayList("Beh\u00e4lter"));
            this.places.set(FXCollections.observableArrayList("Ort"));
            this.placesMap.clear();
            this.placesMap.put("Ort", FXCollections.observableArrayList("Behälter"));
        }
    }
}
