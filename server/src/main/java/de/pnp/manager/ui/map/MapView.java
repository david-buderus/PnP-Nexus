package de.pnp.manager.ui.map;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import de.pnp.manager.model.map.Map;
import de.pnp.manager.model.map.specification.CryptSpecification;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import de.pnp.manager.ui.part.UpdatingListCell;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Random;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;
import static de.pnp.manager.ui.ViewFactory.labelTextField;

public class MapView extends ViewPart {

    private final MapCanvas canvas;
    private final ObjectProperty<Map> map;
    private final ObjectProperty<Structure> selectedStructure;
    private final LongProperty seed;
    private final IntegerProperty width, height, depth;
    private final BooleanProperty loading;
    private final IntegerProperty shownYLayer;
    private final Random random;

    public MapView(IView parent) {
        super("map.title", parent);
        this.selectedStructure = new SimpleObjectProperty<>();
        this.loading = new SimpleBooleanProperty(false);
        this.shownYLayer = new SimpleIntegerProperty(0);
        this.map = new SimpleObjectProperty<>(null);
        this.seed = new SimpleLongProperty(5411351666781167994L);
        this.width = new SimpleIntegerProperty(50);
        this.height = new SimpleIntegerProperty(10);
        this.depth = new SimpleIntegerProperty(50);
        this.canvas = new MapCanvas(map, shownYLayer);
        this.random = new Random();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 10, 10, 10));

        BorderPane rightSide = new BorderPane();
        rightSide.setPadding(new Insets(0, 0, 0, 10));
        root.setRight(rightSide);

        VBox generateBox = new VBox(10);
        generateBox.setAlignment(Pos.CENTER);
        generateBox.setPadding(new Insets(0, 0, 20, 0));
        rightSide.setTop(generateBox);

        ComboBox<Structure> structure = new ComboBox<>(FXCollections.observableArrayList(Structure.values()));
        this.selectedStructure.bind(structure.getSelectionModel().selectedItemProperty());
        structure.getSelectionModel().select(Structure.crypt);
        structure.setCellFactory(list -> new UpdatingListCell<>());
        structure.setButtonCell(new UpdatingListCell<>());
        structure.setPrefWidth(215);
        generateBox.getChildren().add(structure);

        generateBox.getChildren().add(labelTextField("map.info.width", width));

        generateBox.getChildren().add(labelTextField("map.info.height", height));

        generateBox.getChildren().add(labelTextField("map.info.depth", depth));

        generateBox.getChildren().add(labelTextField("map.info.seed", seed));

        Button seedButton = new Button();
        seedButton.textProperty().bind(getMessageProperty("map.button.randomSeed"));
        seedButton.setOnAction(ev -> seed.set(random.nextLong()));
        seedButton.setPrefWidth(215);
        generateBox.getChildren().add(seedButton);

        Button generateButton = new Button();
        generateButton.textProperty().bind(getMessageProperty("map.button.generate"));
        generateButton.setOnAction(ev -> generate());
        generateButton.disableProperty().bind(loading);
        generateButton.setPrefWidth(215);
        generateBox.getChildren().add(generateButton);

        VBox layerBox = new VBox(10);
        layerBox.setAlignment(Pos.CENTER);
        rightSide.setCenter(layerBox);

        Button upButton = new Button("U");
        upButton.setDisable(true);
        upButton.setPrefSize(40, 40);
        upButton.setOnAction(ev -> shownYLayer.set(Math.min(shownYLayer.get() + 1, map.get().getHeight() - 1)));
        layerBox.getChildren().add(upButton);

        Button downButton = new Button("D");
        downButton.setDisable(true);
        downButton.setPrefSize(40, 40);
        downButton.setOnAction(ev -> shownYLayer.set(Math.max(shownYLayer.get() - 1, 0)));
        layerBox.getChildren().add(downButton);

        this.map.addListener((ob, o, n) -> {
            upButton.setDisable(n != null && shownYLayer.get() >= map.get().getHeight() - 1);
            downButton.setDisable(n != null && shownYLayer.get() < 1);
        });
        this.shownYLayer.addListener((ob, o, n) -> {
            upButton.setDisable(n.intValue() >= map.get().getHeight() - 1);
            downButton.setDisable(n.intValue() < 1);
        });

        this.canvas.setPrefSize(500, 500);
        root.setCenter(canvas);

        BorderPane info = new BorderPane();
        root.setBottom(info);

        Label coordinates = new Label();
        coordinates.textProperty().bind(canvas.getMouseX().asString().concat(", ").concat(canvas.getMouseY()).concat(", ").concat(canvas.getMouseZ()));
        info.setRight(coordinates);

        this.setContent(root);
    }

    private void generate() {
        Map m = new Map(seed.get(), width.get(), height.get(), depth.get());
        m.setSpecification(new CryptSpecification(m));
        map.set(m);

        Thread generateThread = new Thread(() -> {
            long time = System.currentTimeMillis();
            m.generate();
            Platform.runLater(() -> {
                loading.set(false);
                canvas.refresh();
                shownYLayer.set(0);
            });
            System.out.println("Done after: " + (System.currentTimeMillis() - time) / 1000.0);
        });

        loading.set(true);
        generateThread.setDaemon(true);
        generateThread.start();
    }

    private enum Structure implements WithToStringProperty {
        cave, crypt;


        @Override
        public ReadOnlyStringProperty toStringProperty() {
            return LanguageUtility.getMessageProperty("map.structure." + super.toString());
        }
    }
}
