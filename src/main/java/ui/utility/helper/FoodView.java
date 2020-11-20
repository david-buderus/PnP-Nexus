package ui.utility.helper;

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
import model.member.FoodMember;
import model.member.data.Requirement;
import ui.IView;
import ui.ViewPart;
import ui.part.NumStringConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FoodView extends ViewPart {
    private List<FoodMember> members;

    public FoodView(IView parent) {
        super("Essen", parent);
        this.members = new ArrayList<>();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 0, 20, 0));

        // Titel
        HBox line = new HBox(10);
        line.setAlignment(Pos.CENTER_LEFT);
        line.setPadding(new Insets(0, 0, 10, 5));

        line.getChildren().add(nameBox("Name"));

        HBox life = nameBox("Essen");
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

        HBox mana = nameBox("Trinken");
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

        line.getChildren().add(nameBox("Hunger"));

        line.getChildren().add(nameBox("Durst"));

        line.getChildren().add(nameBox("Essensverbrauch"));

        line.getChildren().add(nameBox("Trinkenverbrauch"));

        root.setTop(line);

        // Scroll

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scroll.setPadding(new Insets(5, 5, 5, 5));

        VBox inner = new VBox(10);
        inner.setAlignment(Pos.CENTER);
        scroll.setContent(inner);

        root.setCenter(scroll);

        BorderPane buttons = new BorderPane();
        buttons.setPadding(new Insets(15, 5, 5, 5));

        Button addButton = new Button("Hinzuf\u00fcgen");
        addButton.setPrefWidth(100);
        addButton.setOnAction((ev) -> addOne(inner));
        buttons.setRight(addButton);

        Button turnButton = new Button("N\u00e4chster Tag");
        turnButton.setPrefWidth(100);
        turnButton.setOnAction(ev -> nextDay());
        buttons.setCenter(turnButton);

        root.setBottom(buttons);

        Button clearButton = new Button("Leeren");
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
        line.setAlignment(Pos.TOP_CENTER);
        members.add(member);

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        TextField name = new TextField();
        name.textProperty().bindBidirectional(member.nameProperty());
        box.getChildren().add(name);

        Button remove = new Button("Entfernen");
        remove.setPrefWidth(70);
        remove.setOnAction(ev -> {
            members.remove(member);
            root.getChildren().remove(line);
        });

        box.getChildren().add(remove);

        line.getChildren().add(box);

        try {
            line.getChildren().add(createButtonBox(member, FoodMember.class.getMethod("eat", Double.class),
                    member.eatenFoodProperty()));
            line.getChildren().add(createButtonBox(member, FoodMember.class.getMethod("drink", Double.class),
                    member.drunkDrinkingProperty()));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

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

        ComboBox<Requirement> neededFood = new ComboBox<>();
        neededFood.setPrefWidth(149);
        neededFood.setItems(FXCollections.observableArrayList(Requirement.values()));
        member.requiredFoodProperty().bind(neededFood.getSelectionModel().selectedItemProperty());
        neededFood.getSelectionModel().select(Requirement.normal);
        line.getChildren().add(neededFood);

        ComboBox<Requirement> neededDrinking = new ComboBox<>();
        neededDrinking.setPrefWidth(149);
        neededDrinking.setItems(FXCollections.observableArrayList(Requirement.values()));
        member.neededDrinkingProperty().bind(neededDrinking.getSelectionModel().selectedItemProperty());
        neededDrinking.getSelectionModel().select(Requirement.normal);
        line.getChildren().add(neededDrinking);

        root.getChildren().add(line);
    }

    private VBox createButtonBox(FoodMember member, Method adder, DoubleProperty prop) {
        VBox box = new VBox(10);

        TextField value = new TextField();
        value.textProperty().bindBidirectional(prop, new NumStringConverter());
        box.getChildren().add(value);

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button add = new Button("+");
        add.setPrefSize(25, 25);
        buttons.getChildren().add(add);

        add.setOnMouseClicked(ev -> {
            try {
                adder.invoke(member, ctrlValues(ev));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        Button remove = new Button("-");
        remove.setPrefSize(25, 25);
        buttons.getChildren().add(remove);

        remove.setOnMouseClicked(ev -> {
            try {
                adder.invoke(member, -ctrlValues(ev));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        box.getChildren().add(buttons);

        return box;
    }

    private HBox nameBox(String s) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(149);

        Label name = new Label(s);
        box.getChildren().add(name);

        return box;
    }
}