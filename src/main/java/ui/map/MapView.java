package ui.map;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.map.Map;
import ui.IView;
import ui.ViewPart;

public class MapView extends ViewPart {

    private final MapCanvas canvas;
    private ObjectProperty<Map> map;
    private ObjectProperty<Structure> selectedStructure;
    private final BooleanProperty loading;
    private final IntegerProperty shownYLayer;

    public MapView(IView parent) {
        super("Karten", parent);
        this.selectedStructure = new SimpleObjectProperty<>();
        this.loading = new SimpleBooleanProperty(false);
        this.shownYLayer = new SimpleIntegerProperty(0);
        this.map = new SimpleObjectProperty<>(null);
        this.canvas = new MapCanvas(map, shownYLayer);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 10, 10, 10));

        BorderPane rightSide = new BorderPane();
        root.setRight(rightSide);

        VBox generateBox = new VBox(10);
        generateBox.setAlignment(Pos.CENTER);
        generateBox.setPadding(new Insets(0, 0, 20, 0));
        rightSide.setTop(generateBox);

        ComboBox<Structure> structure = new ComboBox<>(FXCollections.observableArrayList(Structure.values()));
        this.selectedStructure.bind(structure.getSelectionModel().selectedItemProperty());
        structure.getSelectionModel().selectFirst();
        structure.setPrefWidth(120);
        generateBox.getChildren().add(structure);

        Button generateButton = new Button("Generiere");
        generateButton.setOnAction(ev -> generate());
        generateButton.disableProperty().bind(loading);
        generateButton.setPrefWidth(120);
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

        this.setContent(root);
    }

    private void generate() {
        map.set(new Map());

        Thread generateThread = new Thread(() -> {
            long time = System.currentTimeMillis();
            map.get().generate();
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

    private enum Structure {
        cave, crypt;

        @Override
        public String toString() {
            switch (this) {
                case cave:
                    return "Höhle";
                case crypt:
                    return "Krypta";
                default:
                    return "";
            }
        }
    }
}
