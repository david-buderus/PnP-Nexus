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
import manager.Database;
import manager.Utility;
import model.Rarity;
import model.item.Jewellery;
import ui.IView;
import ui.part.UpdatingListCell;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JewelleryView extends SearchView<Jewellery> {
    private final StringProperty material;
    private final StringProperty gem;
    private final StringProperty typ;
    private final ObjectProperty<Rarity> rarity;
    private final ListProperty<String> gems;
    private final ListProperty<String> materials;
    private final ListProperty<String> types;
    private final BooleanProperty disabled;
    private final IntegerProperty searchCount;
    private final Random rand;

    public JewelleryView(IView parent) {
        super("Schmuck", parent, Jewellery.class);

        this.material = new SimpleStringProperty("Material");
        this.gem = new SimpleStringProperty("Edelstein");
        this.typ = new SimpleStringProperty("Typ");
        this.rarity = new SimpleObjectProperty<>();
        this.materials = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.materials.add("Material");
        this.gems = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.gems.add("Edelstein");
        this.types = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.types.add("Typ");
        this.disabled = new SimpleBooleanProperty(true);
        this.searchCount = new SimpleIntegerProperty(1);
        this.rand = new Random();

        Database.jewelleryList.addListener((ob, o, n) -> update());

        int width = 400;

        VBox root = this.createRoot(
                new String[]{"Name", "Typ", "Material", "Edelstein", "Seltenheit", "Preis", "Effekt", "Slots", "Voraussetzung"},
                new String[]{"name", "subTyp", "material", "gem", "rarity", "cost", "effect", "slots", "requirement"});

        HBox infos1 = new HBox(50);
        infos1.setAlignment(Pos.CENTER);

        ComboBox<String> materialB = new ComboBox<>();
        material.bind(materialB.selectionModelProperty().get().selectedItemProperty());
        materialB.itemsProperty().bindBidirectional(materials);
        materialB.getSelectionModel().selectFirst();
        materialB.setPrefWidth((double) width / 2 - 5);
        infos1.getChildren().add(materialB);

        ComboBox<String> gemB = new ComboBox<>();
        gem.bind(gemB.selectionModelProperty().get().selectedItemProperty());
        gemB.itemsProperty().bindBidirectional(gems);
        gemB.getSelectionModel().selectFirst();
        gemB.setPrefWidth((double) width / 2 - 5);
        infos1.getChildren().add(gemB);

        root.getChildren().add(infos1);

        HBox infos2 = new HBox(50);
        infos2.setAlignment(Pos.CENTER);

        ComboBox<String> typB = new ComboBox<>();
        typ.bind(typB.selectionModelProperty().get().selectedItemProperty());
        typB.itemsProperty().bindBidirectional(types);
        typB.getSelectionModel().selectFirst();
        typB.setPrefWidth((double) width / 2 - 5);
        infos2.getChildren().add(typB);

        ComboBox<Rarity> rarityB = new ComboBox<>();
        rarity.bind(rarityB.selectionModelProperty().get().selectedItemProperty());
        rarityB.setItems(FXCollections.observableArrayList(Rarity.values()));
        rarityB.setButtonCell(new UpdatingListCell<>());
        rarityB.setCellFactory(list -> new UpdatingListCell<>());
        rarityB.getSelectionModel().select(Rarity.unknown);
        rarityB.setPrefWidth((double) width / 2 - 5);
        infos2.getChildren().add(rarityB);

        root.getChildren().add(infos2);

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

        update();
        this.setContent(root);
    }

    private void clear() {
        this.fullList.set(FXCollections.observableArrayList());
    }

    private void search() {
        for (int i = 0; i < searchCount.intValue(); i++) {
            Rarity rarity = this.rarity.get() == Rarity.unknown ? Utility.getRandomRarity() : this.rarity.get();
            Collection<String> material = this.material.get().equals("Material") ? Database.getRandomMaterial()
                    : Collections.singletonList(this.material.get());

            Stream<Jewellery> stream = Database.jewelleryList.stream().filter(w -> w.getRarity() == rarity);
            stream = stream.filter(w -> material.contains(w.getMaterial()));

            if (!this.gem.get().equals("Edelstein")) {
                stream = stream.filter(w -> w.getGem().equals(this.gem.get()));
            }
            if (!this.typ.get().equals("Typ")) {
                stream = stream.filter(w -> w.getSubTyp().equals(this.typ.get()));
            }
            List<Jewellery> result = stream.collect(Collectors.toList());

            if (result.size() > 0) {
                Jewellery found = (Jewellery) result.get(rand.nextInt(result.size())).getWithUpgrade();
                Jewellery other = getItem(found);

                if (other != null) {
                    other.addAmount(1);
                } else {
                    fullList.add(found);
                }
            }
        }
    }

    private Jewellery getItem(Jewellery found) {

        for (Jewellery other : fullList) {
            if (other.equals(found)) {
                return other;
            }
        }

        return null;
    }

    private void update() {
        if (!Database.jewelleryList.isEmpty()) {
            this.disabled.set(false);

            ObservableList<String> material = FXCollections.observableArrayList("Material");
            ObservableList<String> gem = FXCollections.observableArrayList("Edelstein");
            ObservableList<String> typ = FXCollections.observableArrayList("Typ");

            for (Jewellery jewellery : Database.jewelleryList) {
                String m = jewellery.getMaterial();
                String g = jewellery.getGem();
                String t = jewellery.getSubTyp();

                if (!material.contains(m)) {
                    material.add(m);
                }
                if (!gem.contains(g)) {
                    gem.add(g);
                }
                if (!typ.contains(t)) {
                    typ.add(t);
                }
            }
            materials.set(material);
            gems.set(gem.sorted());
            types.set(typ.sorted());
        } else {
            disabled.set(true);
            materials.set(FXCollections.observableArrayList("Material"));
            gems.set(FXCollections.observableArrayList("Edelstein"));
            types.set(FXCollections.observableArrayList("Typ"));
        }
    }
}
