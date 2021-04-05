package model.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Graph<N, E> {

    protected ObservableList<N> nodes;
    protected ObservableList<Edge<N, E>> edges;

    public Graph() {
        this(Collections.emptyList());
    }

    public Graph(Collection<N> nodes) {
        this.nodes = FXCollections.observableArrayList(nodes);
        this.edges = FXCollections.observableArrayList();
    }

    public void addNode(N node) {
        this.nodes.add(node);
    }

    public boolean removeNode(N node) {
        return this.nodes.remove(node);
    }

    public void addEdge(N origin, N target) {
        this.edges.add(new Edge<>(origin, target, null));
    }

    public void addEdge(N origin, N target, E content) {
        this.edges.add(new Edge<>(origin, target, content));
    }

    public void removeEdge(N origin, N target) {
        Collection<Edge<N, E>> toRemove = new ArrayList<>();

        for (Edge<N, E> edge : edges) {
            if (edge.origin == origin && edge.target == target) {
                toRemove.add(edge);
            }
        }

        edges.removeAll(toRemove);
    }

    public ObservableList<N> getNodes() {
        return nodes;
    }

    public ObservableList<Edge<N, E>> getEdges() {
        return edges;
    }

    public static class Edge<N, E> {

        public final N origin;
        public final N target;
        public final E content;

        public Edge(N origin, N target, E content) {
            this.origin = origin;
            this.target = target;
            this.content = content;
        }
    }
}
