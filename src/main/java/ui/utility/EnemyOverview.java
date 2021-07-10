package ui.utility;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import manager.Database;
import manager.LanguageUtility;
import model.graph.Graph;
import model.member.generation.GenerationBase;
import model.member.generation.TypedGenerationBase;
import ui.part.GraphView;
import ui.part.UpdatingListCell;
import ui.utility.enemygraph.GenerationPositioningStrategy;

import java.util.Collection;

import static ui.ViewFactory.labelTextArea;
import static ui.ViewFactory.labelTextField;

public class EnemyOverview extends BorderPane {

    protected GraphView<GenerationBase, String> graphView;
    protected ObjectProperty<GenerationBase> selectedGenerationBase;

    public EnemyOverview() {
        this.graphView = new GraphView<>();
        this.selectedGenerationBase = new SimpleObjectProperty<>(null);

        graphView.setPositioningStrategy(new GenerationPositioningStrategy(200));
        graphView.setNodeFactory((n) -> {
            HBox box = new HBox(5);
            box.setMaxWidth(150);
            box.setPrefWidth(150);
            box.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

            box.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> selectedGenerationBase.set(n));

            Label name = new Label(n.getName());
            name.setPadding(new Insets(5));
            name.setWrapText(true);
            box.getChildren().add(name);

            return box;
        });
        graphView.setPadding(new Insets(5));

        Database.generationBaseList.addListener((ob, o, n) -> reload());

        ScrollPane scrollPane = new ScrollPane();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setContent(graphView);

        graphView.prefHeightProperty().bind(scrollPane.heightProperty());

        this.setCenter(scrollPane);

        VBox controlBox = new VBox(5);
        controlBox.setPadding(new Insets(5));
        this.setLeft(controlBox);

        ComboBox<GenerationBase> selectBox = new ComboBox<>();
        selectBox.setCellFactory(list -> new UpdatingListCell<>());
        selectBox.setButtonCell(new UpdatingListCell<>());
        selectBox.setItems(Database.generationBaseList);
        controlBox.getChildren().add(selectBox);

        selectBox.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            if (n != null) {
                reload(n);
            } else {
                reload();
            }
        });

        Button resetButton = new Button();
        resetButton.textProperty().bind(LanguageUtility.getMessageProperty("search.enemies.reset"));
        resetButton.prefWidthProperty().bind(selectBox.widthProperty());
        resetButton.setOnAction(ev -> {
            selectedGenerationBase.set(null);
            selectBox.getSelectionModel().select(null);
        });
        controlBox.getChildren().add(resetButton);

        VBox info = new VBox(5);
        info.setPadding(new Insets(10));
        info.setAlignment(Pos.TOP_LEFT);
        info.setPrefWidth(280);

        Label infoLabel = new Label();
        infoLabel.textProperty().bind(LanguageUtility.getMessageProperty("search.enemies.info"));
        infoLabel.setFont(Font.font("", FontWeight.BOLD, 20));
        info.getChildren().add(infoLabel);

        Label emptyLabel = new Label();
        emptyLabel.textProperty().bind(LanguageUtility.getMessageProperty("search.enemies.nothingSelected"));
        emptyLabel.setAlignment(Pos.CENTER);
        info.getChildren().add(emptyLabel);

        selectedGenerationBase.addListener((ob, o, n) -> {
            info.getChildren().clear();
            info.getChildren().add(infoLabel);

            if (n != null) {
                info.getChildren().add(labelTextField("search.enemies.name", n.getName()));
                info.getChildren().add(labelTextArea("search.enemies.advantages", collectionToString(n.getAdvantages())));
                info.getChildren().add(labelTextArea("search.enemies.disadvantages", collectionToString(n.getDisadvantages())));
            } else {
                info.getChildren().add(emptyLabel);
            }
        });

        this.setRight(info);
    }

    private void reload() {

        Graph<GenerationBase, String> graph = new Graph<>();

        for (GenerationBase generationBase : Database.generationBaseList) {
            graph.addNode(generationBase);
        }

        for (GenerationBase generationBase : Database.generationBaseList) {
            if (generationBase instanceof TypedGenerationBase<?>) {
                TypedGenerationBase<?> typedGenerationBase = (TypedGenerationBase<?>) generationBase;

                for (GenerationBase subType : typedGenerationBase.getSubTypes()) {
                    graph.tryAddEdge(generationBase, subType);
                }
            }
        }

        graphView.setGraph(graph);
    }

    private void reload(GenerationBase root) {
        Graph<GenerationBase, String> graph = new Graph<>();
        buildGraph(graph, root);
        graphView.setGraph(graph);
    }

    private void buildGraph(Graph<GenerationBase, String> graph, GenerationBase node) {
        graph.addNode(node);

        if (node instanceof TypedGenerationBase<?>) {
            TypedGenerationBase<?> typedNode = (TypedGenerationBase<?>) node;

            for (GenerationBase child : typedNode.getSubTypes()) {
                buildGraph(graph, child);
                graph.addEdge(node, child);
            }
        }
    }

    private String collectionToString(Collection<String> lines) {
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String[] parts = line.split("\\r?\\n");
            for (int i = 0; i < parts.length; i++) {
                if (i == 0) {
                    result.append("- ").append(parts[i]).append("\n");
                } else {
                    result.append("  ").append(parts[i]).append("\n");
                }
            }
        }

        return result.toString();
    }
}
