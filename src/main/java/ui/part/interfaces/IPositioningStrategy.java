package ui.part.interfaces;

import javafx.scene.layout.Region;
import model.graph.Graph;
import ui.part.GraphView;

import java.util.Map;

public interface IPositioningStrategy<N> {

    void position(Graph<N, ?> graph, Map<N, Region> regionMap, GraphView<N, ?> graphView);
}
