package de.pnp.manager.ui.battle.battlefield;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.battle.BattlefieldDetails;
import de.pnp.manager.model.interfaces.WithUnlocalizedName;
import de.pnp.manager.ui.IView;
import de.pnp.manager.ui.ViewPart;
import de.pnp.manager.ui.part.UpdatingListCell;
import de.pnp.manager.ui.part.ZoomableScrollPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static de.pnp.manager.main.LanguageUtility.getMessage;
import static de.pnp.manager.main.LanguageUtility.getMessageProperty;
import static de.pnp.manager.ui.ViewFactory.labelTextField;

public class BattlefieldCreationView extends ViewPart {

    protected ImageView imageView;
    protected Canvas gridCanvas;
    protected GraphicsContext gridContext;
    protected Canvas fieldCanvas;
    protected GraphicsContext fieldContext;
    protected ObjectProperty<CreationSteps> steps;
    protected VBox currentControls;
    protected ObjectMapper mapper;

    protected double widthHeightRatio;
    protected StringProperty name;
    protected BooleanProperty nameAlreadyUsed;
    protected ObjectProperty<Path> imagePath;
    protected IntegerProperty width;
    protected IntegerProperty height;
    protected DoubleProperty topPadding;
    protected DoubleProperty rightPadding;
    protected DoubleProperty bottomPadding;
    protected DoubleProperty leftPadding;
    protected ObservableList<FieldInfo> blocksVisibleList;
    protected ObservableList<FieldInfo> blocksAccessibleList;
    protected ObjectProperty<ClickType> clickType;

    public BattlefieldCreationView(IView parent) {
        super("battlefieldCreation.title", parent);

        this.mapper = new ObjectMapper();
        this.name = new SimpleStringProperty();
        this.nameAlreadyUsed = new SimpleBooleanProperty();
        this.imagePath = new SimpleObjectProperty<>(null);
        this.steps = new SimpleObjectProperty<>();
        this.width = new SimpleIntegerProperty(10);
        this.height = new SimpleIntegerProperty(10);
        this.topPadding = new SimpleDoubleProperty(0);
        this.rightPadding = new SimpleDoubleProperty(0);
        this.bottomPadding = new SimpleDoubleProperty(0);
        this.leftPadding = new SimpleDoubleProperty(0);
        this.width.addListener((ob, o, n) -> drawGrid());
        this.height.addListener((ob, o, n) -> drawGrid());
        this.width.addListener((ob, o, n) -> height.set((int) (n.doubleValue() * widthHeightRatio)));
        this.height.addListener((ob, o, n) -> width.set((int) (n.doubleValue() / widthHeightRatio)));
        this.topPadding.addListener((ob, o, n) -> drawGrid());
        this.rightPadding.addListener((ob, o, n) -> drawGrid());
        this.bottomPadding.addListener((ob, o, n) -> drawGrid());
        this.leftPadding.addListener((ob, o, n) -> drawGrid());
        this.blocksVisibleList = FXCollections.observableList(new ArrayList<>());
        this.blocksAccessibleList = FXCollections.observableList(new ArrayList<>());
        this.blocksVisibleList.addListener((ListChangeListener<? super FieldInfo>) observable -> drawField());
        this.blocksAccessibleList.addListener((ListChangeListener<? super FieldInfo>) observable -> drawField());
        this.clickType = new SimpleObjectProperty<>();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        this.setContent(root);

        ScrollPane scrollPane = new ZoomableScrollPane();
        scrollPane.setStyle("-fx-background-color:transparent;");
        scrollPane.maxWidthProperty().bind(root.widthProperty().subtract(250));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setCenter(scrollPane);

        StackPane stackPane = new StackPane();
        scrollPane.setContent(stackPane);

        this.imageView = new ImageView();
        stackPane.getChildren().add(imageView);

        this.gridCanvas = new Canvas();
        this.gridContext = gridCanvas.getGraphicsContext2D();
        stackPane.getChildren().add(gridCanvas);

        this.fieldCanvas = new Canvas();
        this.fieldContext = fieldCanvas.getGraphicsContext2D();
        stackPane.getChildren().add(fieldCanvas);

        VBox controlContainer = new VBox(20);
        root.setRight(controlContainer);

        HBox nameBox = new HBox(5);
        nameBox.setAlignment(Pos.CENTER);
        controlContainer.getChildren().add(nameBox);

        Label nameLabel = new Label();
        nameLabel.textProperty().bind(LanguageUtility.getMessageProperty("battlefieldCreation.map.name"));
        nameLabel.setPrefWidth(60);
        nameBox.getChildren().add(nameLabel);

        TextField nameField = new TextField();
        nameField.setPrefWidth(150);
        nameField.textProperty().bindBidirectional(name);
        nameAlreadyUsed.addListener((ob, o, n) -> {
            if (n || name.get().isBlank()) {
                nameField.setStyle("-fx-text-box-border: #B22222;");
            } else {
                nameField.setStyle("");
            }
        });
        nameBox.getChildren().add(nameField);

        currentControls = new VBox(5);
        controlContainer.getChildren().add(currentControls);

        BorderPane flowControls = new BorderPane();
        flowControls.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(flowControls);

        Button exportButton = new Button();
        exportButton.textProperty().bind(getMessageProperty("battlefieldCreation.button.export"));
        exportButton.setPrefWidth(200);
        exportButton.setOnAction(ev -> export());
        exportButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> nameAlreadyUsed.get() || imagePath.get() == null || name.get().isBlank(),
                nameAlreadyUsed, imagePath, name
        ));
        flowControls.setCenter(exportButton);

        Button nextButton = new Button();
        nextButton.textProperty().bind(getMessageProperty("battlefieldCreation.button.next"));
        nextButton.setPrefWidth(200);
        nextButton.setOnAction(ev -> next());
        flowControls.setRight(nextButton);

        Button prevButton = new Button();
        prevButton.textProperty().bind(getMessageProperty("battlefieldCreation.button.previous"));
        prevButton.setPrefWidth(200);
        prevButton.setOnAction(ev -> previous());
        flowControls.setLeft(prevButton);

        this.steps.addListener((ob, o, n) -> {
            switch (n) {
                case BACKGROUND:
                    loadBackgroundControls();
                    break;
                case GRID:
                    loadGridControls();
                    break;
                case FIELD:
                    loadFieldControls();
                    break;
            }
        });
        this.steps.set(CreationSteps.BACKGROUND);
        nextButton.disableProperty().bind(Bindings.createBooleanBinding(() -> steps.get().last, steps));
        prevButton.disableProperty().bind(Bindings.createBooleanBinding(() -> steps.get().first, steps));

        fieldCanvas.setOnMouseClicked(ev -> {
            if (steps.get() == CreationSteps.FIELD) {
                double xStep = fieldCanvas.getWidth() / width.get();
                double yStep = fieldCanvas.getHeight() / height.get();
                double topPad = topPadding.get() * yStep;
                double rightPad = rightPadding.get() * xStep;

                int realX = (int) ((ev.getX() - rightPad) / xStep);
                int realY = (int) ((ev.getY() - topPad) / yStep);

                clickedField(realX, realY);
            }
        });

        try {
            Files.createDirectories(getMapPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.name.addListener((ob, o, n) -> {
            File file = new File(getMapPath().toString());
            nameAlreadyUsed.set(Arrays.stream(Objects.requireNonNull(file.listFiles((dir, name) -> name.endsWith(".map"))))
                    .anyMatch(f -> FilenameUtils.removeExtension(f.getName()).equals(n)));
        });
        this.name.set(getMessage("battlefieldCreation.map.defaultName"));
    }

    protected void add(Node node) {
        currentControls.getChildren().clear();
        currentControls.getChildren().add(node);
    }

    protected void loadBackgroundControls() {
        VBox controls = new VBox(5);
        add(controls);

        Button loadBackgroundButton = new Button();
        loadBackgroundButton.setPrefWidth(215);
        loadBackgroundButton.textProperty().bind(getMessageProperty("battlefieldCreation.background.button.load"));
        controls.getChildren().add(loadBackgroundButton);
        loadBackgroundButton.setOnAction(ev -> loadBackground());

        Button clearBackgroundButton = new Button();
        clearBackgroundButton.setPrefWidth(215);
        clearBackgroundButton.textProperty().bind(getMessageProperty("battlefieldCreation.background.button.clear"));
        controls.getChildren().add(clearBackgroundButton);
        clearBackgroundButton.setOnAction(ev -> clearBackground());
    }

    protected void loadGridControls() {
        VBox controls = new VBox(5);
        add(controls);

        Label sizeInfo = new Label();
        sizeInfo.textProperty().bind(getMessageProperty("battlefieldCreation.grid.sizeInfo"));
        controls.getChildren().add(sizeInfo);

        controls.getChildren().add(labelTextField("battlefieldCreation.grid.width", width));
        controls.getChildren().add(labelTextField("battlefieldCreation.grid.height", height));

        Label paddingInfo = new Label();
        paddingInfo.setPadding(new Insets(10, 0, 0, 0));
        paddingInfo.textProperty().bind(getMessageProperty("battlefieldCreation.grid.paddingInfo"));
        controls.getChildren().add(paddingInfo);

        controls.getChildren().add(labelTextField("battlefieldCreation.grid.topPadding", topPadding));
        controls.getChildren().add(labelTextField("battlefieldCreation.grid.rightPadding", rightPadding));
        controls.getChildren().add(labelTextField("battlefieldCreation.grid.bottomPadding", bottomPadding));
        controls.getChildren().add(labelTextField("battlefieldCreation.grid.leftPadding", leftPadding));
    }

    protected void loadFieldControls() {
        VBox controls = new VBox(5);
        add(controls);

        ComboBox<ClickType> clickBox = new ComboBox<>(FXCollections.observableArrayList(ClickType.values()));
        clickBox.setCellFactory(list -> new UpdatingListCell<>());
        clickBox.setButtonCell(new UpdatingListCell<>());
        clickBox.setPrefWidth(215);
        this.clickType.bind(clickBox.getSelectionModel().selectedItemProperty());
        clickBox.getSelectionModel().select(ClickType.ACCESSIBILITY);
        controls.getChildren().add(clickBox);
    }

    protected void loadBackground() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(LanguageUtility.getMessage("imageFiles"), "*.jpg", "*.png", "*.svg"),
                new FileChooser.ExtensionFilter(LanguageUtility.getMessage("allFiles"), "*.*"));

        File file = chooser.showOpenDialog(getStage());

        if (file != null) {
            imagePath.set(file.toPath());
            Image background = new Image(file.toURI().toString());
            gridCanvas.setHeight(background.getHeight());
            gridCanvas.setWidth(background.getWidth());
            fieldCanvas.setHeight(background.getHeight());
            fieldCanvas.setWidth(background.getWidth());
            widthHeightRatio = background.getHeight() / background.getWidth();
            imageView.setImage(background);
            next();
            drawGrid();
        }
    }

    protected void clearBackground() {
        imagePath.set(null);
        imageView.setImage(null);
    }

    protected void drawGrid() {
        gridContext.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        double xStep = gridCanvas.getWidth() / width.get();
        double yStep = gridCanvas.getHeight() / height.get();
        double topPad = topPadding.get() * yStep;
        double rightPad = rightPadding.get() * xStep;
        double bottomPad = bottomPadding.get() * yStep;
        double leftPad = leftPadding.get() * xStep;

        for (double x = leftPad; x < gridCanvas.getWidth() - rightPad; x += xStep) {
            gridContext.strokeLine(x, topPad, x, gridCanvas.getHeight() - bottomPad);
        }
        for (double y = topPad; y < gridCanvas.getHeight() - bottomPad; y += yStep) {
            gridContext.strokeLine(leftPad, y, gridCanvas.getWidth() - rightPad, y);
        }
    }

    protected void drawField() {
        fieldContext.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        double xStep = fieldCanvas.getWidth() / width.get();
        double yStep = fieldCanvas.getHeight() / height.get();
        double topPad = topPadding.get() * yStep;
        double rightPad = rightPadding.get() * xStep;

        fieldContext.save();
        fieldContext.setLineWidth(10);
        fieldContext.setStroke(Color.BLACK);
        for (FieldInfo info : blocksVisibleList) {
            double x = rightPad + info.x * xStep;
            double y = topPad + info.y * yStep;
            fieldContext.strokeLine(x + xStep / 2, y, x + xStep / 2, y + yStep);
            fieldContext.strokeLine(x, y + yStep / 2, x + xStep, y + yStep / 2);
        }
        fieldContext.restore();
        fieldContext.save();
        fieldContext.setLineWidth(10);
        fieldContext.setStroke(Color.RED);
        for (FieldInfo info : blocksAccessibleList) {
            double x = rightPad + info.x * xStep;
            double y = topPad + info.y * yStep;
            fieldContext.strokeLine(x, y, x + xStep, y + yStep);
            fieldContext.strokeLine(x, y + yStep, x + xStep, y);
        }
        fieldContext.restore();
    }

    protected void clickedField(int x, int y) {
        switch (clickType.get()) {
            case VISIBILITY:
                if (blocksVisibleList.stream().anyMatch(info -> info.x == x && info.y == y)) {
                    blocksVisibleList.removeIf(info -> info.x == x && info.y == y);
                } else {
                    blocksVisibleList.add(new FieldInfo(x, y));
                }
                break;
            case ACCESSIBILITY:
                if (blocksAccessibleList.stream().anyMatch(info -> info.x == x && info.y == y)) {
                    blocksAccessibleList.removeIf(info -> info.x == x && info.y == y);
                } else {
                    blocksAccessibleList.add(new FieldInfo(x, y));
                }
                break;
        }
    }

    protected void previous() {
        this.steps.set(this.steps.get().getPrevious());
    }

    protected void next() {
        this.steps.set(this.steps.get().getNext());
    }

    protected Path getMapPath() {
        return Utility.getHomeFolder().resolve("maps");
    }

    protected void export() {
        if (!nameAlreadyUsed.get() && imagePath.get() != null && !name.get().isBlank()) {
            try {
                final Path iPath = imagePath.get();
                String relativePath;

                if (iPath.startsWith(getMapPath())) {
                    relativePath = iPath.getFileName().toString();
                } else {
                    relativePath = name.get() + "." + FilenameUtils.getExtension(iPath.getFileName().toString());
                    Files.copy(iPath, getMapPath().resolve(relativePath));
                }

                final BattlefieldDetails.TileInfo[][] tileInfos = new BattlefieldDetails.TileInfo[width.get()][height.get()];

                for (int x = 0; x < width.get(); x++) {
                    for (int y = 0; y < height.get(); y++) {
                        tileInfos[x][y] = new BattlefieldDetails.TileInfo();
                    }
                }

                for (FieldInfo info : blocksAccessibleList) {
                    tileInfos[info.x][info.y].setAccessible(false);
                }
                for (FieldInfo info : blocksVisibleList) {
                    tileInfos[info.x][info.y].setBlockingVisibility(true);
                }

                final BattlefieldDetails details = new BattlefieldDetails(
                        width.get(), height.get(),
                        new double[]{topPadding.get(), rightPadding.get(), bottomPadding.get(), leftPadding.get()},
                        relativePath, tileInfos
                );

                File file = new File(getMapPath().resolve(name.get() + ".map").toString());

                if (file.createNewFile()) {
                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
                        mapper.writeValue(outputStream, details);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imagePath.set(null);
        blocksVisibleList.clear();
        blocksAccessibleList.clear();
        width.set(10);
        height.set(10);
        topPadding.set(0);
        rightPadding.set(0);
        bottomPadding.set(0);
        leftPadding.set(0);
        gridContext.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());
        fieldContext.clearRect(0, 0, fieldCanvas.getWidth(), fieldCanvas.getHeight());
        imageView.setImage(null);
        nameAlreadyUsed.set(true);
    }

    private static class FieldInfo {
        protected int x;
        protected int y;

        public FieldInfo(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private enum ClickType implements WithUnlocalizedName {
        VISIBILITY("battlefieldCreation.click.visibility"),
        ACCESSIBILITY("battlefieldCreation.click.accessibility");

        protected final String unlocalizedName;

        ClickType(String unlocalizedName) {
            this.unlocalizedName = unlocalizedName;
        }

        @Override
        public String getUnlocalizedName() {
            return unlocalizedName;
        }
    }

    private enum CreationSteps {
        BACKGROUND(true, false), GRID, FIELD(false, true);

        protected final boolean first, last;

        CreationSteps() {
            this(false, false);
        }

        CreationSteps(boolean first, boolean last) {
            this.first = first;
            this.last = last;
        }

        public CreationSteps getPrevious() {
            switch (this) {
                case GRID:
                    return BACKGROUND;
                case FIELD:
                    return GRID;
            }

            return this;
        }

        public CreationSteps getNext() {
            switch (this) {
                case BACKGROUND:
                    return GRID;
                case GRID:
                    return FIELD;
            }

            return this;
        }
    }
}
