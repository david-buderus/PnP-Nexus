package ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import manager.DatabaseLoader;
import manager.Utility;
import org.apache.commons.lang.exception.ExceptionUtils;
import ui.battle.BattleView;
import ui.map.MapView;
import ui.search.SearchOverview;
import ui.utility.InconsistencyView;
import ui.utility.InfoView;
import ui.utility.MemoryView;
import ui.utility.SQLView;
import ui.utility.helper.HelperOverview;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManagerView extends View {

    public ManagerView(Stage stage) {
        super(stage, new SimpleStringProperty("Keine Datei ausgew\u00e4hlt"));

        stage.setTitle("P&P Manager");

        int width = 400;

        TabPane root = new TabPane();

        Tab startTab = new Tab("Start");
        startTab.setClosable(false);
        root.getTabs().add(startTab);

        Tab battleTab = new BattleView(this);
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

        Tab inconsistencyTab = new InconsistencyView(this);
        root.getTabs().add(inconsistencyTab);


        //First Menu
        VBox startPane = new VBox(10);
        startPane.setPadding(new Insets(10, 20, 20, 20));
        startPane.setAlignment(Pos.CENTER);
        startTab.setContent(startPane);

        HBox titleLine = new HBox(10);
        titleLine.setPadding(new Insets(0, 0, 10, 0));
        titleLine.setAlignment(Pos.CENTER);

        Label fileText = new Label();
        fileText.setPrefWidth((int) (width * (2.0 / 3)) - 10);
        fileText.textProperty().bindBidirectional(fileName);

        titleLine.getChildren().add(fileText);

        Button loadButton = new Button("Laden");
        loadButton.setPrefWidth(width - ((int) (width * (2.0 / 3)) - 10));
        loadButton.setOnAction(ev -> load());

        titleLine.getChildren().add(loadButton);

        startPane.getChildren().add(titleLine);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private InfoView info;

    private void load() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("Accessdatei", "*.accdb"),
                new ExtensionFilter("Alle Dateien", "*.*"));
        File file = chooser.showOpenDialog(stage);

        if (file == null) {
            return;
        }

        load(file);
    }

    public void load(File file) {
        this.info = new InfoView("Ladefehler");
        this.fileName.set("Lädt...");

        Service<Object> service = new Service<>() {
            @Override
            protected Task<Object> createTask() {
                return new Task<>() {
                    @Override
                    protected Object call() {

                        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + file.getPath())) {
                            info.addAll(DatabaseLoader.loadDatabase(connection));
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
            this.fileName.set(file.getName());
            if (!info.isEmpty())
                info.show();
        });
        service.setOnCancelled(ev -> {
            this.fileName.set("Datei konnte nicht geladen werden");
            if (!info.isEmpty())
                info.show();
        });
        service.start();
    }
}
