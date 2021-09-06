package ui.utility;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import manager.LanguageUtility;
import manager.UpdateChecker;
import manager.Utility;
import ui.View;

public class UpdateView  extends View {

    public UpdateView(UpdateChecker.UpdateResponse response) {
        super("update.title");

        VBox root = new VBox(5);
        root.setPadding(new Insets(20));

        Label label = new Label();
        label.setWrapText(true);
        label.setPadding(new Insets(0, 0, 50, 0));
        label.setText(LanguageUtility.getMessage("update.intro") + " " + response.newVersion + "\n\n" + response.info);
        root.getChildren().add(label);

        Button skip = new Button();
        skip.setMaxWidth(Double.MAX_VALUE);
        skip.textProperty().bind(LanguageUtility.getMessageProperty("update.button.skip"));
        skip.setOnAction(ev -> {
            Utility.saveToCustomConfig("version.skip", response.newVersion);
            close();
        });
        root.getChildren().add(skip);

        Button remindMe = new Button();
        remindMe.setMaxWidth(Double.MAX_VALUE);
        remindMe.textProperty().bind(LanguageUtility.getMessageProperty("update.button.remindMe"));
        remindMe.setOnAction(ev -> close());
        root.getChildren().add(remindMe);

        Scene scene = new Scene(root);
        this.stage.setScene(scene);
    }
}
