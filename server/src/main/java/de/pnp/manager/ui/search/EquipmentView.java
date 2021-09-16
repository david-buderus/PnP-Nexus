package de.pnp.manager.ui.search;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.Rarity;
import de.pnp.manager.model.item.Equipment;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.part.UpdatingListCell;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public abstract class EquipmentView<Eq extends Equipment> extends SearchView<Eq> {

    protected final StringProperty name;
    protected final StringProperty material;
    protected final StringProperty typ;
    protected final ObjectProperty<Rarity> rarity;
    protected final ListProperty<String> names;
    protected final ListProperty<String> materials;
    protected final ListProperty<String> types;
    protected final BooleanProperty disabled;
    protected final IntegerProperty searchCount;

    protected ReadOnlyStringProperty defaultName;
    protected final ReadOnlyStringProperty defaultMaterial;
    protected final ReadOnlyStringProperty defaultTyp;

    protected final ListProperty<Eq> equipmentList;

    public EquipmentView(String title, IView parent, ListProperty<Eq> equipmentList) {
        super(title, parent);
        this.equipmentList = equipmentList;
        this.equipmentList.addListener((observable, o, n) -> update());
        this.defaultName = getMessageProperty("search.default.name");
        this.defaultMaterial = getMessageProperty("search.default.material");
        this.defaultTyp = getMessageProperty("search.default.typ");
        this.name = new SimpleStringProperty(defaultName.get());
        this.material = new SimpleStringProperty(defaultMaterial.get());
        this.typ = new SimpleStringProperty(defaultTyp.get());
        this.rarity = new SimpleObjectProperty<>(Rarity.unknown);
        this.names = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.names.add(defaultName.get());
        this.materials = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.materials.add(defaultMaterial.get());
        this.types = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.types.add(defaultTyp.get());
        this.disabled = new SimpleBooleanProperty(true);
        this.searchCount = new SimpleIntegerProperty(1);
    }

    protected void addControls() {
        int width = 400;

        HBox infos1 = new HBox(50);
        infos1.setAlignment(Pos.CENTER);

        ComboBox<String> nameB = new ComboBox<>();
        name.bind(nameB.selectionModelProperty().get().selectedItemProperty());
        nameB.itemsProperty().bindBidirectional(names);
        nameB.getSelectionModel().select(defaultName.get());
        nameB.setPrefWidth((double) width / 2 - 5);
        infos1.getChildren().add(nameB);

        ComboBox<String> materialB = new ComboBox<>();
        material.bind(materialB.selectionModelProperty().get().selectedItemProperty());
        materialB.itemsProperty().bindBidirectional(materials);
        materialB.getSelectionModel().selectFirst();
        materialB.setPrefWidth((double) width / 2 - 5);
        infos1.getChildren().add(materialB);

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

        Button searchButton = new Button();
        searchButton.textProperty().bind(getMessageProperty("search.button.search"));
        searchButton.setOnAction(ev -> search());
        searchButton.setPrefWidth((double) width / 2 - 5);
        searchButton.disableProperty().bindBidirectional(disabled);
        searching.getChildren().add(searchButton);

        root.getChildren().add(searching);

        Button clear = new Button();
        clear.textProperty().bind(getMessageProperty("search.button.reset"));
        clear.setPrefWidth(width + 40);
        clear.setOnAction(ev -> clear());
        root.getChildren().add(clear);
    }

    @SuppressWarnings("unchecked")
    protected void search() {
        for (int i = 0; i < searchCount.intValue(); i++) {
            Rarity rarity = this.rarity.get() == Rarity.unknown ? Rarity.getRandomRarity() : this.rarity.get();
            Collection<String> material = this.material.get().equals(defaultMaterial.get()) ? Database.getRandomMaterial()
                    : Collections.singletonList(this.material.get());

            Stream<Eq> stream = equipmentList.stream().filter(w -> w.getRarity() == rarity);
            stream = stream.filter(w -> material.contains(w.getMaterial()));

            if (!this.name.get().equals(defaultName.get())) {
                stream = stream.filter(w -> w.getName().equals(this.name.get()));
            }
            if (!this.typ.get().equals(defaultTyp.get())) {
                stream = stream.filter(w -> w.getSubtype().equals(this.typ.get()));
            }
            List<Eq> result = stream.collect(Collectors.toList());

            if (result.size() > 0) {
                Eq found = (Eq) result.get(rand.nextInt(result.size())).getWithUpgrade();
                Eq other = getItem(found);

                if (other != null) {
                    other.addAmount(1);
                } else {
                    fullList.add(found);
                }
            }
        }
    }

    protected Eq getItem(Eq found) {

        for (Eq other : fullList) {
            if (other.equals(found)) {
                return other;
            }
        }

        return null;
    }

    protected void clear() {
        this.fullList.set(FXCollections.observableArrayList());
    }

    protected void update() {
        if (!equipmentList.isEmpty()) {
            this.disabled.set(false);

            ObservableList<String> name = FXCollections.observableArrayList(defaultName.get());
            ObservableList<String> material = FXCollections.observableArrayList(defaultMaterial.get());
            ObservableList<String> typ = FXCollections.observableArrayList(defaultTyp.get());

            for (Eq equipment : equipmentList) {
                String n = equipment.getName();
                String m = equipment.getMaterial();
                String t = equipment.getSubtype();

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
            names.set(FXCollections.observableArrayList(defaultName.get()));
            materials.set(FXCollections.observableArrayList(defaultMaterial.get()));
            types.set(FXCollections.observableArrayList(defaultTyp.get()));
        }
    }
}
