package de.pnp.manager.ui.utility;

import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import de.pnp.manager.model.graph.Graph;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import de.pnp.manager.model.member.generation.Drop;
import de.pnp.manager.model.member.generation.GenerationBase;
import de.pnp.manager.model.member.generation.TypedGenerationBase;
import de.pnp.manager.model.other.Talent;
import de.pnp.manager.ui.part.GraphView;
import de.pnp.manager.ui.part.UpdatingListCell;
import de.pnp.manager.ui.utility.enemygraph.GenerationPositioningStrategy;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.pnp.manager.ui.ViewFactory.labelTextArea;
import static de.pnp.manager.ui.ViewFactory.labelTextField;

public class EnemyOverview extends BorderPane {

    protected GraphView<GenerationBase, String> graphView;
    protected ObjectProperty<GenerationBase> selectedGenerationBase;

    public EnemyOverview() {
        this.graphView = new GraphView<>();
        this.selectedGenerationBase = new SimpleObjectProperty<>(null);

        ComboBox<GenerationBase> selectBox = new ComboBox<>();

        graphView.setPositioningStrategy(new GenerationPositioningStrategy(200));
        graphView.setNodeFactory((n) -> {
            HBox box = new HBox(5);
            box.setMaxWidth(150);
            box.setPrefWidth(150);
            box.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, BorderWidths.DEFAULT)));
            box.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

            box.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.isControlDown()) {
                    selectBox.getSelectionModel().select(n);
                }
                selectedGenerationBase.set(n);
            });

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

        Label hintLabel = new Label();
        hintLabel.textProperty().bind(LanguageUtility.getMessageProperty("search.enemies.filterHint"));
        hintLabel.setWrapText(true);
        controlBox.getChildren().add(hintLabel);

        ScrollPane infoScrollPane = new ScrollPane();

        VBox info = new VBox(5);
        info.setPadding(new Insets(10));
        info.setAlignment(Pos.TOP_LEFT);
        info.setPrefWidth(280);
        infoScrollPane.setContent(info);

        Label infoLabel = new Label();
        infoLabel.textProperty().bind(LanguageUtility.getMessageProperty("search.enemies.info"));
        infoLabel.setFont(Font.font("", FontWeight.BOLD, 20));
        infoLabel.setPadding(new Insets(0, 0, 0, 20));
        info.getChildren().add(infoLabel);

        Label emptyLabel = new Label();
        emptyLabel.textProperty().bind(LanguageUtility.getMessageProperty("search.enemies.nothingSelected"));
        emptyLabel.setAlignment(Pos.CENTER);
        emptyLabel.setPadding(new Insets(10));
        info.getChildren().add(emptyLabel);

        selectedGenerationBase.addListener((ob, o, n) -> {
            info.getChildren().clear();
            info.getChildren().add(infoLabel);

            if (n != null) {
                info.getChildren().add(labelTextField("search.enemies.name", n.getName()));
                addIfNotEmpty(info.getChildren(), "search.enemies.advantages", n.getAdvantages());
                addIfNotEmpty(info.getChildren(), "search.enemies.disadvantages", n.getDisadvantages());
                addIfNotEmpty(info.getChildren(), "search.enemies.properties", n.getPropertyList());
                addIfNotEmpty(info.getChildren(), "search.enemies.groups", n.getParents()
                        .stream().map(GenerationBase::getName));
                addIfNotEmpty(info.getChildren(), "search.enemies.recommendedPrimaryAttributes", n.getPrimaryAttributes()
                        .stream().map(WithToStringProperty::toStringProperty).map(ObservableObjectValue::get));
                addIfNotEmpty(info.getChildren(), "search.enemies.recommendedSecondaryAttributes", n.getSecondaryAttributes()
                        .stream().map(WithToStringProperty::toStringProperty).map(ObservableObjectValue::get));
                addIfNotEmpty(info.getChildren(), "search.enemies.ableToWearInPrimary", n.getPrimaryWeaponTypes());
                addIfNotEmpty(info.getChildren(), "search.enemies.ableToWearInSecondary", n.getSecondaryWeaponTypes());
                addIfNotEmpty(info.getChildren(), "search.enemies.importantTalents", n.getMainTalents().stream().map(Talent::getName));
                addIfNotEmpty(info.getChildren(), "search.enemies.forbiddenTalents", n.getForbiddenTalents().stream().map(Talent::getName));
                addIfNotEmpty(info.getChildren(), "search.enemies.drops", n.getDrops().stream().map(Drop::getName));
            } else {
                info.getChildren().add(emptyLabel);
            }
        });

        this.setRight(infoScrollPane);
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

    private void addIfNotEmpty(ObservableList<Node> list, String key, Stream<String> stream) {
        addIfNotEmpty(list, key, stream.collect(Collectors.toList()));
    }

    private void addIfNotEmpty(ObservableList<Node> list, String key, Collection<String> collection) {
        if (!collection.isEmpty()) {
            list.add(labelTextArea(key, collectionToString(collection)));
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
