package de.pnp.manager.ui.part.interfaces;

import javafx.scene.layout.Region;
import de.pnp.manager.model.graph.Graph;
import de.pnp.manager.ui.part.GraphView;

import java.util.Map;

public interface IPositioningStrategy<N> {

    void position(Graph<N, ?> graph, Map<N, Region> regionMap, GraphView<N, ?> graphView);
}
