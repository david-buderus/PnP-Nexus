package ui;

import javafx.stage.Stage;
import ui.part.ConfigurableTab;

public class ConfigurableViewPart extends ConfigurableTab implements IView {

    protected IView parent;

    public ConfigurableViewPart(IView parent) {
        super();
        this.parent = parent;
        this.setClosable(false);
    }

    @Override
    public Stage getStage() {
        return this.parent.getStage();
    }
}
