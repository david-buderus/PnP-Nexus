package de.pnp.manager.ui;

import de.pnp.manager.ui.part.ConfigurableTab;
import javafx.stage.Stage;

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
