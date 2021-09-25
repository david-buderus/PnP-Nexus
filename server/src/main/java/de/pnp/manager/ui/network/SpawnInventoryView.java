package de.pnp.manager.ui.network;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.manager.InventoryHandler;
import de.pnp.manager.ui.View;
import de.pnp.manager.ui.ViewFactory;
import de.pnp.manager.ui.part.NumberField;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SpawnInventoryView extends View {

    public SpawnInventoryView(String sessionID, InventoryHandler handler) {
        super("spawn.title");
        StringProperty name = new SimpleStringProperty(LanguageUtility.getMessage("inventory.spawn.text.default"));
        IntegerProperty amountOfSlots = new SimpleIntegerProperty(10);
        DoubleProperty maxSize = new SimpleDoubleProperty(100);

        VBox root = new VBox(5);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        root.getChildren().add(ViewFactory.labelTextField("inventory.spawn.name", name));
        root.getChildren().add(ViewFactory.labelTextField("inventory.spawn.amountOfSlots", amountOfSlots));
        root.getChildren().add(ViewFactory.labelTextField("inventory.spawn.maxSize", maxSize));

        Button spawn = new Button();
        spawn.textProperty().bind(LanguageUtility.getMessageProperty("inventory.spawn.button.spawn"));
        spawn.setPrefWidth(215);
        root.getChildren().add(spawn);

        spawn.setOnAction(ev -> {
            handler.createContainer(sessionID, name.get(), new Inventory(maxSize.get(), amountOfSlots.get()));
            close();
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        show();
    }
}
