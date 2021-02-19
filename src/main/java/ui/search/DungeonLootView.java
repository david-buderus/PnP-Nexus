package ui.search;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import manager.Database;
import model.loot.DungeonLootFactory;
import model.loot.Loot;
import ui.IView;
import ui.ViewPart;

import java.util.HashMap;

import static manager.LanguageUtility.getMessageProperty;

public class DungeonLootView extends ViewPart {

    private final StringProperty container;
    private final StringProperty place;
    private final ListProperty<Loot> list;
    private final ListProperty<String> containers;
    private final ListProperty<String> places;
    private final BooleanProperty disabled;
    private final IntegerProperty lootingCount;
    private final HashMap<String, ObservableList<String>> placesMap;

    private final ReadOnlyStringProperty containerDefault;
    private final ReadOnlyStringProperty placeDefault;

    public DungeonLootView(IView parent) {
        super("search.loot.title", parent);
        this.containerDefault = getMessageProperty("search.loot.default.container");
        this.placeDefault = getMessageProperty("search.loot.default.place");

        this.container = new SimpleStringProperty(containerDefault.get());
        this.place = new SimpleStringProperty(placeDefault.get());
        this.list = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.containers = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.containers.add(containerDefault.get());
        this.places = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.places.add(placeDefault.get());
        this.disabled = new SimpleBooleanProperty(true);
        this.lootingCount = new SimpleIntegerProperty(1);
        this.placesMap = new HashMap<>();

        Database.dungeonLootList.addListener((ob, o, n) -> update());

        float width = 400;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        TableView<Loot> lootTable = new TableView<>();
        lootTable.itemsProperty().bindBidirectional(list);
        lootTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Loot, String> name = new TableColumn<>();
        name.textProperty().bind(getMessageProperty("search.loot.column.name"));
        name.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getName()));
        lootTable.getColumns().add(name);

        TableColumn<Loot, Integer> amount = new TableColumn<>();
        amount.textProperty().bind(getMessageProperty("column.amount"));
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

        Button lootButton = new Button();
        lootButton.textProperty().bind(getMessageProperty("search.loot.button.loot"));
        lootButton.setOnAction(ev -> loot());
        lootButton.setPrefWidth(width / 2 - 5);
        lootButton.disableProperty().bindBidirectional(disabled);
        looting.setRight(lootButton);

        root.getChildren().add(looting);

        Button clear = new Button();
        clear.textProperty().bind(getMessageProperty("search.button.reset"));
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

            for (DungeonLootFactory factory : Database.dungeonLootList) {

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
        if (!Database.dungeonLootList.isEmpty()) {
            this.disabled.set(false);
            placesMap.clear();

            ObservableList<String> placesList = FXCollections.observableArrayList(placeDefault.get());
            placesMap.put(placeDefault.get(), FXCollections.observableArrayList(containerDefault.get()));

            for (DungeonLootFactory factory : Database.dungeonLootList) {
                String container = factory.getContainer();
                String place = factory.getPlace();

                if (container.isEmpty()) {
                    continue;
                }

                if (!placesMap.containsKey(place)) {
                    placesMap.put(place, FXCollections.observableArrayList());
                }

                if (!placesList.contains(place)) {
                    placesList.add(place);
                }

                if (!placesMap.get(place).contains(container)) {
                    placesMap.get(place).add(container);
                }

            }
            containers.set(placesMap.get(placeDefault.get()).sorted());
            places.set(placesList.sorted());
        } else {
            this.disabled.set(true);
            this.containers.set(FXCollections.observableArrayList(containerDefault.get()));
            this.places.set(FXCollections.observableArrayList(placeDefault.get()));
            this.placesMap.clear();
            this.placesMap.put(placeDefault.get(), FXCollections.observableArrayList(containerDefault.get()));
        }
    }
}
