package ui.part;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import model.graph.Graph;

import java.util.HashMap;
import java.util.Random;

public class GraphView<N, E> extends Region {

    protected ObjectProperty<Graph<N, E>> graph;

    private ListChangeListener<N> nodeListener;
    private ListChangeListener<Graph.Edge<N, E>> edgeListener;
    private HashMap<N, Region> nodeMap;

    public GraphView() {
        this(new Graph<>());
    }

    public GraphView(Graph<N, E> graph) {
        this.graph = new SimpleObjectProperty<>();
        this.nodeMap = new HashMap<>();
        this.nodeListener = (c) -> alignGraph();
        this.edgeListener = (c) -> alignGraph();

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

        Random rand = new Random();

        for (N node : getGraph().getNodes()) {
            Button label = new Button(node.toString());
            this.getChildren().add(label);
            label.layoutXProperty().bind(widthProperty().divide(rand.nextDouble()));
            label.layoutYProperty().bind(heightProperty().divide(rand.nextDouble()));
            widthProperty().divide(rand.nextDouble()).addListener((ob, o, n) -> System.out.println(n));
            //label.relocate(rand.nextInt(500), rand.nextInt(500));
            this.nodeMap.put(node, label);
        }

        for (Graph.Edge<N, E> edge : getGraph().getEdges()) {
            Region origin = nodeMap.get(edge.origin);
            Region target = nodeMap.get(edge.target);

            Line line = new Line();
            line.startXProperty().bind(getLineX(origin));
            line.startYProperty().bind(getLineY(origin));
            line.endXProperty().bind(getLineX(target));
            line.endYProperty().bind(getLineY(target));
            this.getChildren().add(0, line);
        }
    }

    protected DoubleBinding getLineX(Region region) {
        return region.layoutXProperty().add(region.widthProperty().divide(2));
    }

    protected DoubleBinding getLineY(Region region) {
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
}
