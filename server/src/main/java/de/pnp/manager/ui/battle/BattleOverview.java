package de.pnp.manager.ui.battle;

import de.pnp.manager.model.manager.BattleHandler;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class BattleOverview extends ViewPart {

    public BattleOverview(IView parent, BattleHandler battleHandler, NetworkHandler networkHandler) {
        super("battle.tabName", parent);

        TabPane root = new TabPane();

        BattleView battleTab = new BattleView(this, battleHandler);
        battleTab.setOnClosed(ev -> battleHandler.deleteBattle(battleTab.getBattle().getBattleID()));
        root.getTabs().add(battleTab);

        Tab addTab = new Tab(" + ");
        addTab.setClosable(false);
        root.getTabs().add(addTab);

        root.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            if (n.equals(addTab)) {
                Tab newTab = new BattleView(this, battleHandler);
                newTab.setOnClosed(ev -> battleHandler.deleteBattle(battleTab.getBattle().getBattleID()));
                root.getTabs().add(root.getTabs().size() - 1, newTab);
                root.getSelectionModel().select(newTab);
            }
        });

        this.setContent(root);
    }
}
