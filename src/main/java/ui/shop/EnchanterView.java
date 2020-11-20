package ui.shop;

import city.shop.Enchanter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import manager.Utility;
import model.item.Item;
import model.upgrade.UpgradeFactory;
import model.upgrade.UpgradeModel;

public class EnchanterView extends ShopView {

    private Enchanter enchanter;

    private ObservableList<?> toAdd;
    private ObjectProperty<UpgradeModel> toRemove;

    public EnchanterView(Enchanter enchanter) {
        super();
        stage.setTitle("Verzauberer");

        this.enchanter = enchanter;
        this.toRemove = new SimpleObjectProperty<>();

        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab enchantmentTab = new Tab("Verzauberungen");
        VBox enBox = new VBox(10);

        String[] labels = new String[]{"Name", "Stufe", "Ziel", "Effekt", "Slots", "Kosten", "Mana", "Material 1", "Material 2", "Material 3", "Material 4"};
        String[] names = new String[]{"name", "level", "target", "effect", "slots", "cost", "mana", "material1", "material2", "material3", "material4"};

        TableBox tableBox = createTable(enchanter.getUpgrades(), labels, names, UpgradeModel.class);
        tableBox.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        toAdd = tableBox.getTable().getSelectionModel().getSelectedItems();
        enBox.getChildren().add(tableBox);

        HBox buttons = new HBox(30);
        buttons.setAlignment(Pos.CENTER);

        Button add = new Button("Hinzufügen");
        add.setOnAction(ev -> add());
        buttons.getChildren().add(add);

        Button remove = new Button("Entfernen");
        remove.setOnAction(ev -> remove());
        remove.prefWidthProperty().bind(add.widthProperty());
        buttons.getChildren().add(remove);

        Button clear = new Button("Leeren");
        clear.setOnAction(ev -> clear());
        clear.prefWidthProperty().bind(add.widthProperty());
        buttons.getChildren().add(clear);

        Button buy = new Button("Kaufen");
        buy.setOnAction(ev -> buy());
        buy.prefWidthProperty().bind(add.widthProperty());
        buttons.getChildren().add(buy);

        enchanter.getDifference().addListener((ListChangeListener<Item>) listener ->
                buy.setDisable(!enchanter.getDifference().isEmpty()));

        enBox.getChildren().add(buttons);

        HBox tables = new HBox(10);
        tables.setAlignment(Pos.CENTER);
        tables.setPadding(new Insets(10, 20, 20, 20));

        //Table
        TableView<UpgradeModel> selectedModels = new TableView<>();
        selectedModels.setPrefHeight(200);
        selectedModels.itemsProperty().bindBidirectional(enchanter.selectedProperty());
        tables.getChildren().add(selectedModels);

        toRemove.bind(selectedModels.getSelectionModel().selectedItemProperty());

        TableColumn<UpgradeModel, Integer> amountColumn = new TableColumn<>("Menge");
        selectedModels.getColumns().add(amountColumn);
        amountColumn.setCellValueFactory(val -> enchanter.getAmount().get(val.getValue()).asObject());

        TableColumn<UpgradeModel, String> nameColumn = new TableColumn<>("Name");
        selectedModels.getColumns().add(nameColumn);
        nameColumn.prefWidthProperty().bind(selectedModels.widthProperty().subtract(amountColumn.widthProperty()).subtract(5));
        nameColumn.setCellValueFactory(val -> new ReadOnlyStringWrapper(val.getValue().getFullName()));

        //Table
        TableView<Item> itemDifference = new TableView<>();
        itemDifference.setPrefHeight(200);
        itemDifference.itemsProperty().bindBidirectional(enchanter.differenceProperty());
        tables.getChildren().add(itemDifference);

        TableColumn<Item, Integer> amountColumn1 = new TableColumn<>("Menge");
        itemDifference.getColumns().add(amountColumn1);
        amountColumn1.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Item, String> nameColumn1 = new TableColumn<>("Name");
        itemDifference.getColumns().add(nameColumn1);
        nameColumn1.prefWidthProperty().bind(itemDifference.widthProperty().subtract(amountColumn1.widthProperty()).subtract(5));
        nameColumn1.setCellValueFactory(new PropertyValueFactory<>("name"));

        enBox.getChildren().add(tables);

        enchantmentTab.setContent(enBox);
        root.getTabs().add(enchantmentTab);

        Tab materialTab = new Tab("Material");

        labels = new String[]{"Name", "Typ", "Subtyp", "Seltenheit", "Preis", "Effekt"};
        names = new String[]{"name", "typ", "subTyp", "rarity", "cost", "effect"};
        TableBox tb = createTable(enchanter.getMaterials(), labels, names, Item.class);
        VBox.setVgrow(tb.getTable(), Priority.ALWAYS);
        materialTab.setContent(tb);
        root.getTabs().add(materialTab);

        Scene scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.sizeToScene();
    }

    protected void format(Object object, Text text) {
        if (object instanceof UpgradeModel) {
            UpgradeModel model = (UpgradeModel) object;
            UpgradeFactory factory = Utility.upgradeMap.get(model);

            if (enchanter.getRealMaterials().containsAmount(factory.getMaterialList(model.getLevel(), model.getLevel()))) {
                text.setFill(Paint.valueOf("#000000"));
            } else {
                text.setFill(Paint.valueOf("#ff0000"));
            }
        }
    }

    private void add() {
        enchanter.add(toAdd);
        refresh();
    }

    private void remove() {
        enchanter.remove(toRemove.get());
        refresh();
    }

    private void clear() {
        enchanter.clear();
        refresh();
    }

    private void buy() {
        enchanter.buy();
        refresh();
    }
}
