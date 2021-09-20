package de.pnp.manager.ui.network;

import de.pnp.manager.main.CopyService;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.WorkbookService;
import de.pnp.manager.model.character.GeneratedCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.PnPCharacterFactory;
import de.pnp.manager.model.manager.CharacterHandler;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.character.AssignCharactersMessage;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
    protected ObjectProperty<PnPCharacter> selectedCharacter;
    protected ObjectProperty<Client> selectedClient;

    public NetworkView(IView parent, String sessionID, NetworkHandler networkHandler, CharacterHandler characterHandler) {
        super("network.tabname", parent);
        this.sessionID = sessionID;
        this.selectedCharacter = new SimpleObjectProperty<>();
        this.selectedClient = new SimpleObjectProperty<>();
        this.networkHandler = networkHandler;
        this.characterHandler = characterHandler;

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

        TableView<PnPCharacter> characterTable = new TableView<>();
        characterTable.setPrefSize(400, 400);
        selectedCharacter.bind(characterTable.getSelectionModel().selectedItemProperty());
        Text characterPlaceholder = new Text();
        characterPlaceholder.textProperty().bind(LanguageUtility.getMessageProperty("network.noCharacters"));
        characterTable.setPlaceholder(characterPlaceholder);
        characterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        characterTable.setItems(characterHandler.getCharacters(sessionID));
        tableBox.getChildren().add(characterTable);

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

        VBox controlBox = new VBox(5);
        root.setRight(controlBox);

        Button assign = new Button();
        assign.textProperty().bind(LanguageUtility.getMessageProperty("network.button.assign"));
        assign.setPrefWidth(200);
        assign.disableProperty().bind(selectedCharacter.isNull().or(selectedClient.isNull()));
        assign.setOnAction(ev -> {
            if (selectedClient.get() != null && selectedCharacter.get() != null) {
                selectedClient.get().sendActiveMessage(
                        new AssignCharactersMessage(selectedCharacter.get(), Calendar.getInstance().getTime())
                );
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
        spawnCharacter.textProperty().bind(LanguageUtility.getMessageProperty("network.button.spawn"));
        spawnCharacter.setPrefWidth(200);
        controlBox.getChildren().add(spawnCharacter);
        spawnCharacter.setOnAction(ev -> new SpawnView(1, spawnParameters -> {
            for (SpawnView.SpawnParameter parameter : spawnParameters) {
                characterHandler.createCharacter(sessionID, null, (characterID, battle) ->
                        new GeneratedCharacter(characterID, battle, parameter.level, parameter.characterisation,
                                parameter.race, parameter.profession, parameter.fightingStyle, parameter.specialisation));
            }
        }));


        Button deleteCharacter = new Button();
        deleteCharacter.textProperty().bind(LanguageUtility.getMessageProperty("network.button.delete"));
        deleteCharacter.setPrefWidth(200);
        controlBox.getChildren().add(deleteCharacter);
        deleteCharacter.setOnAction(ev -> {
            if (selectedCharacter.get() != null) {
                characterHandler.deleteCharacter(sessionID, selectedCharacter.get().getCharacterID());
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
