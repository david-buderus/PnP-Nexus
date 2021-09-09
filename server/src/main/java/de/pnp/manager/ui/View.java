package de.pnp.manager.ui;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.binding.StringExpression;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class View implements IView {

    protected static Image ICON_IMAGE = new Image("Icon.png");

    protected Stage stage;

    public View(String key) {
        this(key, new Stage());
    }

    public View(String key, StringExpression titleAddition) {
        this(key, titleAddition, new Stage());
    }

    public View(String key, StringExpression titleAddition, Stage stage) {
        this(key, stage);
        this.stage.titleProperty().bind(LanguageUtility.getMessageProperty(key).concat(titleAddition));
    }

    public View(String key, Stage stage) {
        this.stage = stage;
        this.stage.titleProperty().bind(LanguageUtility.getMessageProperty(key));
        this.stage.getIcons().add(ICON_IMAGE);
        this.stage.setOnCloseRequest(ev -> onClose());
    }

    protected void onClose() {
    }

    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }
}
