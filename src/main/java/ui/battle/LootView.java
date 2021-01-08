package ui.battle;

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
import manager.LanguageUtility;
import manager.Utility;
import model.Currency;
import model.item.Equipment;
import model.item.Item;
import model.loot.Loot;
import model.upgrade.Upgrade;
import ui.View;

import java.util.Collection;

import static manager.LanguageUtility.getMessageProperty;

public class LootView extends View {

    public LootView(Collection<Loot> loot, Collection<EXP> exp, int playerCount) {
        super("loot.title");

        VBox root = new VBox();
        root.setPadding(new Insets(10, 20, 20, 20));

        Scene scene = new Scene(root);

        TableView<Loot> lootTable = new TableView<>();
        VBox.setVgrow(lootTable, Priority.ALWAYS);
        lootTable.setItems(FXCollections.observableArrayList(loot));
        lootTable.setRowFactory(table -> new LootRow());
        lootTable.setPrefWidth(800);

        TableColumn<Loot, String> name = new TableColumn<>();
        name.textProperty().bind(getMessageProperty("loot.item.name"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        lootTable.getColumns().add(name);

        TableColumn<Loot, Integer> amount = new TableColumn<>();
        amount.textProperty().bind(getMessageProperty("loot.item.amount"));
        amount.setCellValueFactory(cell -> cell.getValue().amountProperty().asObject());
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
            Currency sellValue = Utility.sellLoot(lootTable.getItems());

            if (playerCount > 0) {
                coinLabel.setText(sellValue.getCoinString() + "\t "+ LanguageUtility.getMessage("loot.sell.perPlayer") + ": " +
                        sellValue.divide(playerCount).getCoinString());
            } else {
                coinLabel.setText(sellValue.getCoinString() );
            }

        });

        stage.setScene(scene);
        stage.show();
    }

    public static class EXP {
        public String player;
        public int amount;
    }

    public static class LootRow extends TableRow<Loot> {

        public LootRow() {
            super();

            this.itemProperty().addListener((ob, o, n) -> {
                this.setOnContextMenuRequested(ev -> {
                });
                if (n == null) {
                    return;
                }

                Item item = n.getItem();

                if (item instanceof Equipment) {
                    Collection<Upgrade> upgrades = ((Equipment) item).getUpgrades();

                    if (!upgrades.isEmpty()) {
                        ContextMenu upgradeMenu = new ContextMenu();
                        this.setOnContextMenuRequested(ev -> upgradeMenu.show(this, ev.getScreenX(), ev.getScreenY()));

                        for (Upgrade upgrade : upgrades) {
                            MenuItem upgradeItem = new MenuItem(upgrade.getFullName());
                            upgradeMenu.getItems().add(upgradeItem);
                        }
                    }
                }
            });
        }
    }
}
