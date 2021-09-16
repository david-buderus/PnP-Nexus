package de.pnp.manager.ui.battle;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.item.IEquipment;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.loot.ILoot;
import de.pnp.manager.model.loot.Loot;
import de.pnp.manager.model.upgrade.IUpgrade;
import de.pnp.manager.ui.View;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collection;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public class LootView extends View {

    public LootView(Collection<ILoot> loot, Collection<EXP> exp, int playerCount) {
        super("loot.title");

        VBox root = new VBox();
        root.setPadding(new Insets(10, 20, 20, 20));

        Scene scene = new Scene(root);

        TableView<ILoot> lootTable = new TableView<>();
        VBox.setVgrow(lootTable, Priority.ALWAYS);
        lootTable.setItems(FXCollections.observableArrayList(loot));
        lootTable.setRowFactory(table -> new LootRow());
        lootTable.setPrefWidth(800);

        TableColumn<ILoot, String> name = new TableColumn<>();
        name.textProperty().bind(getMessageProperty("loot.item.name"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        lootTable.getColumns().add(name);

        TableColumn<ILoot, Integer> amount = new TableColumn<>();
        amount.textProperty().bind(getMessageProperty("loot.item.amount"));
        amount.setCellValueFactory(cell -> ((Loot) cell.getValue()).amountProperty().asObject());
        amount.setPrefWidth(100);
        lootTable.getColumns().add(amount);

        name.prefWidthProperty().bind(lootTable.widthProperty().subtract(amount.widthProperty()));

        root.getChildren().add(lootTable);

        TableView<EXP> levelTable = new TableView<>();
        levelTable.setItems(FXCollections.observableArrayList(exp));
        levelTable.setPrefWidth(800);
        levelTable.setPrefHeight(200);

        TableColumn<EXP, String> player = new TableColumn<>();
        player.textProperty().bind(getMessageProperty("loot.player.name"));
        player.setCellValueFactory(x -> new ReadOnlyStringWrapper(x.getValue().player));
        levelTable.getColumns().add(player);

        TableColumn<EXP, Integer> expAmount = new TableColumn<>();
        expAmount.textProperty().bind(getMessageProperty("loot.player.exp"));
        expAmount.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().amount).asObject());
        expAmount.setPrefWidth(100);
        levelTable.getColumns().add(expAmount);
        root.getChildren().add(levelTable);

        player.prefWidthProperty().bind(levelTable.widthProperty().subtract(expAmount.widthProperty()));

        BorderPane buttonPane = new BorderPane();
        buttonPane.setPadding(new Insets(10, 0, 0, 0));
        root.getChildren().add(buttonPane);

        HBox leftButtons = new HBox(5);
        buttonPane.setLeft(leftButtons);

        Button remove = new Button();
        remove.textProperty().bind(getMessageProperty("loot.button.remove"));
        remove.setPrefWidth(100);
        leftButtons.getChildren().add(remove);
        remove.setOnAction(ev -> lootTable.getItems().remove(lootTable.getSelectionModel().getSelectedItem()));

        Button save = new Button();
        save.textProperty().bind(getMessageProperty("loot.button.save"));
        save.setPrefWidth(100);
        leftButtons.getChildren().add(save);
        save.setOnAction(ev -> {
            if (lootTable.getSelectionModel().getSelectedItem() != null) {
                Utility.memoryView.add(lootTable.getSelectionModel().getSelectedItem());
                lootTable.getItems().remove(lootTable.getSelectionModel().getSelectedItem());
            }
        });

        Label coinLabel = new Label();
        buttonPane.setCenter(coinLabel);

        Button sell = new Button();
        sell.textProperty().bind(getMessageProperty("loot.button.sell"));
        sell.setPrefWidth(100);
        buttonPane.setRight(sell);

        sell.setOnAction(ev -> {
            ICurrency sellValue = Utility.sellLoot(lootTable.getItems());

            if (playerCount > 0) {
                coinLabel.setText(sellValue.getCoinString() + "\t " + LanguageUtility.getMessage("loot.sell.perPlayer") + ": " +
                        sellValue.divide(playerCount).getCoinString());
            } else {
                coinLabel.setText(sellValue.getCoinString());
            }

        });

        stage.setScene(scene);
        stage.show();
    }

    public static class EXP {
        public String player;
        public int amount;
    }

    public static class LootRow extends TableRow<ILoot> {

        public LootRow() {
            super();

            this.itemProperty().addListener((ob, o, n) -> {
                this.setOnContextMenuRequested(ev -> {
                });
                if (n == null) {
                    return;
                }

                IItem item = n.getItem();

                if (item instanceof IEquipment) {
                    Collection<IUpgrade> upgrades = ((IEquipment) item).getUpgrades();

                    if (!upgrades.isEmpty()) {
                        ContextMenu upgradeMenu = new ContextMenu();
                        this.setOnContextMenuRequested(ev -> upgradeMenu.show(this, ev.getScreenX(), ev.getScreenY()));

                        for (IUpgrade upgrade : upgrades) {
                            MenuItem upgradeItem = new MenuItem(upgrade.getFullName());
                            upgradeMenu.getItems().add(upgradeItem);
                        }
                    }
                }
            });
        }
    }
}
