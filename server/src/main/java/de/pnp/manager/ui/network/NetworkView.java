package de.pnp.manager.ui.network;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.manager.CharacterHandler;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class NetworkView extends ViewPart {

    protected String sessionID;

    public NetworkView(IView parent, String sessionID, NetworkHandler networkHandler, CharacterHandler characterHandler) {
        super("network.tabname", parent);
        this.sessionID = sessionID;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        HBox tableBox = new HBox(15);
        tableBox.setAlignment(Pos.CENTER);
        root.setCenter(tableBox);

        TableView<Client> clientTable = new TableView<>();
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clientTable.setItems(networkHandler.clientsProperty().filtered(c -> c.getCurrentSession().getSessionID().equals(sessionID)));
        tableBox.getChildren().add(clientTable);

        TableColumn<Client, String> clientNameColumn = new TableColumn<>();
        clientNameColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.name"));
        clientNameColumn.setCellValueFactory(c -> c.getValue().clientNameProperty());
        clientTable.getColumns().add(clientNameColumn);

        TableView<PnPCharacter> characterTable = new TableView<>();
        characterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        characterTable.setItems(characterHandler.getCharacters(sessionID));
        tableBox.getChildren().add(characterTable);

        TableColumn<PnPCharacter, String> characterIDColumn = new TableColumn<>();
        characterIDColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.name"));
        characterIDColumn.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCharacterID()));
        characterTable.getColumns().add(characterIDColumn);

        TableColumn<PnPCharacter, String> characterNameColumn = new TableColumn<>();
        characterNameColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.id"));
        characterNameColumn.setCellValueFactory(c -> c.getValue().nameProperty());
        characterTable.getColumns().add(characterNameColumn);

        this.setContent(root);
    }
}
