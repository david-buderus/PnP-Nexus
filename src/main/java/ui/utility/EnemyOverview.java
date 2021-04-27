package ui.utility;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import manager.Database;
import model.graph.Graph;
import model.member.generation.GenerationBase;
import model.member.generation.specs.*;
import ui.part.GraphView;

import java.util.Random;

public class EnemyOverview extends GraphView<GenerationBase, String> {

    public EnemyOverview() {
        super();
        this.setPrefSize(500, 500);

        this.positioningStrategy = (graph, nodeMap, view) -> {
            Random rand = new Random();

            for (GenerationBase node : graph.getNodes()) {

                Region region = nodeMap.get(node);

                region.layoutXProperty().bind(widthProperty().multiply(rand.nextDouble()));

                if (node instanceof Characterisation) {
                    region.layoutYProperty().bind(heightProperty().multiply(0.1));
                }
                if (node instanceof Race) {
                    region.layoutYProperty().bind(heightProperty().multiply(0.3));
                }
                if (node instanceof Profession) {
                    region.layoutYProperty().bind(heightProperty().multiply(0.5));
                }
                if (node instanceof FightingStyle) {
                    region.layoutYProperty().bind(heightProperty().multiply(0.7));
                }
                if (node instanceof Specialisation) {
                    region.layoutYProperty().bind(heightProperty().multiply(0.9));
                }
            }

        };
        this.nodeFactory = (n) -> {
            HBox box = new HBox(5);
            box.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

            Label name = new Label(n.getName());
            name.setPadding(new Insets(5));
            box.getChildren().add(name);

            return box;
        };

        Database.specialisationList.addListener((ob, o, n) -> reload());
    }

    private void reload() {
        Graph<GenerationBase, String> graph = new Graph<>();

        for (Characterisation characterisation : Database.characterisationList) {
            graph.addNode(characterisation);
        }
        for (Race race : Database.raceList) {
            graph.addNode(race);
        }
        for (Profession profession : Database.professionList) {
            graph.addNode(profession);
        }
        for (FightingStyle fightingStyle : Database.fightingStyleList) {
            graph.addNode(fightingStyle);
        }
        for (Specialisation specialisation : Database.specialisationList) {
            graph.addNode(specialisation);
        }

        for (Characterisation characterisation : Database.characterisationList) {
            for (Race subType : characterisation.getSubTypes()) {
                graph.addEdge(characterisation, subType);
            }
        }
        for (Race race : Database.raceList) {
            for (Profession subType : race.getSubTypes()) {
                graph.addEdge(race, subType);
            }
        }
        for (Profession profession : Database.professionList) {
            for (FightingStyle subType : profession.getSubTypes()) {
                graph.addEdge(profession, subType);
            }
        }
        for (FightingStyle fightingStyle : Database.fightingStyleList) {
            for (Specialisation subType : fightingStyle.getSubTypes()) {
                graph.addEdge(fightingStyle, subType);
            }
        }
        this.setGraph(graph);
    }
}
