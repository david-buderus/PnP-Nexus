package ui.shop;

import city.Town;
import city.TownTyp;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import manager.Utility;
import ui.View;

public class CreateTownView extends View {

    private ObjectProperty<Town> selectedTown;

    public CreateTownView(){
        super();
        this.stage.setTitle("Erstelle Stadt");
        this.selectedTown = new SimpleObjectProperty<>();

        int width = 400;

        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        TextField name = new TextField();
        name.setPrefWidth(width);
        root.getChildren().add(name);

        BorderPane firstRow = new BorderPane();

        TextField populationField = new TextField("1000");
        populationField.setPrefWidth((double) width / 2 - 5);

        firstRow.setLeft(populationField);

        Button popCreate = new Button("Erstellen");
        popCreate.setPrefWidth((double) width / 2 - 5);
        popCreate.setOnAction(ev -> {
            try{
                createTown(name.getText(), Integer.parseInt(populationField.getText()));
            } catch (NumberFormatException e){
                populationField.setText("Keine Zahl");
            }
        });

        firstRow.setRight(popCreate);
        root.getChildren().add(firstRow);

        BorderPane secondRow = new BorderPane();

        ComboBox<TownTyp> townTyp = new ComboBox<>();
        townTyp.setItems(FXCollections.observableArrayList(TownTyp.values()));
        townTyp.getSelectionModel().select(TownTyp.town);
        townTyp.setPrefWidth((double) width / 2 - 5);

        secondRow.setLeft(townTyp);

        Button typCreate = new Button("Erstellen");
        typCreate.setPrefWidth((double) width / 2 - 5);
        typCreate.setOnAction(ev -> createTown(name.getText(), townTyp.getValue()));

        secondRow.setRight(typCreate);
        root.getChildren().add(secondRow);

        TableView<Town> table = new TableView<>();
        table.setPrefWidth(width);
        table.setPrefHeight(150);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.itemsProperty().bind(Utility.townList);
        root.getChildren().add(table);

        TableColumn<Town, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(nameC);

        TableColumn<Town, TownTyp> typC = new TableColumn<>("Typ");
        typC.setCellValueFactory(new PropertyValueFactory<>("typ"));
        table.getColumns().add(typC);

        TableColumn<Town, Integer> popC = new TableColumn<>("Bevölkerung");
        popC.setCellValueFactory(new PropertyValueFactory<>("population"));
        table.getColumns().add(popC);

        selectedTown.bind(table.getSelectionModel().selectedItemProperty());

        Button chose = new Button("Auswählen");
        chose.setOnAction(ev -> choseTown());
        root.getChildren().add(chose);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    private void createTown(String name, int population){
        Town town = new Town(name, population);
        Utility.townList.add(town);
        new TownView(town);
        this.stage.close();
    }

    private void createTown(String name, TownTyp typ){
        Town town = new Town(name, typ);
        Utility.townList.add(town);
        new TownView(town);
        this.stage.close();
    }

    private void choseTown(){
        if(selectedTown.get() != null){
            new TownView(selectedTown.get());
            this.stage.close();
        }
    }
}
