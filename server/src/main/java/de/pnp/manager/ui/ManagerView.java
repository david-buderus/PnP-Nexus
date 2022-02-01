package de.pnp.manager.ui;

import de.pnp.manager.main.*;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.session.ISession;
import de.pnp.manager.ui.battle.BattleOverview;
import de.pnp.manager.ui.map.MapView;
import de.pnp.manager.ui.network.NetworkView;
import de.pnp.manager.ui.search.SearchOverview;
import de.pnp.manager.ui.setting.SettingsOverview;
import de.pnp.manager.ui.utility.*;
import de.pnp.manager.ui.utility.helper.HelperOverview;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ManagerView extends View {

    protected final Manager manager;
    protected SettingsOverview settingsTab;

    public ManagerView(Stage stage, Application application) {
        super("manager.title", stage);
        this.manager = new Manager();

        TabPane root = new TabPane();

        Tab networkTab = new NetworkView(this,
                manager.getNetworkHandler().getActiveSessions().stream().map(ISession::getSessionID).findFirst().orElse(""),
                manager.getNetworkHandler(), manager.getCharacterHandler(), manager.getInventoryHandler()
        );
        root.getTabs().add(networkTab);

        Tab battleTab = new BattleOverview(this, manager.getBattleHandler(), manager.getNetworkHandler());
        root.getTabs().add(battleTab);

        Tab itemTab = new SQLView(this);
        root.getTabs().add(itemTab);

        Tab searchOverviewTab = new SearchOverview(this);
        root.getTabs().add(searchOverviewTab);

        Tab helpOverviewTab = new HelperOverview(this);
        root.getTabs().add(helpOverviewTab);

        MemoryView memoryTab = new MemoryView(this);
        Utility.memoryView = memoryTab;
        root.getTabs().add(memoryTab);

        Tab mapTab = new MapView(this);
        root.getTabs().add(mapTab);

        settingsTab = new SettingsOverview(this, application);
        root.getTabs().add(settingsTab);

        Tab inconsistencyTab = new InconsistencyView(this);
        root.getTabs().add(inconsistencyTab);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    protected void onClose() {
        super.onClose();
        try {
            this.manager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(File file) {
        settingsTab.load(file);
    }
}
