package ui.utility;

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
import manager.Utility;
import model.item.Item;
import model.loot.Loot;
import ui.IView;
import ui.ViewPart;
import ui.battle.LootView;

public class MemoryView extends ViewPart {

    private ObservableList<Loot> loots;

    public MemoryView(IView parent){
        super("Merkliste", parent);
        this.loots = FXCollections.observableArrayList();

        VBox root = new VBox();
        root.setPadding(new Insets(10, 20, 20, 20));

        TableView<Loot> lootTable = new TableView<>();
        VBox.setVgrow(lootTable, Priority.ALWAYS);
        lootTable.setItems(loots);
        lootTable.setRowFactory(table -> new LootView.LootRow());
        lootTable.setPrefWidth(800);

        TableColumn<Loot, String> name = new TableColumn<>("Gegenstand");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        lootTable.getColumns().add(name);

        TableColumn<Loot, Integer> amount = new TableColumn<>("Menge");
        amount.setCellValueFactory(cell -> cell.getValue().amountProperty().asObject());
        amount.setPrefWidth(100);
        lootTable.getColumns().add(amount);

        name.prefWidthProperty().bind(lootTable.widthProperty().subtract(amount.widthProperty()));

        root.getChildren().add(lootTable);

        BorderPane buttonPane = new BorderPane();
        buttonPane.setPadding(new Insets(10,0,0,0));
        root.getChildren().add(buttonPane);

        Button remove = new Button("Entfernen");
        remove.setPrefWidth(150);
        buttonPane.setLeft(remove);

        remove.setOnAction(ev -> loots.remove(lootTable.getSelectionModel().getSelectedItem()));

        Label coinLabel = new Label();
        buttonPane.setCenter(coinLabel);

        Button sell = new Button("Verkaufen");
        sell.setPrefWidth(150);
        buttonPane.setRight(sell);

        sell.setOnAction(ev -> {
            int itemValue = 0;
            int coinValue = 0;

            for (Loot l : loots) {
                Item item = l.getItem();

                if(item.getCostOfOneAsCopper() > 0){
                    itemValue += item.getCostOfOneAsCopper() * l.getAmount();
                } else {
                    switch (item.getName()){
                        case "Kupfer":
                            coinValue += l.getAmount();
                            break;
                        case "Silber":
                            coinValue += l.getAmount()*100;
                            break;
                        case "Gold":
                            coinValue += l.getAmount()*10000;
                            break;
                    }
                }
            }

            coinLabel.setText(Utility.visualiseSell(Math.round(itemValue * 0.8f) + coinValue));
        });

        this.setContent(root);
    }

    public void add(Loot loot){
        this.loots.add(loot);
    }
}
