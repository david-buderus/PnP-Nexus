package de.pnp.manager.ui.utility.helper;

import de.pnp.manager.model.member.data.FoodRequirement;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import de.pnp.manager.model.member.FoodMember;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import de.pnp.manager.ui.part.NumStringConverter;
import de.pnp.manager.ui.part.UpdatingListCell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public class FoodView extends ViewPart {
    private final List<FoodMember> members;

    public FoodView(IView parent) {
        super("helper.food.title", parent);
        this.members = new ArrayList<>();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 0, 20, 0));

        // Title
        HBox line = new HBox(10);
        line.setAlignment(Pos.CENTER_LEFT);
        line.setPadding(new Insets(0, 0, 10, 5));

        line.getChildren().add(nameBox("helper.food.name"));

        HBox life = nameBox("helper.food.food");
        life.setAlignment(Pos.CENTER_LEFT);

        Button lAdd = new Button("+");
        lAdd.setPrefSize(25, 25);
        lAdd.setOnMouseClicked(ev -> {
            for (FoodMember member : members) {
                member.eat(ctrlValues(ev));
            }
        });
        life.getChildren().add(lAdd);

        Button lRemove = new Button("-");
        lRemove.setPrefSize(25, 25);
        lRemove.setOnMouseClicked(ev -> {
            for (FoodMember member : members) {
                member.eat(-ctrlValues(ev));
            }
        });
        life.getChildren().add(lRemove);

        line.getChildren().add(life);

        HBox mana = nameBox("helper.food.drinking");
        mana.setAlignment(Pos.CENTER_LEFT);

        Button mAdd = new Button("+");
        mAdd.setPrefSize(25, 25);
        mAdd.setOnMouseClicked(ev -> {
            for (FoodMember member : members) {
                member.drink(ctrlValues(ev));
            }
        });
        mana.getChildren().add(mAdd);

        Button mRemove = new Button("-");
        mRemove.setPrefSize(25, 25);
        mRemove.setOnMouseClicked(ev -> {
            for (FoodMember member : members) {
                member.drink(-ctrlValues(ev));
            }
        });
        mana.getChildren().add(mRemove);

        line.getChildren().add(mana);

        line.getChildren().add(nameBox("helper.food.hunger"));

        line.getChildren().add(nameBox("helper.food.thirst"));

        line.getChildren().add(nameBox("helper.food.foodConsumption"));

        line.getChildren().add(nameBox("helper.food.drinkingConsumption"));

        root.setTop(line);

        // Scroll

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scroll.setPadding(new Insets(5, 5, 5, 5));

        VBox inner = new VBox(10);
        inner.setAlignment(Pos.CENTER_LEFT);
        scroll.setContent(inner);

        root.setCenter(scroll);

        BorderPane buttons = new BorderPane();
        buttons.setPadding(new Insets(15, 5, 5, 5));

        Button addButton = new Button();
        addButton.textProperty().bind(getMessageProperty("helper.food.button.add"));
        addButton.setPrefWidth(100);
        addButton.setOnAction((ev) -> addOne(inner));
        buttons.setRight(addButton);

        Button turnButton = new Button();
        turnButton.textProperty().bind(getMessageProperty("helper.food.button.nextDay"));
        turnButton.setPrefWidth(100);
        turnButton.setOnAction(ev -> nextDay());
        buttons.setCenter(turnButton);

        root.setBottom(buttons);

        Button clearButton = new Button();
        clearButton.textProperty().bind(getMessageProperty("helper.food.button.clear"));
        clearButton.setPrefWidth(100);
        clearButton.setOnAction(ev -> clear(inner));
        buttons.setLeft(clearButton);

        addOne(inner);

        this.setContent(root);
    }

    private double ctrlValues(MouseEvent ev) {
        if (ev.getButton() != MouseButton.PRIMARY) {
            return 0;
        }

        if (ev.isShiftDown()) {
            return 2;
        } else if (ev.isControlDown()) {
            return 1.5;
        } else if (ev.isAltDown()) {
            return 0.5;
        } else {
            return 1;
        }
    }

    private void nextDay() {
        for (FoodMember member : members) {
            member.nextDay();
        }
    }

    private void clear(VBox inner) {
        members.clear();
        inner.getChildren().clear();
        addOne(inner);
    }

    private void addOne(VBox root) {
        this.addOne(root, new FoodMember());
    }

    private void addOne(VBox root, FoodMember member) {
        HBox line = new HBox(10);
        line.setAlignment(Pos.TOP_LEFT);
        members.add(member);

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        TextField name = new TextField();
        name.textProperty().bindBidirectional(member.nameProperty());
        box.getChildren().add(name);

        Button remove = new Button();
        remove.textProperty().bind(getMessageProperty("helper.food.button.remove"));
        remove.setPrefWidth(70);
        remove.setOnAction(ev -> {
            members.remove(member);
            root.getChildren().remove(line);
        });

        box.getChildren().add(remove);

        line.getChildren().add(box);

        line.getChildren().add(createButtonBox(member::eat, member.eatenFoodProperty()));
        line.getChildren().add(createButtonBox(member::drink, member.drunkDrinkingProperty()));

        TextField hunger = new TextField();
        hunger.setPrefWidth(149);
        hunger.setEditable(false);
        hunger.textProperty().bind(member.starvationProperty());
        line.getChildren().add(hunger);

        TextField thirst = new TextField();
        thirst.setPrefWidth(149);
        thirst.setEditable(false);
        thirst.textProperty().bind(member.thirstProperty());
        line.getChildren().add(thirst);

        ComboBox<FoodRequirement> neededFood = new ComboBox<>();
        neededFood.setPrefWidth(149);
        neededFood.setItems(FXCollections.observableArrayList(FoodRequirement.values()));
        neededFood.setButtonCell(new UpdatingListCell<>());
        neededFood.setCellFactory(list -> new UpdatingListCell<>());
        member.requiredFoodProperty().bind(neededFood.getSelectionModel().selectedItemProperty());
        neededFood.getSelectionModel().select(FoodRequirement.normal);
        line.getChildren().add(neededFood);

        ComboBox<FoodRequirement> neededDrinking = new ComboBox<>();
        neededDrinking.setPrefWidth(149);
        neededDrinking.setItems(FXCollections.observableArrayList(FoodRequirement.values()));
        neededDrinking.setButtonCell(new UpdatingListCell<>());
        neededDrinking.setCellFactory(list -> new UpdatingListCell<>());
        member.neededDrinkingProperty().bind(neededDrinking.getSelectionModel().selectedItemProperty());
        neededDrinking.getSelectionModel().select(FoodRequirement.normal);
        line.getChildren().add(neededDrinking);

        root.getChildren().add(line);
    }

    private VBox createButtonBox(Consumer<Double> adder, DoubleProperty prop) {
        VBox box = new VBox(10);

        TextField value = new TextField();
        value.textProperty().bindBidirectional(prop, new NumStringConverter());
        box.getChildren().add(value);

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button add = new Button("+");
        add.setPrefSize(25, 25);
        buttons.getChildren().add(add);

        add.setOnMouseClicked(ev -> adder.accept(ctrlValues(ev)));

        Button remove = new Button("-");
        remove.setPrefSize(25, 25);
        buttons.getChildren().add(remove);

        remove.setOnMouseClicked(ev -> adder.accept(-ctrlValues(ev)));

        box.getChildren().add(buttons);

        return box;
    }

    private HBox nameBox(String key) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(149);

        Label name = new Label();
        name.textProperty().bind(getMessageProperty(key));
        box.getChildren().add(name);

        return box;
    }
}