package de.pnp.manager.model.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.*;

public class Graph<N, E> {

    protected ObservableSet<N> nodes;
    protected ObservableSet<Edge<N, E>> edges;

    public Graph() {
        this(Collections.emptyList());
    }

    public Graph(Collection<N> nodes) {
        this.nodes = FXCollections.observableSet(new HashSet<>());
        this.edges = FXCollections.observableSet(new HashSet<>());
        this.nodes.addAll(nodes);
    }

    public void addNode(N node) {
        this.nodes.add(node);
    }

    public void addNodes(Collection<N> nodes) {
        this.nodes.addAll(nodes);
    }

    public boolean removeNode(N node) {
        return this.nodes.remove(node);
    }

    public boolean tryAddEdge(N origin, N target) {
        return this.tryAddEdge(origin, target, null);
    }

    public boolean tryAddEdge(N origin, N target, E content) {
        if (nodes.contains(origin) && nodes.contains(target)) {
            this.edges.add(new Edge<>(origin, target, content));
            return true;
        }
        return false;
    }

    public void addEdge(N origin, N target) {
        this.addEdge(origin, target, null);
    }

    public void addEdge(N origin, N target, E content) {
        if (!nodes.contains(origin) || !nodes.contains(target)) {
            throw new NoSuchElementException("Nodes are not in the graph");
        }
        this.edges.add(new Edge<>(origin, target, content));
    }

    public void removeEdge(N origin, N target) {
        Collection<Edge<N, E>> toRemove = new ArrayList<>();

        for (Edge<N, E> edge : edges) {
            if (edge.origin == origin && edge.target == target) {
                toRemove.add(edge);
            }
        }

        toRemove.forEach(edges::remove);
    }

    public ObservableSet<N> getNodes() {
        return nodes;
    }

    public ObservableSet<Edge<N, E>> getEdges() {
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
