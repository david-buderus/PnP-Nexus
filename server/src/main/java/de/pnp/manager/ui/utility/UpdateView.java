package de.pnp.manager.ui.utility;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.UpdateChecker;
import de.pnp.manager.main.Utility;
import de.pnp.manager.ui.View;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UpdateView extends View {

    public UpdateView(UpdateChecker.UpdateResponse response, Application application) {
        super("update.title");

        VBox root = new VBox(50);
        root.setPadding(new Insets(20));

        VBox textBox = new VBox(5);
        root.getChildren().add(textBox);

        Label label = new Label();
        label.setWrapText(true);
        label.setText(LanguageUtility.getMessage("update.intro") + " " + response.newVersion + "\n\n" + response.info);
        textBox.getChildren().add(label);

        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setText(response.url);
        hyperlink.setOnAction(ev -> application.getHostServices().showDocument(response.url));
        textBox.getChildren().add(hyperlink);

        VBox buttonBox = new VBox(5);
        root.getChildren().add(buttonBox);

        Button skip = new Button();
        skip.setMaxWidth(Double.MAX_VALUE);
        skip.textProperty().bind(LanguageUtility.getMessageProperty("update.button.skip"));
        skip.setOnAction(ev -> {
            Utility.saveToCustomConfig("version.skip", response.newVersion);
            close();
        });
        buttonBox.getChildren().add(skip);

        Button remindMe = new Button();
        remindMe.setMaxWidth(Double.MAX_VALUE);
        remindMe.textProperty().bind(LanguageUtility.getMessageProperty("update.button.remindMe"));
        remindMe.setOnAction(ev -> close());
        buttonBox.getChildren().add(remindMe);

        Scene scene = new Scene(root);
        this.stage.setScene(scene);
    }
}
