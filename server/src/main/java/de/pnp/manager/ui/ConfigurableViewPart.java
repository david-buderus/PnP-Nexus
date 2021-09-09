package de.pnp.manager.ui;

import javafx.stage.Stage;
import de.pnp.manager.ui.part.ConfigurableTab;

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
