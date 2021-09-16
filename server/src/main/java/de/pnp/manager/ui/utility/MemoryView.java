package de.pnp.manager.ui.utility;

import de.pnp.manager.main.Utility;
import de.pnp.manager.model.loot.ILoot;
import de.pnp.manager.model.loot.Loot;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import de.pnp.manager.ui.battle.LootView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public class MemoryView extends ViewPart {

    private final ObservableList<ILoot> loots;

    public MemoryView(IView parent) {
        super("memory.title", parent);
        this.loots = FXCollections.observableArrayList();

        VBox root = new VBox();
        root.setPadding(new Insets(10, 20, 20, 20));

        TableView<ILoot> lootTable = new TableView<>();
        VBox.setVgrow(lootTable, Priority.ALWAYS);
        lootTable.setItems(loots);
        lootTable.setRowFactory(table -> new LootView.LootRow());
        lootTable.setPrefWidth(800);

        TableColumn<ILoot, String> name = new TableColumn<>();
        name.textProperty().bind(getMessageProperty("memory.column.name"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        lootTable.getColumns().add(name);

        TableColumn<ILoot, Integer> amount = new TableColumn<>();
        amount.textProperty().bind(getMessageProperty("column.amount"));
        amount.setCellValueFactory(cell -> ((Loot) cell.getValue()).amountProperty().asObject());
        amount.setPrefWidth(100);
        lootTable.getColumns().add(amount);

        name.prefWidthProperty().bind(lootTable.widthProperty().subtract(amount.widthProperty()));

        root.getChildren().add(lootTable);

        BorderPane buttonPane = new BorderPane();
        buttonPane.setPadding(new Insets(10, 0, 0, 0));
        root.getChildren().add(buttonPane);

        Button remove = new Button();
        remove.textProperty().bind(getMessageProperty("memory.button.remove"));
        remove.setPrefWidth(150);
        buttonPane.setLeft(remove);

        remove.setOnAction(ev -> loots.remove(lootTable.getSelectionModel().getSelectedItem()));

        Label coinLabel = new Label();
        buttonPane.setCenter(coinLabel);

        Button sell = new Button();
        sell.textProperty().bind(getMessageProperty("memory.button.sell"));
        sell.setPrefWidth(150);
        buttonPane.setRight(sell);

        sell.setOnAction(ev -> coinLabel.setText(Utility.sellLoot(loots).getCoinString()));

        this.setContent(root);
    }

    public void add(ILoot loot) {
        this.loots.add(loot);
    }
}
