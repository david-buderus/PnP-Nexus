package ui.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import ui.View;

public class InfoView extends View {

    private StringProperty info;

    public InfoView(String title) {
        super();
        this.stage.setTitle(title);
        this.info = new SimpleStringProperty("");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label label = new Label();
        label.textProperty().bindBidirectional(info);
        label.setFont(Font.font(10));
        root.setLeft(label);

        Scene scene = new Scene(root);
        this.stage.setScene(scene);
    }

    public boolean isEmpty() {
        return info.get().isEmpty();
    }

    public void add(String s) {
        info.set(info.get() + s + "\n");
    }
}
