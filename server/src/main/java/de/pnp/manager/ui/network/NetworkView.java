package de.pnp.manager.ui.network;

import de.pnp.manager.main.CopyService;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.WorkbookService;
import de.pnp.manager.model.character.GeneratedCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.PnPCharacterFactory;
import de.pnp.manager.model.manager.CharacterHandler;
import de.pnp.manager.model.manager.InventoryHandler;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.character.AssignCharactersMessage;
import de.pnp.manager.network.message.inventory.AssignInventoryMessage;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import de.pnp.manager.ui.battle.CharacterView;
import de.pnp.manager.ui.battle.SpawnView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class NetworkView extends ViewPart {

    protected String sessionID;
    protected NetworkHandler networkHandler;
    protected CharacterHandler characterHandler;
    protected InventoryHandler inventoryHandler;
    protected ObjectProperty<PnPCharacter> selectedCharacter;
    protected ObjectProperty<Container> selectedInventory;
    protected ObjectProperty<Client> selectedClient;

    public NetworkView(IView parent, String sessionID,
                       NetworkHandler networkHandler,
                       CharacterHandler characterHandler,
                       InventoryHandler inventoryHandler) {
        super("network.tabname", parent);
        this.sessionID = sessionID;
        this.selectedCharacter = new SimpleObjectProperty<>();
        this.selectedClient = new SimpleObjectProperty<>();
        this.selectedInventory = new SimpleObjectProperty<>();
        this.networkHandler = networkHandler;
        this.characterHandler = characterHandler;
        this.inventoryHandler = inventoryHandler;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        HBox tableBox = new HBox(15);
        tableBox.setAlignment(Pos.CENTER);
        root.setCenter(tableBox);

        TableView<Client> clientTable = new TableView<>();
        clientTable.setPrefSize(400, 400);
        Text clientPlaceholder = new Text();
        clientPlaceholder.textProperty().bind(LanguageUtility.getMessageProperty("network.noPlayers"));
        clientTable.setPlaceholder(clientPlaceholder);
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clientTable.setItems(networkHandler.clientsProperty().filtered(c ->
                c.getCurrentSession() != null && c.getCurrentSession().getSessionID().equals(sessionID))
        );
        selectedClient.bind(clientTable.getSelectionModel().selectedItemProperty());
        tableBox.getChildren().add(clientTable);

        TableColumn<Client, String> playerIDColumn = new TableColumn<>();
        playerIDColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.id"));
        playerIDColumn.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getClientID()));
        clientTable.getColumns().add(playerIDColumn);

        TableColumn<Client, String> clientNameColumn = new TableColumn<>();
        clientNameColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.name"));
        clientNameColumn.setCellValueFactory(c -> c.getValue().clientNameProperty());
        clientTable.getColumns().add(clientNameColumn);

        TabPane assignableTabs = new TabPane();
        tableBox.getChildren().add(assignableTabs);

        Tab characterTab = new Tab();
        characterTab.setClosable(false);
        characterTab.textProperty().bind(LanguageUtility.getMessageProperty("network.tab.characters"));
        assignableTabs.getTabs().add(characterTab);

        TableView<PnPCharacter> characterTable = new TableView<>();
        characterTable.setPrefSize(400, 400);
        selectedCharacter.bind(characterTable.getSelectionModel().selectedItemProperty());
        Text characterPlaceholder = new Text();
        characterPlaceholder.textProperty().bind(LanguageUtility.getMessageProperty("network.noCharacters"));
        characterTable.setPlaceholder(characterPlaceholder);
        characterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        characterTable.setItems(characterHandler.getCharacters(sessionID));
        characterTab.setContent(characterTable);

        characterTable.setRowFactory( tv -> {
            TableRow<PnPCharacter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    PnPCharacter character = row.getItem();
                    new CharacterView(character).show();
                }
            });
            return row ;
        });

        TableColumn<PnPCharacter, String> characterIDColumn = new TableColumn<>();
        characterIDColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.id"));
        characterIDColumn.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCharacterID()));
        characterTable.getColumns().add(characterIDColumn);

        TableColumn<PnPCharacter, String> characterNameColumn = new TableColumn<>();
        characterNameColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.name"));
        characterNameColumn.setCellValueFactory(c -> c.getValue().nameProperty());
        characterTable.getColumns().add(characterNameColumn);

        Tab inventoryTab = new Tab();
        inventoryTab.setClosable(false);
        inventoryTab.textProperty().bind(LanguageUtility.getMessageProperty("network.tab.inventories"));
        assignableTabs.getTabs().add(inventoryTab);

        TableView<Container> inventoryTable = new TableView<>();
        inventoryTable.setPrefSize(400, 400);
        selectedInventory.bind(inventoryTable.getSelectionModel().selectedItemProperty());
        Text inventoryPlaceholder = new Text();
        inventoryPlaceholder.textProperty().bind(LanguageUtility.getMessageProperty("network.noInventories"));
        inventoryTable.setPlaceholder(inventoryPlaceholder);
        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        inventoryTable.setItems(inventoryHandler.getContainers(sessionID));
        inventoryTab.setContent(inventoryTable);

        inventoryTable.setRowFactory( tv -> {
            TableRow<Container> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    System.out.println(row.getItem().getInventory());
                }
            });
            return row ;
        });

        TableColumn<Container, String> inventoryIDColumn = new TableColumn<>();
        inventoryIDColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.id"));
        inventoryIDColumn.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getInventoryID()));
        inventoryTable.getColumns().add(inventoryIDColumn);

        TableColumn<Container, String> inventoryNameColumn = new TableColumn<>();
        inventoryNameColumn.textProperty().bind(LanguageUtility.getMessageProperty("column.name"));
        inventoryNameColumn.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getName()));
        inventoryTable.getColumns().add(inventoryNameColumn);

        VBox controlBox = new VBox(5);
        root.setRight(controlBox);

        Button assign = new Button();
        assign.textProperty().bind(LanguageUtility.getMessageProperty("network.button.assign"));
        assign.setPrefWidth(200);
        assign.disableProperty().bind(selectedClient.isNull()
                .or(selectedCharacter.isNull().and(assignableTabs.getSelectionModel().selectedItemProperty().isEqualTo(characterTab)))
                .or(selectedInventory.isNull().and(assignableTabs.getSelectionModel().selectedItemProperty().isEqualTo(inventoryTab)))
        );
        assign.setOnAction(ev -> {
            if (selectedClient.get() != null) {

                if (assignableTabs.getSelectionModel().getSelectedItem() == characterTab) {
                    if (selectedCharacter.get() != null) {
                        selectedClient.get().sendActiveMessage(
                                new AssignCharactersMessage(selectedCharacter.get(), Calendar.getInstance().getTime())
                        );
                    }
                } else if (assignableTabs.getSelectionModel().getSelectedItem() == inventoryTab) {
                    if (selectedInventory.get() != null) {
                        selectedClient.get().sendActiveMessage(
                                new AssignInventoryMessage(selectedInventory.get(), Calendar.getInstance().getTime())
                        );
                    }
                }
            }
        });
        controlBox.getChildren().add(assign);

        Button loadPlayer = new Button();
        loadPlayer.textProperty().bind(LanguageUtility.getMessageProperty("network.button.load.player"));
        loadPlayer.setPrefWidth(200);
        loadPlayer.setOnAction(ev -> loadPlayer());
        controlBox.getChildren().add(loadPlayer);

        Button loadCharacter = new Button();
        loadCharacter.textProperty().bind(LanguageUtility.getMessageProperty("network.button.load.character"));
        loadCharacter.setPrefWidth(200);
        loadCharacter.setOnAction(ev -> loadCharacter());
        controlBox.getChildren().add(loadCharacter);

        Button spawnCharacter = new Button();
        spawnCharacter.textProperty().bind(LanguageUtility.getMessageProperty("network.button.spawn.character"));
        spawnCharacter.setPrefWidth(200);
        controlBox.getChildren().add(spawnCharacter);
        spawnCharacter.setOnAction(ev -> new SpawnView(1, spawnParameters -> {
            for (SpawnView.SpawnParameter parameter : spawnParameters) {
                characterHandler.createCharacter(sessionID, null, (characterID, battle) ->
                        new GeneratedCharacter(characterID, battle, parameter.level, parameter.characterisation,
                                parameter.race, parameter.profession, parameter.fightingStyle, parameter.specialisation));
            }
        }));

        Button spawnInventory = new Button();
        spawnInventory.textProperty().bind(LanguageUtility.getMessageProperty("network.button.spawn.inventory"));
        spawnInventory.setPrefWidth(200);
        spawnInventory.setOnAction(ev -> new InventorySpawningView(sessionID, inventoryHandler));
        controlBox.getChildren().add(spawnInventory);


        Button deleteCharacter = new Button();
        deleteCharacter.textProperty().bind(LanguageUtility.getMessageProperty("network.button.delete"));
        deleteCharacter.setPrefWidth(200);
        deleteCharacter.disableProperty().bind(
                selectedCharacter.isNull().and(assignableTabs.getSelectionModel().selectedItemProperty().isEqualTo(characterTab))
                .or(selectedInventory.isNull().and(assignableTabs.getSelectionModel().selectedItemProperty().isEqualTo(inventoryTab)))
        );
        controlBox.getChildren().add(deleteCharacter);
        deleteCharacter.setOnAction(ev -> {

            if (assignableTabs.getSelectionModel().getSelectedItem() == characterTab) {
                if (selectedCharacter.get() != null) {
                    characterHandler.deleteCharacter(sessionID, selectedCharacter.get().getCharacterID());
                }
            } else if (assignableTabs.getSelectionModel().getSelectedItem() == inventoryTab) {
                if (selectedInventory.get() != null) {
                    inventoryHandler.deleteContainer(sessionID, selectedInventory.get().getInventoryID());
                }
            }
        });

        this.setContent(root);
    }

    protected void load(EventHandler<WorkerStateEvent> handler) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(LanguageUtility.getMessage("excelFile"), "*.xlsx"),
                new FileChooser.ExtensionFilter(LanguageUtility.getMessage("allFiles"), "*.*"));

        File file = chooser.showOpenDialog(getStage());

        if (file == null) {
            return;
        }

        CopyService copy = new CopyService();
        copy.setFile(file);
        copy.setOnSucceeded(handler);
        copy.start();
    }

    protected void loadPlayer() {
        load(ev1 -> {

            File temp = (File) ev1.getSource().getValue();

            WorkbookService service = new WorkbookService();
            service.setFile(temp);
            service.setOnSucceeded(ev2 -> {
                Workbook wb = (Workbook) ev2.getSource().getValue();

                characterHandler.createCharacter(sessionID, null, (characterID, battle) ->
                        PnPCharacterFactory.createPlayer(characterID, battle, wb)
                );

                try {
                    wb.close();
                    if (!temp.delete()) {
                        throw new IOException("not closed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            service.start();
        });
    }

    protected void loadCharacter() {
        load(ev1 -> {

            File temp = (File) ev1.getSource().getValue();

            WorkbookService service = new WorkbookService();
            service.setFile(temp);
            service.setOnSucceeded(ev2 -> {
                Workbook wb = (Workbook) ev2.getSource().getValue();

                characterHandler.createCharacter(sessionID, null, (characterID, battle) ->
                        PnPCharacterFactory.createCharacter(characterID, battle, wb)
                );

                try {
                    wb.close();
                    if (!temp.delete()) {
                        throw new IOException("not closed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            service.start();
        });
    }
}
