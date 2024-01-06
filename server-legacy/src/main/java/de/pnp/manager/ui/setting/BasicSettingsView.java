package de.pnp.manager.ui.setting;

import de.pnp.manager.main.DatabaseLoader;
import de.pnp.manager.main.Language;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.NexusExporter;
import de.pnp.manager.main.UpdateChecker;
import de.pnp.manager.main.Utility;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import de.pnp.manager.ui.utility.InfoView;
import de.pnp.manager.ui.utility.UpdateView;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.apache.commons.lang.exception.ExceptionUtils;

public class BasicSettingsView extends ViewPart {

    protected StringProperty fileName;
    protected StringProperty defaultPath;

    public BasicSettingsView(IView parent, Application application) {
        super("settings.basic", parent);
        this.fileName = new SimpleStringProperty();
        this.fileName.bind(LanguageUtility.getMessageProperty("manager.noFile"));
        this.defaultPath = new SimpleStringProperty(Utility.getConfig().getString("home.defaultLoadingPath", ""));
        this.defaultPath.addListener((ob, o, n) -> Utility.saveToCustomConfig("home.defaultLoadingPath", n));

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

        this.setContent(settingsPane);

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

        Label urlText = new Label();
        urlText.setText("Url");
        settingsPane.add(urlText, 0, 5);

        TextField urlField = new TextField();
        urlField.setMaxWidth(Double.MAX_VALUE);
        urlField.setText("http://localhost:8080/");
        settingsPane.add(urlField, 1, 5);

        Label usernameText = new Label();
        usernameText.setText("Username");
        settingsPane.add(usernameText, 0, 6);

        TextField usernameField = new TextField();
        usernameField.setMaxWidth(Double.MAX_VALUE);
        settingsPane.add(usernameField, 1, 6);

        Label passwordText = new Label();
        passwordText.setText("Password");
        settingsPane.add(passwordText, 0, 7);

        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(Double.MAX_VALUE);
        settingsPane.add(passwordField, 1, 7);

        Label universeText = new Label();
        universeText.setText("Universe");
        settingsPane.add(universeText, 0, 8);

        TextField universeField = new TextField();
        universeField.setMaxWidth(Double.MAX_VALUE);
        settingsPane.add(universeField, 1, 8);

        Button exportButton = new Button();
        exportButton.setText("Export to Nexus");
        exportButton.setOnAction(
            ev -> NexusExporter.export(urlField.getText(), universeField.getText(), usernameField.getText(),
                passwordField.getText()));
        exportButton.setMaxWidth(Double.MAX_VALUE);
        settingsPane.add(exportButton, 0, 9, 2, 1);

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
            new FileChooser.ExtensionFilter(LanguageUtility.getMessage("accessFile"), "*.accdb"),
            new FileChooser.ExtensionFilter(LanguageUtility.getMessage("allFiles"), "*.*"));
        File file = chooser.showOpenDialog(getStage());

        if (file == null) {
            return;
        }

        load(file);
    }

    private void setDefaultPath() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(LanguageUtility.getMessage("accessFile"), "*.accdb"),
            new FileChooser.ExtensionFilter(LanguageUtility.getMessage("allFiles"), "*.*"));
        File file = chooser.showOpenDialog(getStage());

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

                        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + file.getPath(),
                            properties)) {
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
            if (!info.isEmpty()) {
                info.show();
            }
        });
        service.start();
    }

}
