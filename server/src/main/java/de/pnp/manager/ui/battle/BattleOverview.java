package de.pnp.manager.ui.battle;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;

public class BattleOverview extends ViewPart {

    public BattleOverview(IView parent) {
        super("battle.tabName", parent);

        TabPane root = new TabPane();

        Tab battleTab = new BattleView(this);
        root.getTabs().add(battleTab);

        Tab addTab = new Tab(" + ");
        addTab.setClosable(false);
        root.getTabs().add(addTab);

        root.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            if (n.equals(addTab)) {
                Tab newTab = new BattleView(this);
                root.getTabs().add(root.getTabs().size() - 1, newTab);
                root.getSelectionModel().select(newTab);
            }
        });

        this.setContent(root);
    }
}
