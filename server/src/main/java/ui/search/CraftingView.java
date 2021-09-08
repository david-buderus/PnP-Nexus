package ui.search;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import manager.Database;
import manager.LanguageUtility;
import manager.TypTranslation;
import model.other.CraftingBonus;
import ui.IView;
import ui.part.NumStringConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static manager.LanguageUtility.getMessageProperty;

public class CraftingView extends SearchView<CraftingBonus> {

    private final StringProperty target;
    private final IntegerProperty amount;
    private final StringProperty info;

    public CraftingView(IView parent) {
        super("search.crafting.title", parent);
        this.target = new SimpleStringProperty("");
        this.amount = new SimpleIntegerProperty(1);
        this.info = new SimpleStringProperty("");

        this.target.addListener((ob, o, n) -> update(n));

        tableView.addColumn("column.name", CraftingBonus::getName, 120);
        tableView.addColumn("column.target", CraftingBonus::getTarget, 100);
        tableView.addColumn("column.effect", CraftingBonus::getEffect, 235);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setMaxWidth(455);

        addControls();

        root.setPadding(new Insets(0));

        HBox newRoot = new HBox(10);
        newRoot.setAlignment(Pos.CENTER);
        newRoot.setPadding(new Insets(10, 20, 20, 20));
        newRoot.getChildren().add(root);
        this.setContent(newRoot);

        TableView<String> typeTable = new TableView<>();
        typeTable.maxHeightProperty().bind(root.heightProperty().subtract(80));
        typeTable.itemsProperty().bind(TypTranslation.allTypes);
        typeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        typeTable.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> target.set(n));

        TableColumn<String, String> column = new TableColumn<>();
        column.textProperty().bind(LanguageUtility.getMessageProperty("column.type"));
        column.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));
        typeTable.getColumns().add(column);

        newRoot.getChildren().add(typeTable);

    }

    private void search() {
        Collection<String> targets = TypTranslation.getAllTypes(target.get());
        ArrayList<CraftingBonus> bonuses = Database.craftingBonusList.stream()
                .filter(x -> targets.contains(x.getTarget())).collect(Collectors.toCollection(ArrayList::new));

        if (bonuses.size() > 0) {
            for (int i = 0; i < amount.get(); i++) {
                fullList.add(bonuses.get(rand.nextInt(bonuses.size())));
            }
        }
    }

    private void update(String target) {
        StringBuilder text = new StringBuilder();

        for (String part : TypTranslation.getAllTypes(target)) {
            text.append(part).append("\n");
        }

        info.set(text.toString());
    }

    private void clear() {
        this.fullList.set(FXCollections.observableArrayList());
    }

    protected void addControls() {
        HBox bottomLayer = new HBox(10);
        bottomLayer.setPadding(new Insets(25, 0, 0, 0));
        bottomLayer.setAlignment(Pos.CENTER);
        root.getChildren().add(bottomLayer);

        VBox inputBox = new VBox(5);
        inputBox.setAlignment(Pos.CENTER);
        bottomLayer.getChildren().add(inputBox);

        HBox searchable = new HBox(0);
        searchable.setAlignment(Pos.CENTER);
        inputBox.getChildren().add(searchable);

        TextField amountField = new TextField();
        amountField.textProperty().bindBidirectional(amount, new NumStringConverter());
        amountField.setPrefWidth(50);
        searchable.getChildren().add(amountField);

        TextField targetField = new TextField();
        targetField.textProperty().bindBidirectional(target);
        targetField.setPrefWidth(200);
        searchable.getChildren().add(targetField);

        Button searchButton = new Button("Suchen");
        searchButton.textProperty().bind(getMessageProperty("search.button.search"));
        searchButton.setPrefWidth(250);
        searchButton.setOnAction(ev -> search());
        inputBox.getChildren().add(searchButton);

        Button clearButton = new Button();
        clearButton.textProperty().bind(getMessageProperty("search.button.reset"));
        clearButton.setPrefWidth(250);
        clearButton.setOnAction(ev -> clear());
        inputBox.getChildren().add(clearButton);

        TextArea infoText = new TextArea();
        infoText.setEditable(false);
        infoText.setPrefWidth(200);
        infoText.setPrefHeight(85);
        infoText.textProperty().bindBidirectional(info);
        bottomLayer.getChildren().add(infoText);
    }
}
