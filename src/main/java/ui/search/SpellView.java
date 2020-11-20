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
import javafx.util.converter.NumberStringConverter;
import manager.Utility;
import model.Spell;
import ui.IView;
import ui.ViewPart;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpellView extends ViewPart {

    private StringProperty name;
    private StringProperty typ;
    private StringProperty cost;
    private IntegerProperty tier;
    private ListProperty<Spell> list;
    private ListProperty<String> names;
    private ListProperty<String> typs;
    private ListProperty<String> costs;
    private ListProperty<Integer> tiers;
    private BooleanProperty disabled;
    private IntegerProperty searchCount;
    private Random rand;

    public SpellView(IView parent) {
        super("Zauber", parent);
        this.name = new SimpleStringProperty("Name");
        this.typ = new SimpleStringProperty("Typ");
        this.cost = new SimpleStringProperty("Kosten");
        this.tier = new SimpleIntegerProperty(1);
        this.list = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.names = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.names.add("Name");
        this.typs = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.typs.add("Typ");
        this.costs = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.costs.add("Kosten");
        this.tiers = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.tiers.addAll(1, 2, 3, 4, 5);
        this.disabled = new SimpleBooleanProperty(true);
        this.searchCount = new SimpleIntegerProperty(1);
        this.rand = new Random();

        Utility.spellList.addListener((ob, o, n) -> update());

        int width = 400;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        HBox titleLine = new HBox(10);
        titleLine.setAlignment(Pos.CENTER);

        root.getChildren().add(titleLine);

        TableView<Spell> searchTable = new TableView<>();
        searchTable.itemsProperty().bindBidirectional(list);
        searchTable.setPrefWidth(730);

        TableColumn<Spell, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameC.setPrefWidth(100);
        searchTable.getColumns().add(nameC);

        TableColumn<Spell, String> effectC = new TableColumn<>("Effekt");
        effectC.setCellValueFactory(new PropertyValueFactory<>("effect"));
        effectC.setPrefWidth(330);
        searchTable.getColumns().add(effectC);

        TableColumn<Spell, String> typC = new TableColumn<>("Typ");
        typC.setCellValueFactory(new PropertyValueFactory<>("typ"));
        typC.setPrefWidth(100);
        searchTable.getColumns().add(typC);

        TableColumn<Spell, String> costC = new TableColumn<>("Kosten");
        costC.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costC.setPrefWidth(50);
        searchTable.getColumns().add(costC);

        TableColumn<Spell, String> timeC = new TableColumn<>("Zauberzeit");
        timeC.setCellValueFactory(new PropertyValueFactory<>("castTime"));
        timeC.setPrefWidth(50);
        searchTable.getColumns().add(timeC);

        TableColumn<Spell, Integer> rarityC = new TableColumn<>("Tier");
        rarityC.setCellValueFactory(new PropertyValueFactory<>("tier"));
        rarityC.prefWidthProperty().bind(searchTable.widthProperty().subtract(nameC.widthProperty())
                .subtract(effectC.widthProperty()).subtract(typC.widthProperty()).subtract(costC.widthProperty()));
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

        Button searchButton = new Button("Suchen");
        searchButton.setOnAction(ev -> search());
        searchButton.setPrefWidth(width / 2f - 5);
        searchButton.disableProperty().bindBidirectional(disabled);
        searching.getChildren().add(searchButton);

        root.getChildren().add(searching);

        Button clear = new Button("Reset");
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

            Stream<Spell> stream = Utility.spellList.stream().filter(w -> w.getTier() == tier);

            if (!this.name.get().equals("Name")) {
                stream = stream.filter(w -> w.getName().equals(this.name.get()));
            }
            if (!this.typ.get().equals("Typ")) {
                stream = stream.filter(w -> w.getTyp().equals(this.typ.get()));
            }
            if (!this.cost.get().equals("Kosten")) {
                stream = stream.filter(w -> w.getCost().equals(this.cost.get()));
            }
            List<Spell> result = stream.collect(Collectors.toList());

            if (result.size() > 0) {
                list.add(result.get(rand.nextInt(result.size())));
            }
        }
    }

    private void update() {
        if (!Utility.spellList.isEmpty()) {
            this.disabled.set(false);

            ObservableList<String> name = FXCollections.observableArrayList("Name");
            ObservableList<String> typ = FXCollections.observableArrayList("Typ");
            ObservableList<String> cost = FXCollections.observableArrayList("Kosten");

            for (Spell spell : Utility.spellList) {
                String n = spell.getName();
                String t = spell.getTyp();
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
            names.set(FXCollections.observableArrayList("Name"));
            typs.set(FXCollections.observableArrayList("Typ"));
            costs.set(FXCollections.observableArrayList("Kosten"));
        }
    }
}
