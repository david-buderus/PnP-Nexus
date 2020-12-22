package ui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import ui.part.NumberField;

public abstract class ViewFactory {

    public static HBox labelTextField(String text, StringProperty property) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        Label name = new Label(text);
        name.setPrefWidth(60);
        box.getChildren().add(name);

        TextField field = new TextField();
        field.setPrefWidth(150);
        field.textProperty().bindBidirectional(property);
        box.getChildren().add(field);

        return box;
    }


    public static HBox labelTextField(String text, Property<Number> property) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        Label name = new Label(text);
        name.setPrefWidth(60);
        box.getChildren().add(name);

        NumberField field = new NumberField();
        field.setPrefWidth(150);
        field.numberProperty().bindBidirectional(property);
        box.getChildren().add(field);

        return box;
    }

    public static HBox labelShortTextField(String text, IntegerProperty property) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        Label name = new Label(text);
        name.setPrefWidth(150);
        box.getChildren().add(name);

        NumberField field = new NumberField();
        field.setPrefWidth(30);
        field.numberProperty().bindBidirectional(property);
        box.getChildren().add(field);

        return box;
    }

    public static HBox labelShortTextField(String text, ReadOnlyIntegerProperty property, IntegerProperty modifier) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        Label name = new Label(text);
        name.setPrefWidth(150);
        box.getChildren().add(name);

        NumberField field = new NumberField();
        field.setPrefWidth(30);
        field.setEditable(false);
        field.numberProperty().bind(property);
        box.getChildren().add(field);

        NumberField mField = new NumberField();
        mField.setPrefWidth(30);
        mField.numberProperty().bindBidirectional(modifier);
        box.getChildren().add(mField);

        return box;
    }

    public static HBox labelTextField(String text, IntegerProperty property1, IntegerProperty property2) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        Label name = new Label(text);
        name.setPrefWidth(60);
        box.getChildren().add(name);

        HBox fieldBox = new HBox();
        fieldBox.setAlignment(Pos.CENTER);

        NumberField field1 = new NumberField();
        field1.setPrefWidth(70);
        field1.numberProperty().bindBidirectional(property1);
        fieldBox.getChildren().add(field1);

        Label mid = new Label("/");
        mid.setAlignment(Pos.CENTER);
        mid.setPrefWidth(10);
        fieldBox.getChildren().add(mid);

        NumberField field2 = new NumberField();
        field2.setPrefWidth(70);
        field2.numberProperty().bindBidirectional(property2);
        fieldBox.getChildren().add(field2);

        box.getChildren().add(fieldBox);

        return box;
    }

    public static HBox labelRegion(String text, Region region) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        Label name = new Label(text);
        name.setPrefWidth(60);
        box.getChildren().add(name);

        region.setPrefWidth(150);
        box.getChildren().add(region);

        return box;
    }

    public static HBox labelRegion(String text, int space1, Region region1, int space2, Region region2) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        Label name = new Label(text);
        name.setPrefWidth(60);
        box.getChildren().add(name);

        HBox regionBox = new HBox();
        box.getChildren().add(regionBox);

        region1.setPrefWidth(space1);
        regionBox.getChildren().add(region1);

        region2.setPrefWidth(space2);
        regionBox.getChildren().add(region2);

        return box;
    }
}
