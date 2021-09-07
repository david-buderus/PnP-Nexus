package ui.part;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import model.graph.Graph;
import ui.part.interfaces.EdgeFactory;
import ui.part.interfaces.IPositioningStrategy;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

public class GraphView<N, E> extends Region {

    protected ObjectProperty<Graph<N, E>> graph;
    protected Function<N, Region> nodeFactory;
    protected EdgeFactory<E> edgeFactory;
    protected IPositioningStrategy<N> positioningStrategy;

    private final SetChangeListener<N> nodeListener;
    private final SetChangeListener<Graph.Edge<N, E>> edgeListener;
    private final HashMap<N, Region> nodeMap;

    public GraphView() {
        this(new Graph<>());
    }

    public GraphView(Graph<N, E> graph) {
        this.graph = new SimpleObjectProperty<>();
        this.nodeMap = new HashMap<>();
        this.nodeListener = (c) -> alignGraph();
        this.edgeListener = (c) -> alignGraph();
        this.nodeFactory = (n) -> new Label(n.toString());
        this.edgeFactory = (startX, startY, endX, endY, content) -> {
            Line line = new Line();
            line.startXProperty().bind(startX);
            line.startYProperty().bind(startY);
            line.endXProperty().bind(endX);
            line.endYProperty().bind(endY);
            return line;
        };
        this.positioningStrategy = (g, regionMap, view) -> {
            Random rand = new Random();

            for (Region region : regionMap.values()) {
                region.layoutXProperty().bind(widthProperty().multiply(rand.nextDouble()));
                region.layoutYProperty().bind(heightProperty().multiply(rand.nextDouble()));
            }

        };

        this.graph.addListener((ob, o, n) -> {
            if (o != null) {
                o.getNodes().removeListener(nodeListener);
                o.getEdges().removeListener(edgeListener);
            }
            if (n != null) {
                n.getNodes().addListener(nodeListener);
                n.getEdges().addListener(edgeListener);
                alignGraph();
            }
        });
        this.graph.set(graph);
    }

    protected void alignGraph() {
        this.getChildren().clear();
        this.nodeMap.clear();

        for (N node : getGraph().getNodes()) {
            Region region = nodeFactory.apply(node);
            this.getChildren().add(region);
            this.nodeMap.put(node, region);
        }
        for (N node : getGraph().getNodes()) {
            this.positioningStrategy.position(getGraph(), nodeMap, this);
        }

        for (Graph.Edge<N, E> edge : getGraph().getEdges()) {
            Region origin = nodeMap.get(edge.origin);
            Region target = nodeMap.get(edge.target);

            this.getChildren().add(0, edgeFactory.create(getCenterX(origin), getCenterY(origin),
                    getCenterX(target), getCenterY(target), edge.content));
        }
    }

    protected DoubleBinding getCenterX(Region region) {
        return region.layoutXProperty().add(region.widthProperty().divide(2));
    }

    protected DoubleBinding getCenterY(Region region) {
        return region.layoutYProperty().add(region.heightProperty().divide(2));
    }

    public Graph<N, E> getGraph() {
        return graph.get();
    }

    public ObjectProperty<Graph<N, E>> graphProperty() {
        return graph;
    }

    public void setGraph(Graph<N, E> graph) {
        this.graph.set(graph);
    }

    public Function<N, Region> getNodeFactory() {
        return nodeFactory;
    }

    public void setNodeFactory(Function<N, Region> nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public EdgeFactory<E> getEdgeFactory() {
        return edgeFactory;
    }

    public void setEdgeFactory(EdgeFactory<E> edgeFactory) {
        this.edgeFactory = edgeFactory;
    }

    public IPositioningStrategy<N> getPositioningStrategy() {
        return positioningStrategy;
    }

    public void setPositioningStrategy(IPositioningStrategy<N> positioningStrategy) {
        this.positioningStrategy = positioningStrategy;
    }
}
