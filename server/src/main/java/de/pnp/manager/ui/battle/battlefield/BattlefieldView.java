package de.pnp.manager.ui.battle.battlefield;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.battle.Battle;
import de.pnp.manager.model.battle.Battlefield;
import de.pnp.manager.model.battle.BattlefieldDetails;
import de.pnp.manager.ui.View;
import de.pnp.manager.ui.part.ZoomableScrollPane;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public class BattlefieldView extends View {

    protected Canvas gridCanvas;
    protected GraphicsContext gridContext;
    protected Canvas fieldCanvas;
    protected GraphicsContext fieldContext;
    protected BorderPane root;
    protected ObservableList<SavedMap> savedMaps;

    protected Battle battle;
    protected Battlefield battlefield;
    protected ObjectMapper mapper;

    public BattlefieldView(Battle battle) {
        super("battlefield.title");

        this.battle = battle;
        this.mapper = new ObjectMapper();

        this.root = new BorderPane();
        root.setPadding(new Insets(10));

        if (battle.getBattlefield() != null) {
            this.battlefield = battle.getBattlefield();
            this.loadBattlefield();
        } else {
            this.loadLoading();
        }

        Scene scene = new Scene(root);
        this.getStage().setScene(scene);
    }

    protected void loadLoading() {
        this.savedMaps = FXCollections.observableArrayList();
        refreshTable();

        VBox tableBox = new VBox(5);
        tableBox.setAlignment(Pos.CENTER);
        root.setCenter(tableBox);

        TableView<SavedMap> mapsTable = new TableView<>();
        mapsTable.setItems(savedMaps);
        mapsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableBox.getChildren().add(mapsTable);

        TableColumn<SavedMap, String> nameCol = new TableColumn<>();
        nameCol.textProperty().bind(getMessageProperty("column.name"));
        nameCol.setCellValueFactory(ob -> new ReadOnlyStringWrapper(ob.getValue().name));
        mapsTable.getColumns().add(nameCol);

        Button refreshButton = new Button();
        refreshButton.textProperty().bind(getMessageProperty("battlefield.button.refresh"));
        refreshButton.prefWidthProperty().bind(mapsTable.widthProperty());
        refreshButton.setOnAction(ev -> refreshTable());
        tableBox.getChildren().add(refreshButton);

        Button selectButton = new Button();
        selectButton.textProperty().bind(getMessageProperty("battlefield.button.select"));
        selectButton.prefWidthProperty().bind(mapsTable.widthProperty());
        selectButton.setOnAction(ev -> load(mapsTable.getSelectionModel().getSelectedItem()));
        selectButton.disableProperty().bind(mapsTable.getSelectionModel().selectedItemProperty().isNull());
        tableBox.getChildren().add(selectButton);
    }

    protected void load(SavedMap map) {
        if (map != null) {
            try {
                BattlefieldDetails details = mapper.readValue(map.path.toFile(), BattlefieldDetails.class);
                battlefield = new Battlefield(battle, details);
                battle.setBattlefield(battlefield);
                loadBattlefield();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void refreshTable() {
        File mapDir = getMapPath().toFile();
        File[] files = mapDir.listFiles((dir, name) -> name.endsWith(".map"));
        if (files != null) {
            for (File file : files) {
                String name = FilenameUtils.removeExtension(file.getName());
                if (savedMaps.stream().noneMatch(map -> map.name.equals(name))) {
                    savedMaps.add(new SavedMap(name, file.toPath()));
                }
            }
        }
    }

    protected void loadBattlefield() {
        ScrollPane scrollPane = new ZoomableScrollPane();
        scrollPane.setStyle("-fx-background-color:transparent;");
        scrollPane.maxWidthProperty().bind(root.widthProperty().subtract(250));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setCenter(scrollPane);

        StackPane stackPane = new StackPane();
        scrollPane.setContent(stackPane);

        ImageView imageView = new ImageView(getMapPath().resolve(battlefield.getImagePath()).toUri().toString());
        stackPane.getChildren().add(imageView);

        gridCanvas = new Canvas();
        gridContext = gridCanvas.getGraphicsContext2D();
        stackPane.getChildren().add(gridCanvas);
        drawGrid();

        this.fieldCanvas = new Canvas();
        this.fieldContext = fieldCanvas.getGraphicsContext2D();
        stackPane.getChildren().add(fieldCanvas);


    }

    protected Path getMapPath() {
        return Utility.getHomeFolder().resolve("maps");
    }

    protected void drawGrid() {
        clearGrid();

        double xStep = gridCanvas.getWidth() / battlefield.getWidth();
        double yStep = gridCanvas.getHeight() / battlefield.getHeight();
        double[] padding = battlefield.getPadding();
        double topPad = padding[0] * yStep;
        double rightPad = padding[1] * xStep;
        double bottomPad = padding[2] * yStep;
        double leftPad = padding[3] * xStep;

        for (double x = leftPad; x < gridCanvas.getWidth() - rightPad; x += xStep) {
            gridContext.strokeLine(x, topPad, x, gridCanvas.getHeight() - bottomPad);
        }
        for (double y = topPad; y < gridCanvas.getHeight() - bottomPad; y += yStep) {
            gridContext.strokeLine(leftPad, y, gridCanvas.getWidth() - rightPad, y);
        }
    }

    protected void clearGrid() {
        gridContext.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
    }

    private static class SavedMap {
        private final String name;
        private final Path path;

        public SavedMap(String name, Path path) {
            this.name = name;
            this.path = path;
        }
    }
}
