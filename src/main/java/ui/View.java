package ui;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class View implements IView {

    protected static Image ICON_IMAGE = new Image("Icon.png");

    protected Stage stage;

    public View() {
        this(new Stage());
    }

    public View(Stage stage) {
        this.stage = stage;
        this.stage.getIcons().add(ICON_IMAGE);
        this.stage.setOnCloseRequest(ev -> onClose());
    }

    protected void onClose() {
    }

    public void show() {
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }
}
