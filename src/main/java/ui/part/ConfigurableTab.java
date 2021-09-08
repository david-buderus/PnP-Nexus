package ui.part;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

public class ConfigurableTab extends Tab {

    protected StringProperty name;

    public ConfigurableTab() {
        super();
        this.name = new SimpleStringProperty("");

        Label label = new Label();
        label.textProperty().bind(nameProperty());
        setGraphic(label);

        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(nameProperty());

        label.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                setGraphic(textField);
                textField.selectAll();
                textField.requestFocus();
            }
        });

        textField.setOnAction(event -> setGraphic(label));

        textField.focusedProperty().addListener((ob, o, n) -> {
            if (!n) {
                setGraphic(label);
            }
        });
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
