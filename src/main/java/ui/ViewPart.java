package ui;

import javafx.scene.control.Tab;
import javafx.stage.Stage;
import manager.LanguageUtility;

public class ViewPart extends Tab implements IView {

    protected IView parent;

    public ViewPart(String title, IView parent) {
        super();
        this.textProperty().bind(LanguageUtility.getMessageProperty(title));
        this.parent = parent;
        this.setClosable(false);
    }

    @Override
    public Stage getStage() {
        return this.parent.getStage();
    }
}
