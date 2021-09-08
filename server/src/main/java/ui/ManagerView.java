package ui;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import manager.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import ui.battle.BattleOverview;
import ui.map.MapView;
import ui.search.SearchOverview;
import ui.utility.*;
import ui.utility.helper.HelperOverview;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ManagerView extends View {

    protected StringProperty fileName;
    protected StringProperty defaultPath;

    public ManagerView(Stage stage, Application application) {
        super("manager.title", stage);
        this.fileName = new SimpleStringProperty();
        this.fileName.bind(LanguageUtility.getMessageProperty("manager.noFile"));
        this.defaultPath = new SimpleStringProperty(Utility.getConfig().getString("home.defaultLoadingPath", ""));
        this.defaultPath.addListener((ob, o, n) -> Utility.saveToCustomConfig("home.defaultLoadingPath", n));

        TabPane root = new TabPane();

        Tab battleTab = new BattleOverview(this);
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

        Tab startTab = new Tab();
        startTab.textProperty().bind(LanguageUtility.getMessageProperty("manager.settings"));
        startTab.setClosable(false);
        root.getTabs().add(startTab);

        Tab inconsistencyTab = new InconsistencyView(this);
        root.getTabs().add(inconsistencyTab);

        GridPane settingsPane = new GridPane();
        settingsPane.setPadding(new Insets(20, 20, 20, 20));
        settingsPane.setAlignment(Pos.CENTER);
        settingsPane.setHgap(10);
        settingsPane.setVgap(5);

        ColumnConstraints textColumn = new ColumnConstraints(250);
        textColumn.setFillWidth(true);
        settingsPane.getColumnConstraints().add(textColumn);
        ColumnConstraints settingsColumn = new ColumnConstraints(250);
        settingsColumn.setFillWidth(true);
        settingsPane.getColumnConstraints().add(settingsColumn);

        startTab.setContent(settingsPane);

        Label fileText = new Label();
        fileText.textProperty().bindBidirectional(fileName);
        settingsPane.add(fileText, 0, 0);

        Button loadButton = new Button();
        loadButton.textProperty().bind(LanguageUtility.getMessageProperty("manager.button.load"));
        loadButton.setMaxWidth(Double.MAX_VALUE);
        loadButton.setOnAction(ev -> load());
        settingsPane.add(loadButton, 1, 0);

        Label defaultFileText = new Label();
        defaultFileText.textProperty().bind(LanguageUtility.getMessageProperty("manager.defaultPath"));
        settingsPane.add(defaultFileText, 0, 1);

        HBox defaultLine = new HBox(5);
        defaultLine.setMaxWidth(Double.MAX_VALUE);
        settingsPane.add(defaultLine, 1, 1);

        TextField defaultTextField = new TextField();
        defaultTextField.textProperty().bindBidirectional(defaultPath);
        defaultTextField.prefWidthProperty().bind(defaultLine.widthProperty().subtract(20));
        defaultTextField.setMaxWidth(Double.MAX_VALUE);
        defaultLine.getChildren().add(defaultTextField);

        Button defaultSetButton = new Button("...");
        defaultSetButton.setPrefWidth(15);
        defaultSetButton.setOnAction(ev -> setDefaultPath());
        defaultLine.getChildren().add(defaultSetButton);

        Label languageText = new Label();
        languageText.textProperty().bind(LanguageUtility.getMessageProperty("language"));
        settingsPane.add(languageText, 0, 2);

        ComboBox<Language> languageBox = new ComboBox<>();
        languageBox.setMaxWidth(Double.MAX_VALUE);
        languageBox.setItems(FXCollections.observableArrayList(Language.values()));
        languageBox.getSelectionModel().select(LanguageUtility.language.get());
        LanguageUtility.language.bind(languageBox.getSelectionModel().selectedItemProperty());
        settingsPane.add(languageBox, 1, 2);

        Label languageTableText = new Label();
        languageTableText.textProperty().bind(LanguageUtility.getMessageProperty("language.table"));
        settingsPane.add(languageTableText, 0, 3);

        ComboBox<Language> languageTableBox = new ComboBox<>();
        languageTableBox.setMaxWidth(Double.MAX_VALUE);
        languageTableBox.setItems(FXCollections.observableArrayList(Language.values()));
        languageTableBox.getSelectionModel().select(DatabaseLoader.tableLanguage.get());
        DatabaseLoader.tableLanguage.bind(languageTableBox.getSelectionModel().selectedItemProperty());
        settingsPane.add(languageTableBox, 1, 3);

        Button checkUpdateButton = new Button();
        checkUpdateButton.textProperty().bind(LanguageUtility.getMessageProperty("manager.button.checkUpdate"));
        checkUpdateButton.setOnAction(ev -> {
            var response = UpdateChecker.checkForUpdates();

            if (response.updateDoesExists) {
                new UpdateView(response, application).show();
                checkUpdateButton.textProperty().bind(LanguageUtility.getMessageProperty("manager.button.checkUpdate"));
            } else {
                checkUpdateButton.textProperty().bind(LanguageUtility.getMessageProperty("manager.button.noUpdate"));
            }
        });
        checkUpdateButton.setMaxWidth(Double.MAX_VALUE);
        settingsPane.add(checkUpdateButton, 0, 4, 2, 1);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        var response = UpdateChecker.checkForUpdates();

        if (response.updateDoesExists) {
            new UpdateView(response, application).show();
            checkUpdateButton.textProperty().bind(LanguageUtility.getMessageProperty("manager.button.checkUpdate"));
        }

        if (!defaultPath.get().isBlank()) {
            load(new File(defaultPath.get()));
        }
    }

    private InfoView info;

    private void load() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new ExtensionFilter(LanguageUtility.getMessage("accessFile"), "*.accdb"),
                new ExtensionFilter(LanguageUtility.getMessage("allFiles"), "*.*"));
        File file = chooser.showOpenDialog(stage);

        if (file == null) {
            return;
        }

        load(file);
    }

    private void setDefaultPath() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new ExtensionFilter(LanguageUtility.getMessage("accessFile"), "*.accdb"),
                new ExtensionFilter(LanguageUtility.getMessage("allFiles"), "*.*"));
        File file = chooser.showOpenDialog(stage);

        if (file == null) {
            return;
        }

        this.defaultPath.set(file.getPath());
    }

    public void load(File file) {
        this.info = new InfoView("manager.loadingError");
        this.fileName.bind(LanguageUtility.getMessageProperty("manager.loading"));

        Service<Object> service = new Service<>() {
            @Override
            protected Task<Object> createTask() {
                return new Task<>() {
                    @Override
                    protected Object call() {
                        Properties properties = new Properties();
                        properties.put("ConnSettings", "SET LOCALE TO de_DE");

                        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + file.getPath(), properties)) {
                            info.addAll(DatabaseLoader.loadDatabase(connection, false));
                        } catch (SQLException e) {
                            info.add(ExceptionUtils.getFullStackTrace(e));
                            this.cancel();
                        } catch (Exception e) {
                            info.add(ExceptionUtils.getFullStackTrace(e));
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded(ev -> {
            this.fileName.unbind();
            this.fileName.set(file.getName());
            if (!info.isEmpty()) {
                info.show();
            }
        });
        service.setOnCancelled(ev -> {
            this.fileName.bind(LanguageUtility.getMessageProperty("manager.fileNotLoaded"));
            if (!info.isEmpty())
                info.show();
        });
        service.start();
    }
}
