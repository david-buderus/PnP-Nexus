package de.pnp.manager.ui;

import de.pnp.manager.main.LanguageUtility;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class ViewPart extends Tab implements IView {

    protected IView parent;

    public ViewPart(String title, IView parent) {
        this(parent);
        this.textProperty().bind(LanguageUtility.getMessageProperty(title));
    }

    public ViewPart(IView parent) {
        super();
        this.parent = parent;
        this.setClosable(false);
    }

    @Override
    public Stage getStage() {
        return this.parent.getStage();
    }
}
