package de.pnp.manager.ui.network;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.manager.CharacterHandler;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NetworkView extends ViewPart {

    public NetworkView(IView parent, NetworkHandler networkHandler, CharacterHandler characterHandler) {
        super("network.tabname", parent);

        TableView<Client> clientTable = new TableView<>();
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clientTable.setItems(networkHandler.clientsProperty());

        TableColumn<Client, String> nameColumn = new TableColumn<>();
        nameColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.name"));
        nameColumn.setCellValueFactory(c -> c.getValue().clientNameProperty());
        clientTable.getColumns().add(nameColumn);

        this.setContent(clientTable);
    }
}
