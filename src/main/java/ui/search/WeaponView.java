package ui.search;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import manager.Utility;
import model.item.Weapon;
import ui.IView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeaponView extends SearchView<Weapon> {
    private StringProperty name;
    private StringProperty material;
    private StringProperty typ;
    private StringProperty rarity;
    private ListProperty<String> names;
    private ListProperty<String> materials;
    private ListProperty<String> types;
    private BooleanProperty disabled;
    private IntegerProperty searchCount;
    private Random rand;

    public WeaponView(IView parent) {
        super("Waffe", parent, Weapon.class);

        this.name = new SimpleStringProperty("Name");
        this.material = new SimpleStringProperty("Material");
        this.typ = new SimpleStringProperty("Typ");
        this.rarity = new SimpleStringProperty("Seltenheit");
        this.names = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.names.add("Name");
        this.materials = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.materials.add("Material");
        this.types = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.types.add("Typ");
        ListProperty<String> rarities = new SimpleListProperty<>(FXCollections.observableArrayList());
        rarities.add("Seltenheit");
        rarities.addAll(Utility.rarities);
        this.disabled = new SimpleBooleanProperty(true);
        this.searchCount = new SimpleIntegerProperty(1);
        this.rand = new Random();

        Utility.weaponList.addListener((ob, o, n) -> updateTable());

        int width = 400;

        VBox root = this.createRoot(
                new String[]{"Name", "Typ", "Initiative", "Würfel", "Schaden", "Treffer", "Seltenheit", "Preis", "Effekt", "Slots", "Voraussetzung"},
                new String[]{"name", "subTyp", "initiative", "dice", "damage", "hit", "rarity", "cost", "effect", "slots", "requirement"});

        HBox info1 = new HBox(50);
        info1.setAlignment(Pos.CENTER);

        ComboBox<String> nameB = new ComboBox<>();
        name.bind(nameB.selectionModelProperty().get().selectedItemProperty());
        nameB.itemsProperty().bindBidirectional(names);
        nameB.getSelectionModel().selectFirst();
        nameB.setPrefWidth((double) width / 2 - 5);
        info1.getChildren().add(nameB);

        ComboBox<String> materialB = new ComboBox<>();
        material.bind(materialB.selectionModelProperty().get().selectedItemProperty());
        materialB.itemsProperty().bindBidirectional(materials);
        materialB.getSelectionModel().selectFirst();
        materialB.setPrefWidth((double) width / 2 - 5);
        info1.getChildren().add(materialB);

        root.getChildren().add(info1);

        HBox info2 = new HBox(50);
        info2.setAlignment(Pos.CENTER);

        ComboBox<String> typB = new ComboBox<>();
        typ.bind(typB.selectionModelProperty().get().selectedItemProperty());
        typB.itemsProperty().bindBidirectional(types);
        typB.getSelectionModel().selectFirst();
        typB.setPrefWidth((double) width / 2 - 5);
        info2.getChildren().add(typB);

        ComboBox<String> rarityB = new ComboBox<>();
        rarity.bind(rarityB.selectionModelProperty().get().selectedItemProperty());
        rarityB.itemsProperty().bindBidirectional(rarities);
        rarityB.getSelectionModel().selectFirst();
        rarityB.setPrefWidth((double) width / 2 - 5);
        info2.getChildren().add(rarityB);

        root.getChildren().add(info2);

        HBox searching = new HBox(50);
        searching.setAlignment(Pos.CENTER);

        TextField count = new TextField();
        count.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        count.textProperty().bindBidirectional(searchCount, new NumberStringConverter());
        count.setPrefWidth((double) width / 2 - 5);
        searching.getChildren().add(count);

        Button searchButton = new Button("Suchen");
        searchButton.setOnAction(ev -> search());
        searchButton.setPrefWidth((double) width / 2 - 5);
        searchButton.disableProperty().bindBidirectional(disabled);
        searching.getChildren().add(searchButton);

        root.getChildren().add(searching);

        Button clear = new Button("Reset");
        clear.setPrefWidth(width + 40);
        clear.setOnAction(ev -> clear());
        root.getChildren().add(clear);

        updateTable();
        this.setContent(root);
    }

    private void clear() {
        this.fullList.set(FXCollections.observableArrayList());
    }

    private void search() {
        for (int i = 0; i < searchCount.intValue(); i++) {
            String rarity = this.rarity.get().equals("Seltenheit") ? Utility.getRandomRarity() : this.rarity.get();
            Collection<String> material = this.material.get().equals("Material") ? Utility.getRandomMaterial()
                    : Collections.singletonList(this.material.get());

            Stream<Weapon> stream = Utility.weaponList.stream().filter(w -> w.getRarity().equals(rarity));
            stream = stream.filter(w -> material.contains(w.getMaterial()));

            if (!this.name.get().equals("Name")) {
                stream = stream.filter(w -> w.getName().equals(this.name.get()));
            }
            if (!this.typ.get().equals("Typ")) {
                stream = stream.filter(w -> w.getSubTyp().equals(this.typ.get()));
            }
            List<Weapon> result = stream.collect(Collectors.toList());

            if (result.size() > 0) {
                Weapon found = (Weapon) result.get(rand.nextInt(result.size())).getWithUpgrade();
                Weapon other = getItem(found);

                if (other != null) {
                    other.addAmount(1);
                } else {
                    fullList.add(found);
                }
            }
        }
    }

    private Weapon getItem(Weapon found) {

        for (Weapon other : fullList) {
            if (other.equals(found)) {
                return other;
            }
        }

        return null;
    }

    private void updateTable() {
        if (!Utility.weaponList.isEmpty()) {
            this.disabled.set(false);

            ObservableList<String> name = FXCollections.observableArrayList("Name");
            ObservableList<String> material = FXCollections.observableArrayList("Material");
            ObservableList<String> typ = FXCollections.observableArrayList("Typ");

            for (Weapon weapon : Utility.weaponList) {
                String n = weapon.getName();
                String m = weapon.getMaterial();
                String t = weapon.getSubTyp();

                if (!name.contains(n)) {
                    name.add(n);
                }
                if (!material.contains(m)) {
                    material.add(m);
                }
                if (!typ.contains(t)) {
                    typ.add(t);
                }
            }
            names.set(name.sorted());
            materials.set(material.sorted());
            types.set(typ.sorted());

        } else {
            disabled.set(true);
            names.set(FXCollections.observableArrayList("Name"));
            materials.set(FXCollections.observableArrayList("Material"));
            types.set(FXCollections.observableArrayList("Typ"));
        }
    }
}
