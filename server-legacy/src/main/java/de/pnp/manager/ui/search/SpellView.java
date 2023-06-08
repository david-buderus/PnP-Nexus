package de.pnp.manager.ui.search;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.other.Spell;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public class SpellView extends ViewPart {

    private final StringProperty name;
    private final StringProperty typ;
    private final StringProperty cost;
    private final IntegerProperty tier;
    private final ListProperty<Spell> list;
    private final ListProperty<String> names;
    private final ListProperty<String> typs;
    private final ListProperty<String> costs;
    private final ListProperty<Integer> tiers;
    private final BooleanProperty disabled;
    private final IntegerProperty searchCount;
    private final Random rand;

    protected final ReadOnlyStringProperty defaultName;
    protected final ReadOnlyStringProperty defaultTyp;
    protected final ReadOnlyStringProperty defaultCost;

    public SpellView(IView parent) {
        super("search.spell.title", parent);
        this.defaultName = getMessageProperty("search.default.name");
        this.defaultTyp = getMessageProperty("search.default.typ");
        this.defaultCost = getMessageProperty("search.spell.default.cost");

        this.name = new SimpleStringProperty(defaultName.get());
        this.typ = new SimpleStringProperty(defaultTyp.get());
        this.cost = new SimpleStringProperty(defaultCost.get());
        this.tier = new SimpleIntegerProperty(1);
        this.list = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.names = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.names.add(defaultName.get());
        this.typs = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.typs.add(defaultTyp.get());
        this.costs = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.costs.add(defaultCost.get());
        this.tiers = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.tiers.addAll(1, 2, 3, 4, 5);
        this.disabled = new SimpleBooleanProperty(true);
        this.searchCount = new SimpleIntegerProperty(1);
        this.rand = new Random();

        Database.spellList.addListener((ob, o, n) -> update());

        int width = 400;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        HBox titleLine = new HBox(10);
        titleLine.setAlignment(Pos.CENTER);

        root.getChildren().add(titleLine);

        TableView<Spell> searchTable = new TableView<>();
        searchTable.itemsProperty().bindBidirectional(list);
        searchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        searchTable.setPrefWidth(730);

        TableColumn<Spell, String> nameC = new TableColumn<>();
        nameC.textProperty().bind(getMessageProperty("column.name"));
        nameC.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getName()));
        nameC.setPrefWidth(100);
        searchTable.getColumns().add(nameC);

        TableColumn<Spell, String> effectC = new TableColumn<>();
        effectC.textProperty().bind(getMessageProperty("column.effect"));
        effectC.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getEffect()));
        effectC.setPrefWidth(330);
        searchTable.getColumns().add(effectC);

        TableColumn<Spell, String> typC = new TableColumn<>();
        typC.textProperty().bind(getMessageProperty("column.type"));
        typC.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getType()));
        typC.setPrefWidth(100);
        searchTable.getColumns().add(typC);

        TableColumn<Spell, String> costC = new TableColumn<>();
        costC.textProperty().bind(getMessageProperty("column.spell.cost"));
        costC.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getCost()));
        costC.setPrefWidth(50);
        searchTable.getColumns().add(costC);

        TableColumn<Spell, String> timeC = new TableColumn<>();
        timeC.textProperty().bind(getMessageProperty("column.spell.castTime"));
        timeC.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getCastTime()));
        timeC.setPrefWidth(50);
        searchTable.getColumns().add(timeC);

        TableColumn<Spell, Integer> rarityC = new TableColumn<>();
        rarityC.textProperty().bind(getMessageProperty("column.tier"));
        rarityC.setCellValueFactory(cell -> new ReadOnlyIntegerWrapper(cell.getValue().getTier()).asObject());
        searchTable.getColumns().add(rarityC);

        root.getChildren().add(searchTable);

        HBox infos1 = new HBox(50);
        infos1.setAlignment(Pos.CENTER);

        ComboBox<String> nameB = new ComboBox<>();
        name.bind(nameB.selectionModelProperty().get().selectedItemProperty());
        nameB.itemsProperty().bindBidirectional(names);
        nameB.getSelectionModel().selectFirst();
        nameB.setPrefWidth(width / 2f - 5);
        infos1.getChildren().add(nameB);

        ComboBox<String> typB = new ComboBox<>();
        typ.bind(typB.selectionModelProperty().get().selectedItemProperty());
        typB.itemsProperty().bindBidirectional(typs);
        typB.getSelectionModel().selectFirst();
        typB.setPrefWidth(width / 2f - 5);
        infos1.getChildren().add(typB);

        root.getChildren().add(infos1);

        HBox infos2 = new HBox(50);
        infos2.setAlignment(Pos.CENTER);

        ComboBox<String> costB = new ComboBox<>();
        cost.bind(costB.selectionModelProperty().get().selectedItemProperty());
        costB.itemsProperty().bindBidirectional(costs);
        costB.getSelectionModel().selectFirst();
        costB.setPrefWidth(width / 2f - 5);
        infos2.getChildren().add(costB);

        ComboBox<Integer> tierB = new ComboBox<>();
        tier.bind(tierB.selectionModelProperty().get().selectedItemProperty());
        tierB.itemsProperty().bindBidirectional(tiers);
        tierB.getSelectionModel().selectFirst();
        tierB.setPrefWidth(width / 2f - 5);
        infos2.getChildren().add(tierB);

        root.getChildren().add(infos2);

        HBox searching = new HBox(50);
        searching.setAlignment(Pos.CENTER);

        TextField count = new TextField();
        count.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        count.textProperty().bindBidirectional(searchCount, new NumberStringConverter());
        count.setPrefWidth(width / 2f - 5);
        searching.getChildren().add(count);

        Button searchButton = new Button();
        searchButton.textProperty().bind(getMessageProperty("search.button.search"));
        searchButton.setOnAction(ev -> search());
        searchButton.setPrefWidth(width / 2f - 5);
        searchButton.disableProperty().bindBidirectional(disabled);
        searching.getChildren().add(searchButton);

        root.getChildren().add(searching);

        Button clear = new Button();
        clear.textProperty().bind(getMessageProperty("search.button.reset"));
        clear.setPrefWidth(width + 40);
        clear.setOnAction(ev -> clear());
        root.getChildren().add(clear);

        update();
        this.setContent(root);
    }

    private void clear() {
        this.list.set(FXCollections.observableArrayList());
    }

    private void search() {
        for (int i = 0; i < searchCount.intValue(); i++) {
            int tier = this.tier.get();

            Stream<Spell> stream = Database.spellList.stream().filter(w -> w.getTier() == tier);

            if (!this.name.get().equals(defaultName.get())) {
                stream = stream.filter(w -> w.getName().equals(this.name.get()));
            }
            if (!this.typ.get().equals(defaultTyp.get())) {
                stream = stream.filter(w -> w.getType().equals(this.typ.get()));
            }
            if (!this.cost.get().equals(defaultCost.get())) {
                stream = stream.filter(w -> w.getCost().equals(this.cost.get()));
            }
            List<Spell> result = stream.collect(Collectors.toList());

            if (result.size() > 0) {
                list.add(result.get(rand.nextInt(result.size())));
            }
        }
    }

    private void update() {
        if (!Database.spellList.isEmpty()) {
            this.disabled.set(false);

            ObservableList<String> name = FXCollections.observableArrayList(defaultName.get());
            ObservableList<String> typ = FXCollections.observableArrayList(defaultTyp.get());
            ObservableList<String> cost = FXCollections.observableArrayList(defaultCost.get());

            for (Spell spell : Database.spellList) {
                String n = spell.getName();
                String t = spell.getType();
                String c = spell.getCost();

                if (!name.contains(n)) {
                    name.add(n);
                }
                if (!cost.contains(c)) {
                    cost.add(c);
                }
                if (!typ.contains(t)) {
                    typ.add(t);
                }
            }
            names.set(name.sorted());
            typs.set(typ.sorted());
            costs.set(cost.sorted());
        } else {
            disabled.set(true);
            names.set(FXCollections.observableArrayList(defaultName.get()));
            typs.set(FXCollections.observableArrayList(defaultTyp.get()));
            costs.set(FXCollections.observableArrayList(defaultCost.get()));
        }
    }
}
